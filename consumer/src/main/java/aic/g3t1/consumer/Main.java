package aic.g3t1.consumer;

import aic.g3t1.consumer.topology.AicTopology;
import org.apache.storm.Config;

public class Main {

    public static void main(String[] args) throws Exception {
        AicTopology topology = new AicTopology();
        topology.submit(getConfig());
    }

    private static Config getConfig() {
        Config config = new Config();
        config.setDebug(true);
        config.setNumWorkers(3);
        return config;
    }

}
