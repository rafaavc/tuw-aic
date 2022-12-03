package aic.g3t1.common.taxiposition;

public class GeoLocation {

    private static final double RADIUS_EARTH = 6_371_000;

    private final double latitude;
    private final double longitude;

    public GeoLocation(double latitudeDegrees, double longitudeDegrees) {
        this.latitude = latitudeDegrees * Math.PI / 180;
        this.longitude = longitudeDegrees * Math.PI / 180;
    }

    public double latitude() {
        return latitude;
    }

    public double longitude() {
        return longitude;
    }

    public static double hav(double centralAngle) {
        double sine = Math.sin(centralAngle / 2);
        return sine * sine;
    }

    public static double distance(GeoLocation location1, GeoLocation location2) {
        double deltaLatitude = location2.latitude - location1.latitude;
        double sumLatitude = location1.latitude + location2.latitude;
        double deltaLongitude = location2.longitude - location1.longitude;
        double h = hav(deltaLatitude) + (1 - hav(-deltaLatitude) - hav(sumLatitude)) * hav(deltaLongitude);
        double radical = Math.sqrt(Math.min(h, 1));
        return 2 * RADIUS_EARTH * Math.asin(radical);
    }

}
