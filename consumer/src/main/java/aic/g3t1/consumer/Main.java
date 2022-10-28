package aic.g3t1.consumer;

import aic.g3t1.common.taxiposition.TaxiPosition;
import aic.g3t1.common.kafka.ConsumerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        var consumer = ConsumerFactory.createConsumer();

        consumer.subscribe(List.of("taxi"));
        while (true) {
            ConsumerRecords<String, TaxiPosition> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, TaxiPosition> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value().toString());
        }
    }
}
