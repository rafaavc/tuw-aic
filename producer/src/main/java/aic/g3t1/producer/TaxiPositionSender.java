package aic.g3t1.producer;

import aic.g3t1.common.taxiposition.TaxiPosition;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class TaxiPositionSender implements Runnable {
    private List<TaxiPosition> taxiPositions = new ArrayList<>();
    private static int positionsSent = 0;
    private ScheduledExecutorService executor;
    private KafkaProducer<String, TaxiPosition> producer;
    private String topic;

    public TaxiPositionSender(List<TaxiPosition> taxiPositions, ScheduledExecutorService executor, KafkaProducer<String, TaxiPosition> producer, String topic) {
        this.taxiPositions = taxiPositions;
        this.executor = executor;
        this.producer = producer;
        this.topic = topic;
    }

    @Override
    public void run() {
        var firstPositionToSend = taxiPositions.get(positionsSent);
        while (positionsSent < taxiPositions.size()) {
            var nextPosition = taxiPositions.get(positionsSent);
            if (nextPosition.getTimestamp().after(firstPositionToSend.getTimestamp())) {
                break;
            }
            var record = new ProducerRecord<>(topic, String.valueOf(positionsSent), nextPosition);
            producer.send(record);
            positionsSent++;
        }
        System.out.println("Sent positions with timestamp = " + firstPositionToSend.getTimestamp());

        if (positionsSent >= taxiPositions.size()) {
            System.out.println("All available taxi positions were sent, closing.");
            producer.close();
            executor.shutdown();
        }
    }
}
