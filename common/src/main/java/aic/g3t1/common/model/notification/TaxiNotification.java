package aic.g3t1.common.model.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class TaxiNotification implements Serializable {
    private final Date timestamp;
    private final int taxiNumber;

    public TaxiNotification(@JsonProperty("timestamp") Date timestamp,
                            @JsonProperty("taxiNumber") int taxiNumber) {
        this.timestamp = timestamp;
        this.taxiNumber = taxiNumber;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getTaxiNumber() {
        return taxiNumber;
    }

    @Override
    public String toString() {
        return "TaxiNotification{" +
                "timestamp=" + timestamp +
                ", taxiNumber=" + taxiNumber +
                '}';
    }
}
