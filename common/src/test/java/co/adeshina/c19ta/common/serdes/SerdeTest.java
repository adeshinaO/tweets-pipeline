package co.adeshina.c19ta.common.serdes;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.common.dto.TweetData;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SerdeTest {

    private TweetDataSerde tweetJsonDeserializer = new TweetDataSerde();
    private TweetAggregateSerde aggregateJsonDeserializer = new TweetAggregateSerde();

    @Test
    public void shouldDeserialize() {
        TweetAggregate aggregate = aggregateJsonDeserializer.deserialize(null, tweetAggregateJson.getBytes());
        TweetData tweet = tweetJsonDeserializer.deserialize(null, tweetDataJson.getBytes());

        assertEquals("COVID19", aggregate.getTerm());
        assertEquals(5, aggregate.getCountByAccountType().get(TweetAggregate.AccountType.ONE_THOUSAND_FOLLOWERS));
        assertTrue(tweet.isUserHasOneThousandFollowers());
        assertEquals("COVID19", tweet.getTerm());
    }

    String tweetAggregateJson = "{\n"
            + "  \"term\": \"COVID19\",\n"
            + "  \"acct_type_count\": {\n"
            + "    \"one_thousand_followers\": 5,\n"
            + "    \"less_than_one_thousand_followers\": 6\n"
            + "  }\n"
            + "}";

    String tweetDataJson = "{\n"
            + "  \"term\": \"COVID19\",\n"
            + "  \"has_one_thousand_followers\": \"true\"\n"
            + "}";
}
