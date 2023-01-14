package aic.g3t1.common.model.taxiposition;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TaxiPositionSerializer implements Serializer {
    @Override
    public byte[] serialize(String topic, Object data) {
        if (data == null) {
            return new byte[0];
        }

        try {
            ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(arrayStream);
            objectStream.writeObject(data);
            byte[] byteArray = arrayStream.toByteArray();
            arrayStream.close();
            objectStream.close();
            return byteArray;
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }
}
