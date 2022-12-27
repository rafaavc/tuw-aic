package aic.g3t1.common.model.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class TaxiNotification implements Serializable {
    private final Type type;
    private final Date timestamp;
    private final int taxiNumber;

    public TaxiNotification(@JsonProperty("type") Type type,
                            @JsonProperty("timestamp") Date timestamp,
                            @JsonProperty("taxiNumber") int taxiNumber) {
        this.type = type;
        this.timestamp = timestamp;
        this.taxiNumber = taxiNumber;
    }

    public Type getType() {
        return type;
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
                "type=" + type +
                ", timestamp=" + timestamp +
                ", taxiNumber=" + taxiNumber +
                '}';
    }

    public enum Type {
        SPEEDING,
        LEAVING_PREDEFINED_AREA
    }
}
