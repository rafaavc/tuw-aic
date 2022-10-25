package aic.g3t1.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var properties = new Properties();
        properties.setProperty("bootstrap.servers", System.getenv("KAFKA_BOOTSTRAP_SERVER"));
        properties.setProperty("linger.ms", String.valueOf(1)); // time to wait before sending messages out to Kafka
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        String topic = "topic1";
        System.out.printf("Starting to publish on topic '%s'.\n", topic);

        var producer = new KafkaProducer<String, String>(properties);

        int i = 0;
        while (i++ < 1000) {
            var record = new ProducerRecord<>(topic, Integer.toString(i), Integer.toString(i * i));
            producer.send(record);
            System.out.printf("published key = %s, value = %s%n", record.key(), record.value());
            Thread.sleep(500);
        }

        producer.close();

        System.out.println("I've said all I wanted!");
    }
}
