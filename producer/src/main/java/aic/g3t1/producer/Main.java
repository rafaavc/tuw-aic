package aic.g3t1.producer;

import aic.g3t1.common.kafka.ProducerFactory;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Main {
    public static void main(String[] args) throws Exception {
        var producer = ProducerFactory.createProducer();


        String topic = "topic1";
        System.out.printf("Starting to publish on topic '%s'.\n", topic);

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
