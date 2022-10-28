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
import java.util.ArrayList;
import java.util.List;

public class Producer {
    private final String topic = "taxi";
    private List<TaxiPosition> taxiPositions = new ArrayList<>();

    public void run() throws Exception {
        try {
            this.readTaxiData("../taxi_data/");
        } catch (IOException ex) {
            System.out.println("Couldn't read the taxi data");
            throw ex;
        }

        try {
            this.sendTaxiData();
        } catch (MissingEnvironmentVariableException ex) {
            System.out.println("Couldn't send the data via Kafka");
            throw ex;
        }
    }

    private void sendTaxiData() throws MissingEnvironmentVariableException {
        var producer = ProducerFactory.createProducer();

        System.out.println("Starting to publish taxi data on topic " + topic);

        for (var position: taxiPositions) {
            var record = new ProducerRecord<>(topic, position.toString(), position);
            producer.send(record);
            System.out.printf("topic = %s, key = %s, value = %s%n", record.topic(), record.key(), record.value());
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
