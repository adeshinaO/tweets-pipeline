package co.adeshina.c19terma.common.serdes;

import co.adeshina.c19terma.common.dto.TweetAggregate;
import co.adeshina.c19terma.common.dto.TweetData;

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
        assertEquals(5, aggregate.getCountByAccountType().get(TweetAggregate.AccountType.VERIFIED));
        assertTrue(tweet.isVerifiedUser());
        assertEquals("COVID19", tweet.getTerm());
    }

    String tweetAggregateJson = "{\n"
            + "  \"term\": \"COVID19\",\n"
            + "  \"acct_type_count\": {\n"
            + "    \"verified_accounts\": 5,\n"
            + "    \"unverified_accounts\": 6\n"
            + "  }\n"
            + "}";

    String tweetDataJson = "{\n"
            + "  \"term\": \"COVID19\",\n"
            + "  \"is_verified_user\": \"true\"\n"
            + "}";
}
