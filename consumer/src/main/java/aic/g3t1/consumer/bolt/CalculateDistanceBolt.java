package aic.g3t1.consumer.bolt;

import aic.g3t1.common.model.taxiposition.GeoLocation;
import aic.g3t1.common.model.taxiposition.TaxiPosition;
import aic.g3t1.consumer.redis.operation.IncrementDistanceOperation;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.*;

import static aic.g3t1.consumer.redis.operation.RedisOperation.F_GROUP;
import static aic.g3t1.consumer.redis.operation.RedisOperation.F_REDIS_OPERATION;
import static aic.g3t1.consumer.spout.TaxiPositionFields.*;

public class CalculateDistanceBolt extends BaseRichBolt {

    private static final long serialVersionUID = 8173659194652408935L;

    /**
     * The allowed rounding error for the minimum feasible distance in meters.
     * <p>
     * Example: If <code>EPSILON</code> is <code>Math.ulp(1d)</code>,
     * then we allow distances greater than the rounding error of 1 meter, which is around 2e-19d.
     */
    private static final double EPSILON = Math.ulp(1d);

    private transient OutputCollector collector;

    private final Map<Integer, TaxiPosition> lastPositions = new HashMap<>();
    private final Set<Integer> reportedTaxis = new HashSet<>();

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        TaxiPosition taxiPosition = TaxiPosition.builder()
                .taxiNumber(tuple.getIntegerByField(F_TAXI_NUMBER))
                .timestamp((Date) tuple.getValueByField(F_TIMESTAMP))
                .longitude(tuple.getDoubleByField(F_LONGITUDE))
                .latitude(tuple.getDoubleByField(F_LATITUDE))
                .build();
        int taxiNumber = taxiPosition.getTaxiNumber();

        TaxiPosition lastPosition = lastPositions.put(taxiNumber, taxiPosition);
        if (lastPosition != null) {
            double distance = GeoLocation.distance(lastPosition.getLocation(), taxiPosition.getLocation());
            List<Object> newTuple = List.of(taxiNumber, new IncrementDistanceOperation(taxiNumber, distance));
            // If the distance is minuscule, ignore it unless the taxi has not been reported yet
            if (distance > EPSILON) {
                collector.emit(tuple, newTuple);
            } else if (!reportedTaxis.contains(taxiNumber)) {
                collector.emit(tuple, newTuple);
                reportedTaxis.add(taxiNumber);
            }
        }

        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(F_GROUP, F_REDIS_OPERATION));
    }

}
