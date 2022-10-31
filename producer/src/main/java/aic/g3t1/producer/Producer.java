package aic.g3t1.producer;

import aic.g3t1.common.exceptions.MissingEnvironmentVariableException;
import aic.g3t1.common.kafka.ProducerFactory;
import aic.g3t1.common.taxiposition.TaxiPosition;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

public class Producer {
    private final String topic = "taxi";
    private final String folder = "../taxi_data/";
    private List<TaxiPosition> taxiPositions = new ArrayList<>();

    public void run() throws Exception {
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
        var producer = ProducerFactory.createProducer();

        System.out.println("Starting to publish taxi data on topic " + topic);

        int i = 0;
        var lastSentPositionTimestamp = new Date(1970, 1, 1);
        long timeElapsedSinceLastSent = 0;
        long previousTime = 0;

        while(i < taxiPositions.size()) {
            var nextPositionTimestamp = taxiPositions.get(i).getTimestamp();
            var timeBetweenPositions = nextPositionTimestamp.getTime() - lastSentPositionTimestamp.getTime();

            var currentTime = System.currentTimeMillis();
            timeElapsedSinceLastSent += currentTime - previousTime;
            previousTime = currentTime;

            if (timeElapsedSinceLastSent >= timeBetweenPositions / speed) {
                while (i < taxiPositions.size()) {
                    var nextPosition = taxiPositions.get(i);
                    if (nextPosition.getTimestamp().after(nextPositionTimestamp)) {
                        break;
                    }
                    var record = new ProducerRecord<>(topic, String.valueOf(i), nextPosition);
                    producer.send(record);
                    i++;
                }
                System.out.println("Sent positions with timestamp = " + nextPositionTimestamp);
                timeElapsedSinceLastSent = 0;
                lastSentPositionTimestamp = nextPositionTimestamp;
            } else {
                try {
                    Thread.sleep(Math.round((timeBetweenPositions - timeElapsedSinceLastSent) / speed));
                } catch (InterruptedException e) {
                    System.out.println("Error trying to sleep");
                }
            }

        }

        System.out.println("Finished publishing all the data. Disconnecting.");

        producer.close();
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
}
