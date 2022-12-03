package aic.g3t1.common.taxiposition;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaxiPosition implements Serializable, Comparable<TaxiPosition> {
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

    public TaxiPosition(String csvLine) throws ParseException {
        String[] splitLine = csvLine.split(",");
        this.taxiNumber = Integer.parseInt(splitLine[0]);
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(splitLine[1]);
        this.longitude = Double.parseDouble(splitLine[2]);
        this.latitude = Double.parseDouble(splitLine[3]);
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

    @Override
    public int compareTo(TaxiPosition taxiPosition) {
        if (this.timestamp.compareTo(taxiPosition.timestamp) != 0) {
            return this.timestamp.compareTo(taxiPosition.timestamp);
        }
        return this.taxiNumber - taxiPosition.taxiNumber;
    }

    @Override
    public String toString() {
        return "TaxiPosition{" +
                "taxiNumber=" + taxiNumber +
                ", timestamp=" + timestamp.toString() +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    public GeoLocation getLocation() {
        return new GeoLocation(latitude, longitude);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int taxiNumber;
        private Date timestamp;
        private double longitude;
        private double latitude;

        public Builder taxiNumber(int taxiNumber) {
            this.taxiNumber = taxiNumber;
            return this;
        }

        public Builder timestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public TaxiPosition build() {
            return new TaxiPosition(taxiNumber, timestamp, longitude, latitude);
        }

    }

}
