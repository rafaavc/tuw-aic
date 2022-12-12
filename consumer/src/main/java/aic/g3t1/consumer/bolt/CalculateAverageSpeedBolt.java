package aic.g3t1.consumer.bolt;

import aic.g3t1.consumer.redis.operation.SetAverageSpeedOperation;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static aic.g3t1.consumer.bolt.TaxiSpeedFields.F_TAXI_SPEED;
import static aic.g3t1.consumer.redis.operation.RedisOperation.F_GROUP;
import static aic.g3t1.consumer.redis.operation.RedisOperation.F_REDIS_OPERATION;
import static aic.g3t1.consumer.spout.TaxiPositionFields.F_TAXI_NUMBER;

public class CalculateAverageSpeedBolt extends BaseRichBolt {

    private transient OutputCollector collector;
    private final Map<Integer, List<Double>> taxiSpeeds = new HashMap<>();

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        int taxiNumber = input.getIntegerByField(F_TAXI_NUMBER);
        double taxiSpeed = input.getDoubleByField(F_TAXI_SPEED);

        taxiSpeeds.putIfAbsent(taxiNumber, new LinkedList<>());
        List<Double> taxiSpeedHistory = taxiSpeeds.get(taxiNumber);
        taxiSpeedHistory.add(taxiSpeed);
        double avgSpeed = taxiSpeedHistory.stream().collect(Collectors.averagingDouble(d -> d));
        List<Object> newTuple = List.of(taxiNumber, new SetAverageSpeedOperation(taxiNumber, avgSpeed));
        collector.emit(input, newTuple);
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(F_GROUP, F_REDIS_OPERATION));
    }
}
