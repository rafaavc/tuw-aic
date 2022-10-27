package aic.g3t1.common.taxiPosition;

import java.io.*;
import java.util.Date;

public class TaxiPosition implements Serializable {
    private final int taxiNumber;
    private final Date timestamp;
    private final double longitude;
    private final double latitude;

    public TaxiPosition(int taxiNumber, Date timestamp, double longitude, double latitude) {
        this.taxiNumber = taxiNumber;
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getTaxiNumber() {
        return taxiNumber;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
