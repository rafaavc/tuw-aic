package aic.g3t1.common.kafka;

import aic.g3t1.common.taxiposition.TaxiPosition;
import aic.g3t1.common.environment.EnvironmentVariables;
import aic.g3t1.common.exceptions.MissingEnvironmentVariableException;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

public class ProducerFactory {
    public static KafkaProducer<String, TaxiPosition> createProducer() throws MissingEnvironmentVariableException {
        var properties = new Properties();
        properties.setProperty("bootstrap.servers", EnvironmentVariables.getVariable("KAFKA_BOOTSTRAP_SERVER"));
        properties.setProperty("linger.ms", String.valueOf(1)); // time to wait before sending messages out to Kafka
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "aic.g3t1.common.taxiposition.TaxiPositionSerializer");

        return new KafkaProducer<>(properties);
    }
}
