package co.adeshina.c19terma.common.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

// todo DELETE
public class JsonSerde<T extends JsonSerializable> implements Serde<T> {

    // todo: Remove this class and instead pass serdes directly to stream methods using helper functions
    //       e.g Produced.with(), Consumed.with(), Grouped.with() and Serdes.from()


    @Override
    public Serializer<T> serializer() {
        return new JsonSerializer<>();
    }

    @Override
    public Deserializer<T> deserializer() {
        return new JsonDeserializer<>();
    }
}
