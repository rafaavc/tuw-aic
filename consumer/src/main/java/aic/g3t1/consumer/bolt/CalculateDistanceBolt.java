package aic.g3t1.consumer.bolt;

import aic.g3t1.common.taxiposition.GeoLocation;
import aic.g3t1.common.taxiposition.TaxiPosition;
import aic.g3t1.consumer.redis.operation.IncrementDistanceOperation;
import org.apache.storm.redis.bolt.AbstractRedisBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static aic.g3t1.consumer.redis.operation.RedisOperation.*;
import static aic.g3t1.consumer.spout.TaxiPositionFields.*;

public class CalculateDistanceBolt extends AbstractRedisBolt {

    private static final long serialVersionUID = 8173659194652408935L;

    private final Map<Integer, TaxiPosition> lastPositions = new HashMap<>();

    public CalculateDistanceBolt(JedisPoolConfig config) {
        super(config);
    }

    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector collector) {
        super.prepare(map, topologyContext, collector);
    }

    @Override
    protected void process(Tuple tuple) {
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
            collector.emit(tuple, List.of(taxiNumber, new IncrementDistanceOperation(taxiNumber, distance)));
        }

        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(F_GROUP, F_REDIS_OPERATION));
    }

}
