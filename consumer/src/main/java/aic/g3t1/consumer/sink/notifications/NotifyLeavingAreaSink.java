package aic.g3t1.consumer.sink.notifications;

import aic.g3t1.common.environment.EnvironmentVariables;
import aic.g3t1.common.exceptions.MissingEnvironmentVariableException;
import aic.g3t1.common.model.notification.TaxiNotification;
import aic.g3t1.common.model.taxiposition.GeoLocation;
import aic.g3t1.common.model.taxiposition.TaxiPosition;
import org.apache.storm.tuple.Tuple;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static aic.g3t1.consumer.spout.TaxiPositionFields.*;

public class NotifyLeavingAreaSink extends NotifySink {
    private GeoLocation centerOfForbiddenCity;
    private Set<Integer> infractions;
    private final int predefinedAreaRadius;
    private final double latitude, longitude;

    public NotifyLeavingAreaSink() throws MissingEnvironmentVariableException {
        super();

        latitude = Double.parseDouble(EnvironmentVariables.getVariable("FORBIDDEN_CITY_LAT"));
        longitude = Double.parseDouble(EnvironmentVariables.getVariable("FORBIDDEN_CITY_LON"));

        predefinedAreaRadius = Integer.parseInt(EnvironmentVariables.getVariable("PREDEFINED_AREA_RADIUS"));
    }

    @Override
    protected void prepareAdditional() {
        centerOfForbiddenCity = new GeoLocation(latitude, longitude);
        infractions = new HashSet<>();
    }

    @Override
    public void execute(Tuple input) {
        TaxiPosition taxiPosition = TaxiPosition.builder()
                .taxiNumber(input.getIntegerByField(F_TAXI_NUMBER))
                .timestamp((Date) input.getValueByField(F_TIMESTAMP))
                .longitude(input.getDoubleByField(F_LONGITUDE))
                .latitude(input.getDoubleByField(F_LATITUDE))
                .build();

        int taxiNumber = taxiPosition.getTaxiNumber();
        double distance = GeoLocation.distance(centerOfForbiddenCity, taxiPosition.getLocation());
        boolean outsidePredefinedArea = distance / 1000 > predefinedAreaRadius;

        if (!outsidePredefinedArea) infractions.remove(taxiNumber);
        else if (!infractions.contains(taxiNumber)) {
            // only notify if this is the first time the infraction is detected
            infractions.add(taxiNumber);
            var notification = new TaxiNotification(TaxiNotification.Type.LEAVING_PREDEFINED_AREA, new Date(), taxiNumber);
            sendNotification(notification);
        }

        collector.ack(input);
    }
}
