package co.adeshina.c19terma.common.serdes;

import co.adeshina.c19terma.common.dto.TweetAggregate;
import co.adeshina.c19terma.common.dto.TweetData;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerdeTest {

    private TweetDataSerde tweetJsonDeserializer = new TweetDataSerde();
    private TweetAggregateSerde aggregateJsonDeserializer = new TweetAggregateSerde();

    @Test
    public void shouldDeserialize() {
        TweetAggregate aggregate = aggregateJsonDeserializer.deserialize(null, tweetAggregateJson.getBytes());
        TweetData tweet = tweetJsonDeserializer.deserialize(null, tweetDataJson.getBytes());

        assertEquals("COVID19", aggregate.getTerm());
        assertEquals(6, aggregate.getCountriesCount().get("GB"));
        assertEquals("FR", tweet.getCountry());
        assertEquals("COVID19", tweet.getTerm());
    }

    String tweetAggregateJson = "{\n"
            + "  \"term\": \"COVID19\",\n"
            + "  \"countries_count\": {\n"
            + "    \"US\": 5,\n"
            + "    \"GB\": 6,\n"
            + "    \"FR\": 5\n"
            + "  }\n"
            + "}";

    String tweetDataJson = "{\n"
            + "  \"term\": \"COVID19\",\n"
            + "  \"country\": \"FR\"\n"
            + "}";
}
