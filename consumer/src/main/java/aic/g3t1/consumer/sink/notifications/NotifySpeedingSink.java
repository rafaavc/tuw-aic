package aic.g3t1.consumer.sink.notifications;

import aic.g3t1.common.environment.EnvironmentVariables;
import aic.g3t1.common.exceptions.MissingEnvironmentVariableException;
import aic.g3t1.common.model.notification.TaxiNotification;
import org.apache.storm.tuple.Tuple;

import java.util.Date;

import static aic.g3t1.consumer.bolt.TaxiSpeedFields.F_TAXI_SPEED;
import static aic.g3t1.consumer.spout.TaxiPositionFields.F_TAXI_NUMBER;

public class NotifySpeedingSink extends NotifySink {
    int predefinedSpeedLimit;

    public NotifySpeedingSink() throws MissingEnvironmentVariableException {
        super();
        predefinedSpeedLimit = Integer.parseInt(EnvironmentVariables.getVariable("PREDEFINED_SPEED_LIMIT"));
    }

    @Override
    public void execute(Tuple input) {
        double speed = input.getDoubleByField(F_TAXI_SPEED);
        if (speed > predefinedSpeedLimit) {
            int taxiNumber = input.getIntegerByField(F_TAXI_NUMBER);
            var notification = new TaxiNotification(TaxiNotification.Type.SPEEDING, new Date(), taxiNumber);
            sendNotification(notification);
        }
        collector.ack(input);
    }
}
