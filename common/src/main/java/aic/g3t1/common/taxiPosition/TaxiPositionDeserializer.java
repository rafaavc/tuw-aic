package aic.g3t1.common.taxiPosition;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class TaxiPositionDeserializer implements Deserializer {
    @Override
    public TaxiPosition deserialize(String topic, byte[] data) {
        try {
            ByteArrayInputStream arrayStream = new ByteArrayInputStream(data);
            ObjectInputStream objectStream = new ObjectInputStream(arrayStream);
            return (TaxiPosition) objectStream.readObject();
        } catch (Exception ex) {
            throw new SerializationException(ex.getMessage());
        }
    }
}
