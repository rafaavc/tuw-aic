package aic.g3t1.consumer.bolt;

import aic.g3t1.common.model.taxiposition.TaxiPosition;
import aic.g3t1.consumer.redis.operation.UpdateLocationOperation;
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

import static aic.g3t1.consumer.redis.operation.RedisOperation.F_GROUP;
import static aic.g3t1.consumer.redis.operation.RedisOperation.F_REDIS_OPERATION;
import static aic.g3t1.consumer.spout.TaxiPositionFields.*;
import static aic.g3t1.consumer.spout.TaxiPositionFields.F_LATITUDE;

public class UpdateLocationBolt extends BaseRichBolt {

    private transient OutputCollector collector;
    private final Map<Integer, TaxiPosition> lastPositions = new HashMap<>();

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
            String location = String.format("%.6f, %.6f", taxiPosition.getLatitude(), taxiPosition.getLongitude());
            List<Object> newTuple = List.of(taxiNumber, new UpdateLocationOperation(taxiNumber, location));
            collector.emit(tuple, newTuple);
        }

        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(F_GROUP, F_REDIS_OPERATION));
    }

}
