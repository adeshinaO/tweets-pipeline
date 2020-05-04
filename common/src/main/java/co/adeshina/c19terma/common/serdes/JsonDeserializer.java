package co.adeshina.c19terma.common.serdes;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.Arrays;
import org.apache.kafka.common.serialization.Deserializer;

public class JsonDeserializer<T extends JsonSerializable> implements Deserializer<T> {

    private GsonBuilder builder = new GsonBuilder();

    {
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    }

    private final Gson gson = builder.create();

    @Override
    public T deserialize(String topic, byte[] data) {

        if (data == null) {
            return null;
        }

        return gson.fromJson(Arrays.toString(data), new TypeToken<T>(){}.getType());
    }
}
