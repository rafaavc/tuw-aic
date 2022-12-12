package aic.g3t1.producer;

import aic.g3t1.common.environment.EnvironmentVariables;
import aic.g3t1.common.exceptions.MissingEnvironmentVariableException;
import aic.g3t1.common.kafka.ProducerFactory;
import aic.g3t1.common.taxiposition.TaxiPosition;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Producer {
    private final String topic = "taxi";
    private final String folder = "../" + EnvironmentVariables.getVariable("TAXI_DATA_FOLDER");
    private final int speed = Integer.parseInt(EnvironmentVariables.getVariable("TAXI_DATA_SPEED"));
    private int numberTaxis = Integer.parseInt(EnvironmentVariables.getVariable("NUMBER_TAXIS"));
    private final List<TaxiPosition> taxiPositions = new ArrayList<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public Producer() throws MissingEnvironmentVariableException {
    }

    public void start() throws Exception {
        try {
            this.readTaxiData(folder);
        } catch (IOException ex) {
            System.out.println("Couldn't read the taxi data");
            throw ex;
        }

        try {
            this.sendTaxiData(speed);
        } catch (MissingEnvironmentVariableException ex) {
            System.out.println("Couldn't send the data via Kafka");
            throw ex;
        }
    }

    /**
     * Send the taxi data with an interval directly related to the real intervals of the data
     *
     * @param speed Speed of sending data compared to the real speed. Value of 10 means it is 10x faster than real time
     */
    private void sendTaxiData(double speed) throws MissingEnvironmentVariableException {
        KafkaProducer<String, TaxiPosition> producer = ProducerFactory.createProducer();

        System.out.println("Starting to publish taxi data on topic " + topic);

        TaxiPositionSender sender = new TaxiPositionSender(taxiPositions, executor, producer, topic);
        executor.scheduleAtFixedRate(sender, 0, Math.round(1000 / speed), TimeUnit.MILLISECONDS);
    }

    private void readTaxiData(String folder) throws IOException, InterruptedException {
        System.out.println(System.getProperty("user.dir"));
        Path dataFolder = Paths.get(folder);
        if (!Files.isDirectory(dataFolder)) {
            throw new FileNotFoundException("Folder with taxi data was not found.");
        }

        System.out.println("Reading the taxi data from the files in " + folder);

        // Get all files in folder and sort them based on the number in their name.
        // If they were sorted by name instead of the number, "10.txt" < "2.txt".
        List<Path> taxiFiles = new ArrayList<>(Files.walk(dataFolder).filter(Files::isReadable).filter(Files::isRegularFile).collect(Collectors.toList()));
        taxiFiles.sort(Comparator.comparingInt((Path file) -> Integer.parseInt(file.getFileName().toString().split("\\.")[0])));
        if (numberTaxis == -1) {
            numberTaxis = taxiFiles.size();
        } else if (taxiFiles.size() < numberTaxis) {
            System.out.println("Less taxi files that the number of taxis specified. Reading all files.");
            numberTaxis = taxiFiles.size();
        }

        List<FileReader> readTasks = new ArrayList<>();
        for (int i = 0; i < numberTaxis; i++) {
            Path taxiFile = taxiFiles.get(i);
            readTasks.add(new FileReader(taxiFile));
        }

        Executors.newFixedThreadPool(4).invokeAll(readTasks).forEach(taxiList -> {
            try {
                this.taxiPositions.addAll(taxiList.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        this.taxiPositions.sort(TaxiPosition::compareTo);
    }
}
