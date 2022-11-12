package aic.g3t1.consumer.topology;

import aic.g3t1.common.exceptions.MissingEnvironmentVariableException;
import aic.g3t1.consumer.spout.AicKafkaSpout;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

public class AicTopology {

    private static final String TOPOLOGY_NAME = "aic-topology";
    private static final String KAFKA_SPOUT_ID = "kafka_spout";

    private final TopologyBuilder BUILDER = new TopologyBuilder();

    public AicTopology() throws MissingEnvironmentVariableException {
        initialize();
    }

    private void initialize() throws MissingEnvironmentVariableException {
        BUILDER.setSpout(KAFKA_SPOUT_ID, new AicKafkaSpout());
    }

    public void submit(Config config) throws AuthorizationException, InvalidTopologyException, AlreadyAliveException {
        StormSubmitter.submitTopology(TOPOLOGY_NAME, config, BUILDER.createTopology());
    }

}
