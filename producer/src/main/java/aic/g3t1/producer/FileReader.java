package aic.g3t1.producer;

import aic.g3t1.common.model.taxiposition.TaxiPosition;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class FileReader implements Callable<List<TaxiPosition>> {
    private final Path taxiFile;

    public FileReader(Path taxiFile) {
        this.taxiFile = taxiFile;
    }

    @Override
    public List<TaxiPosition> call() throws Exception {
        List<String> fileContent = Files.readAllLines(taxiFile);
        return fileContent.stream().map(csvLine -> {
            try {
                return new TaxiPosition(csvLine);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}
