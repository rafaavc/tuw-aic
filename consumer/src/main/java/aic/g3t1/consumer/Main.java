package aic.g3t1.consumer;

import aic.g3t1.common.model.taxiposition.TaxiPosition;
import aic.g3t1.consumer.redis.operation.IncrementDistanceOperation;
import aic.g3t1.consumer.redis.operation.SetAverageSpeedOperation;
import aic.g3t1.consumer.redis.operation.UpdateLocationOperation;
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
            System.exit(1);
        }
    }

    private static Config getConfig() {
        Config config = new Config();
        config.registerSerialization(TaxiPosition.class, TaxiPositionKryoSerializer.class);
        config.registerSerialization(Date.class);
        config.registerSerialization(IncrementDistanceOperation.class);
        config.registerSerialization(SetAverageSpeedOperation.class);
        config.registerSerialization(UpdateLocationOperation.class);
        config.setDebug(true);
        config.setNumWorkers(5);
        return config;
    }

}
