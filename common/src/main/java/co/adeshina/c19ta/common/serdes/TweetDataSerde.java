package co.adeshina.c19ta.common.serdes;

import co.adeshina.c19ta.common.dto.TweetData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

public class TweetDataSerde implements Deserializer<TweetData>, Serializer<TweetData> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TweetData deserialize(String topic, byte[] data) {

        if (data == null) {
            return null;
        }

        try {
            return objectMapper.readValue(new String(data), TweetData.class);
        } catch (JsonProcessingException e) {
            throw new SerdeException("Failed to deserialize", e);
        }
    }

    @Override
    public byte[] serialize(String topic, TweetData tweetData) {

        if (tweetData == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsBytes(tweetData);
        } catch (JsonProcessingException e) {
            throw new SerdeException("Failed to serialize", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) { }

    @Override
    public void close() { }
}
