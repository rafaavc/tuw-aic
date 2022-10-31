package aic.g3t1.producer;

import aic.g3t1.common.exceptions.MissingEnvironmentVariableException;
import aic.g3t1.common.kafka.ProducerFactory;
import aic.g3t1.common.taxiposition.TaxiPosition;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Producer implements Runnable {
    private final String topic = "taxi";
    private final String folder = "../taxi_data/";
    private List<TaxiPosition> taxiPositions = new ArrayList<>();
    private static int positionsSent = 0;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);;
    private KafkaProducer<String, TaxiPosition> producer;
    private ScheduledFuture scheduledTask;

    public void start() throws Exception {
        try {
            this.readTaxiData(folder);
        } catch (IOException ex) {
            System.out.println("Couldn't read the taxi data");
            throw ex;
        }

        try {
            this.sendTaxiData(2);
        } catch (MissingEnvironmentVariableException ex) {
            System.out.println("Couldn't send the data via Kafka");
            throw ex;
        }
    }

    /**
     * Send the taxi data with an interval directly related to the real intervals of the data
     * @param speed Speed of sending data compared to the real speed. Value of 10 means it is 10x faster than real time
     * @throws MissingEnvironmentVariableException
     */
    private void sendTaxiData(double speed) throws MissingEnvironmentVariableException {
        producer = ProducerFactory.createProducer();

        System.out.println("Starting to publish taxi data on topic " + topic);

        scheduledTask = executor.scheduleAtFixedRate(this, 0, Math.round(1000/speed), TimeUnit.MILLISECONDS);
    }

    private void readTaxiData(String folder) throws IOException {
        Path dataFolder = Paths.get(folder);
        if (!Files.isDirectory(dataFolder)) {
            throw new FileNotFoundException("Folder with taxi data was not found.");
        }

        System.out.println("Reading the taxi data from the files in " + folder);

        List<List<TaxiPosition>> allTaxiLists = new ArrayList<>();
        int i = 0;
        List<Path> taxiFiles = Files.walk(dataFolder).filter(Files::isReadable).toList();
        for (Path taxiFile: taxiFiles) {
            if (!Files.isRegularFile(taxiFile) || !Files.isReadable(taxiFile)) {
                continue;
            }

            List<String> fileContent = Files.readAllLines(taxiFile);
            allTaxiLists.add(fileContent.stream().map(csvLine -> {
                try {
                    return new TaxiPosition(csvLine);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }).toList());
        }

        this.taxiPositions = mergeTaxiLists(allTaxiLists);
    }

    private List<TaxiPosition> mergeTaxiLists(List<List<TaxiPosition>> allTaxiLists) {
        List<TaxiPosition> mergeList = new ArrayList<>();
        for (var taxiList: allTaxiLists) {
            mergeList.addAll(taxiList);
        }
        System.out.println("Sorting all the taxi data by time");
        mergeList.sort(TaxiPosition::compareTo);
        return mergeList;
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
            scheduledTask.cancel(false);
        }
    }
}
