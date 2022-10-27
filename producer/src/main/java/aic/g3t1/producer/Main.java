package aic.g3t1.producer;

import aic.g3t1.common.taxiPosition.TaxiPosition;
import aic.g3t1.common.kafka.ProducerFactory;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws Exception {
        var producer = ProducerFactory.createProducer();


        String topic = "taxi";
        System.out.printf("Starting to publish taxi data on topic '%s'.\n", topic);
        System.out.println(System.getProperty("user.dir"));

        String data_folder = "../taxi_data/";
        File firstTaxi = new File(data_folder + "1.txt");

        ArrayList<TaxiPosition> firstTaxiPositions = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(firstTaxi));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] splitLine = line.split(",");
            Date timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(splitLine[1]);
            firstTaxiPositions.add(new TaxiPosition(Integer.parseInt(splitLine[0]), timestamp, Double.parseDouble(splitLine[2]), Double.parseDouble(splitLine[3])));
        }

        for (var position: firstTaxiPositions) {
            var record = new ProducerRecord<>(topic, position.getTimestamp().toString(), position);
            producer.send(record);
            System.out.printf("topic = %s, key = %s, value = %s%n", record.topic(), record.key(), record.value().toString());
        }

        producer.close();

        System.out.println("I've said all I wanted!");
    }
}
