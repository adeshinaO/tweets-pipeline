package co.adeshina.c19terma.common.serdes;

import co.adeshina.c19terma.common.dto.TweetAggregate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

public class TweetAggregateSerde implements Deserializer<TweetAggregate>, Serializer<TweetAggregate> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public TweetAggregate deserialize(String topic, byte[] data) {

        if (data == null) {
            return null;
        }

        try {
            return mapper.readValue(new String(data), TweetAggregate.class);
        } catch (JsonProcessingException e) {
            throw new SerdeException("Failed to deserialize", e);
        }
    }

    @Override
    public byte[] serialize(String topic, TweetAggregate tweetAggregate) {

        if (tweetAggregate == null) {
            return null;
        }

        try {
            return mapper.writeValueAsBytes(tweetAggregate);
        } catch (JsonProcessingException e) {
            throw new SerdeException("Failed to serialize", e);
        }
    }

    @Override
    public void close() { }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) { }
}
