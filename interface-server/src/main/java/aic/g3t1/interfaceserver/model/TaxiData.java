package aic.g3t1.interfaceserver.model;

import java.io.Serializable;
import java.util.*;

public class TaxiData implements Serializable {
    public final List<IndividualTaxiData> taxis;
    public final int amountOfTaxis;
    public final double totalDistance;

    private TaxiData(List<IndividualTaxiData> taxis) {
        this.taxis = taxis;
        this.amountOfTaxis = taxis.size();
        this.totalDistance = taxis.stream()
                .map(IndividualTaxiData::getDistance)
                .reduce(0., Double::sum);
    }

    private static class Location {
        public final double latitude;
        public final double longitude;
        
        public Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private static class IndividualTaxiData implements Serializable {
        public final int number;
        public final Location location;
        public final Double distance, averageSpeed;
        public IndividualTaxiData(int number, Location location, Double distance, Double averageSpeed) {
            this.number = number;
            this.location = location;
            this.distance = distance;
            this.averageSpeed = averageSpeed;
        }

        public double getDistance() {
            return distance == null ? 0. : distance;
        }
    }

    public static class Builder {
        private Map<Integer, Double> distances;
        private Map<Integer, Double> averageSpeeds;
        private Map<Integer, Location> locations;

        public Map<Integer, Double> parseMap(Map<String, String> map) {
            var result = new HashMap<Integer, Double>();
            for (var entry : map.entrySet())
                result.put(Integer.parseInt(entry.getKey()), Double.parseDouble(entry.getValue()));
            return result;
        }

        public Builder distances(Map<String, String> distances) {
            this.distances = parseMap(distances);
            return this;
        }

        public Builder averageSpeeds(Map<String, String> averageSpeeds) {
            this.averageSpeeds = parseMap(averageSpeeds);
            return this;
        }

        public Builder locations(Map<String, String> locations) {
            var result = new HashMap<Integer, Location>();
            for (var entry : locations.entrySet()) {
                var coords = entry.getValue().split(", ");
                var location = new Location(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
                result.put(Integer.parseInt(entry.getKey()), location);
            }
            this.locations = result;
            return this;
        }

        public TaxiData build() {
            var taxis = new ArrayList<IndividualTaxiData>();

            for (int taxiNumber : locations.keySet()) {
                taxis.add(new IndividualTaxiData(
                    taxiNumber,
                    locations.get(taxiNumber),
                    distances.get(taxiNumber),
                    averageSpeeds.get(taxiNumber)
                ));
            }

            return new TaxiData(taxis);
        }
    }
}
