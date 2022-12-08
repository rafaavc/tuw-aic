package aic.g3t1.consumer.topology;

import aic.g3t1.common.exceptions.MissingEnvironmentVariableException;
import aic.g3t1.consumer.bolt.CalculateDistanceBolt;
import aic.g3t1.consumer.bolt.KafkaTupleBolt;
import aic.g3t1.consumer.sink.DebugSink;
import aic.g3t1.consumer.sink.StoreInformationSink;
import aic.g3t1.consumer.spout.AicKafkaSpout;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import static aic.g3t1.consumer.redis.operation.RedisOperation.F_GROUP;
import static aic.g3t1.consumer.spout.TaxiPositionFields.F_TAXI_NUMBER;

public class AicTopology {

    private static final String REDIS_HOSTNAME = "redis";
    private static final int REDIS_PORT = 6379;

    private static final String TOPOLOGY_NAME = "aic-topology";
    private static final String KAFKA_SPOUT_ID = "kafka_spout";
    private static final String KAFKA_TUPLE_BOLT_ID = "kafka_tuple_bolt";
    private static final String CALCULATE_DISTANCE_BOLT_ID = "calculate_distance_bolt";
    private static final String STORE_INFORMATION_SINK_ID = "store_information_sink";
    private static final String DEBUG_SINK_ID = "debug_sink";

    private final TopologyBuilder BUILDER = new TopologyBuilder();

    public AicTopology() throws MissingEnvironmentVariableException {
        initialize();
    }

    private void initialize() throws MissingEnvironmentVariableException {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig.Builder()
                .setHost(REDIS_HOSTNAME)
                .setPort(REDIS_PORT)
                .build();

        BUILDER.setSpout(KAFKA_SPOUT_ID, new AicKafkaSpout(), 1);
        BUILDER.setBolt(KAFKA_TUPLE_BOLT_ID, new KafkaTupleBolt(), 1).globalGrouping(KAFKA_SPOUT_ID);
        BUILDER.setBolt(DEBUG_SINK_ID, new DebugSink()).fieldsGrouping(KAFKA_TUPLE_BOLT_ID, new Fields(F_TAXI_NUMBER));

        BUILDER.setBolt(CALCULATE_DISTANCE_BOLT_ID, new CalculateDistanceBolt())
                .fieldsGrouping(KAFKA_TUPLE_BOLT_ID, new Fields(F_TAXI_NUMBER));
        BUILDER.setBolt(STORE_INFORMATION_SINK_ID, new StoreInformationSink(jedisPoolConfig))
                .fieldsGrouping(CALCULATE_DISTANCE_BOLT_ID, new Fields(F_GROUP));
    }

    public void submit(Config config) throws AuthorizationException, InvalidTopologyException, AlreadyAliveException {
        StormSubmitter.submitTopology(TOPOLOGY_NAME, config, BUILDER.createTopology());
    }

}
