package aic.g3t1.common.model.taxiposition;

public class GeoLocation {

    /**
     * The earth's radius in meters.
     */
    private static final double RADIUS_EARTH = 6_371_000;

    private final double latitude;
    private final double longitude;

    public GeoLocation(double latitudeDegrees, double longitudeDegrees) {
        this.latitude = latitudeDegrees * Math.PI / 180;
        this.longitude = longitudeDegrees * Math.PI / 180;
    }

    public static double hav(double centralAngle) {
        double sine = Math.sin(centralAngle / 2);
        return sine * sine;
    }

    /**
     * Returns the distance between to GeoLocations in meters.
     * @param location1 the first location
     * @param location2 the second location
     * @return The spherical distance in meters.
     */
    public static double distance(GeoLocation location1, GeoLocation location2) {
        double deltaLatitude = location2.latitude - location1.latitude;
        double sumLatitude = location1.latitude + location2.latitude;
        double deltaLongitude = location2.longitude - location1.longitude;
        double h = hav(deltaLatitude) + (1 - hav(-deltaLatitude) - hav(sumLatitude)) * hav(deltaLongitude);
        double radical = Math.sqrt(clamp(h, 0, 1));
        return 2 * RADIUS_EARTH * Math.asin(radical);
    }

    public double latitude() {
        return latitude;
    }

    public double longitude() {
        return longitude;
    }

    private static double clamp(double value, double lowerBound, double upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Lower bound must be less than or equal to upper bound");
        }
        return Math.min(Math.max(value, lowerBound), upperBound);
    }

    @Override
    public String toString() {
        return "(" + (latitude * 180 / Math.PI) + " | " + (longitude * 180 / Math.PI) + ")";
    }

}
