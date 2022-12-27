package aic.g3t1.producer;

import aic.g3t1.common.model.taxiposition.TaxiPosition;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class TaxiPositionSender implements Runnable {
    private final List<TaxiPosition> taxiPositions;
    private static int positionsSent = 0;
    private static Date nextTimestamp;
    private final ScheduledExecutorService executor;
    private final KafkaProducer<String, TaxiPosition> producer;
    private final String topic;

    public TaxiPositionSender(List<TaxiPosition> taxiPositions, ScheduledExecutorService executor, KafkaProducer<String, TaxiPosition> producer, String topic) {
        this.taxiPositions = taxiPositions;
        this.executor = executor;
        this.producer = producer;
        this.topic = topic;
        nextTimestamp = taxiPositions.get(0).getTimestamp();
    }

    @Override
    public void run() {
        while (positionsSent < taxiPositions.size()) {
            var nextPosition = taxiPositions.get(positionsSent);
            if (nextPosition.getTimestamp().after(nextTimestamp)) {
                nextTimestamp = new Date(nextTimestamp.getTime() + 1000);
                break;
            }
            var record = new ProducerRecord<>(topic, String.valueOf(positionsSent), nextPosition);
            producer.send(record);
            positionsSent++;
        }
        //System.out.println("Sent positions with timestamp = " + nextTimestamp);

        if (positionsSent >= taxiPositions.size()) {
            System.out.println("All available taxi positions were sent, closing.");
            producer.close();
            executor.shutdown();
        }
    }
}
