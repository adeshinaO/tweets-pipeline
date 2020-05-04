package co.adeshina.c19terma.common.serdes;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.kafka.common.serialization.Serializer;

public class JsonSerializer<T extends JsonSerializable> implements Serializer<T> {

    private GsonBuilder builder = new GsonBuilder();

    {
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    }

    private final Gson gson = builder.create();

    @Override
    public byte[] serialize(String topic, T data) {

        if (data == null) {
            return null;
        }

        return gson.toJson(data).getBytes();
    }
}
