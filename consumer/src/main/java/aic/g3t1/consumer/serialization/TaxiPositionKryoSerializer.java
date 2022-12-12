package aic.g3t1.consumer.serialization;

import aic.g3t1.common.taxiposition.TaxiPosition;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;

public class TaxiPositionKryoSerializer extends Serializer<TaxiPosition> {

    @Override
    public void write(Kryo kryo, Output output, TaxiPosition object) {
        if (object == null) {
            output.write(new byte[0]);
        }

        try {
            ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(arrayStream);
            objectStream.writeObject(object);
            byte[] byteArray = arrayStream.toByteArray();
            arrayStream.close();
            objectStream.close();
            output.write(byteArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TaxiPosition read(Kryo kryo, Input input, Class<TaxiPosition> type) {
        try {
            ByteArrayInputStream arrayStream = new ByteArrayInputStream(input.readAllBytes());
            ObjectInputStream objectStream = new ObjectInputStream(arrayStream);
            return (TaxiPosition) objectStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
