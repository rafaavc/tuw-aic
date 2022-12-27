package aic.g3t1.consumer.bolt;

import aic.g3t1.common.model.taxiposition.GeoLocation;
import aic.g3t1.common.model.taxiposition.TaxiPosition;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static aic.g3t1.consumer.bolt.TaxiSpeedFields.F_TAXI_SPEED;
import static aic.g3t1.consumer.spout.TaxiPositionFields.F_LATITUDE;
import static aic.g3t1.consumer.spout.TaxiPositionFields.F_LONGITUDE;
import static aic.g3t1.consumer.spout.TaxiPositionFields.F_TAXI_NUMBER;
import static aic.g3t1.consumer.spout.TaxiPositionFields.F_TIMESTAMP;

public class CalculateSpeedBolt extends BaseRichBolt {

    private static final double EPSILON = Math.ulp(1d);
    private transient OutputCollector collector;
    private final Map<Integer, TaxiPosition> lastPositions = new HashMap<>();

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        TaxiPosition taxiPosition = TaxiPosition.builder()
                .taxiNumber(input.getIntegerByField(F_TAXI_NUMBER))
                .timestamp((Date) input.getValueByField(F_TIMESTAMP))
                .longitude(input.getDoubleByField(F_LONGITUDE))
                .latitude(input.getDoubleByField(F_LATITUDE))
                .build();
        int taxiNumber = taxiPosition.getTaxiNumber();

        TaxiPosition lastPosition = lastPositions.put(taxiNumber, taxiPosition);

        if (lastPosition != null) {
            double distance = GeoLocation.distance(lastPosition.getLocation(), taxiPosition.getLocation());
            long time = taxiPosition.getTimestamp().getTime() - lastPosition.getTimestamp().getTime();
            // if 2 subsequent timestamps have exactly the same time, don't calculate the speed as it is undefined
            if (time > 0) {
                // distance (meters)/time (milliseconds) -> multiply by 3600 to get km/h
                double speed = distance / time * 3600;
                // If the distance is minuscule, the speed of the taxi is set to 0
                List<Object> newTuple;
                if (distance > EPSILON) {
                    newTuple = List.of(taxiNumber, speed);
                } else {
                    newTuple = List.of(taxiNumber, 0.0d);
                }
                collector.emit(input, newTuple);
            }
        }

        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(F_TAXI_NUMBER, F_TAXI_SPEED));
    }
}
