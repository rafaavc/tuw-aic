package aic.g3t1.consumer;

import aic.g3t1.common.taxiposition.TaxiPosition;
import aic.g3t1.consumer.redis.operation.IncrementDistanceOperation;
import aic.g3t1.consumer.serialization.TaxiPositionKryoSerializer;
import aic.g3t1.consumer.topology.AicTopology;
import org.apache.storm.Config;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        try {
            AicTopology topology = new AicTopology();
            topology.submit(getConfig());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Config getConfig() {
        Config config = new Config();
        config.registerSerialization(TaxiPosition.class, TaxiPositionKryoSerializer.class);
        config.registerSerialization(Date.class);
        config.registerSerialization(IncrementDistanceOperation.class);
        config.setDebug(true);
        config.setNumWorkers(3);
        return config;
    }

}
