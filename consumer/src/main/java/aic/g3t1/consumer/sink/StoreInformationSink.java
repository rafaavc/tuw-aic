package aic.g3t1.consumer.sink;

import aic.g3t1.consumer.redis.JedisCommandsCloseable;
import aic.g3t1.consumer.redis.operation.RedisOperation;
import org.apache.storm.redis.bolt.AbstractRedisBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.JedisCommands;

import java.util.Map;

import static aic.g3t1.consumer.redis.operation.RedisOperation.F_REDIS_OPERATION;

public class StoreInformationSink extends AbstractRedisBolt {

    private static final long serialVersionUID = -455773549648657467L;

    public StoreInformationSink(JedisPoolConfig config) {
        super(config);
    }

    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector collector) {
        super.prepare(map, topologyContext, collector);
    }

    @Override
    protected void process(Tuple tuple) {
        RedisOperation operation = (RedisOperation) tuple.getValueByField(F_REDIS_OPERATION);

        try (JedisCommandsCloseable jedisCmd = new JedisCommandsCloseable(this::getInstance, this::returnInstance)) {
            JedisCommands jedisCommands = jedisCmd.open();
            operation.perform(jedisCommands);
        }

        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // This is a sink -> no output fields
    }

}
