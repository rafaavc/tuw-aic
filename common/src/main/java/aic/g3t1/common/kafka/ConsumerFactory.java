package aic.g3t1.common.kafka;

import aic.g3t1.common.model.taxiposition.TaxiPosition;
import aic.g3t1.common.environment.EnvironmentVariables;
import aic.g3t1.common.exceptions.MissingEnvironmentVariableException;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

public class ConsumerFactory {
    public static KafkaConsumer<String, TaxiPosition> createConsumer() throws MissingEnvironmentVariableException {
        var properties = new Properties();
        properties.setProperty("bootstrap.servers", EnvironmentVariables.getVariable("KAFKA_BOOTSTRAP_SERVER"));
        properties.setProperty("group.id", EnvironmentVariables.getVariable("KAFKA_GROUP_ID"));
        properties.setProperty("enable.auto.commit", "true");
        properties.setProperty("auto.commit.interval.ms", "1000");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "aic.g3t1.common.taxiposition.TaxiPositionDeserializer");

        return new KafkaConsumer<>(properties);
    }
}
