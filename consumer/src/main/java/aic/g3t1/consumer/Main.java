package aic.g3t1.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        var properties = new Properties();
        properties.setProperty("bootstrap.servers", System.getenv("KAFKA_BOOTSTRAP_SERVER"));
        properties.setProperty("group.id", System.getenv("KAFKA_GROUP_ID"));
        properties.setProperty("enable.auto.commit", "true");
        properties.setProperty("auto.commit.interval.ms", "1000");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        var consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(List.of("topic1"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }
}
