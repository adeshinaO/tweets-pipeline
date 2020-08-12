package co.adeshina.c19ta.processor;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.common.dto.TweetData;
import co.adeshina.c19ta.common.serdes.TweetAggregateSerde;
import co.adeshina.c19ta.common.serdes.TweetDataSerde;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamProcessorHelper {

    private static Logger logger = LoggerFactory.getLogger(StreamProcessor.class);

    protected static final Serde<TweetData> TWEET_DATA_SERDE =
            Serdes.serdeFrom(new TweetDataSerde(), new TweetDataSerde());

    protected static final Serde<TweetAggregate> TWEET_AGGREGATE_SERDE =
            Serdes.serdeFrom(new TweetAggregateSerde(), new TweetAggregateSerde());

    protected static final Initializer<TweetAggregate> INITIALIZER = () -> {
        Map<TweetAggregate.AccountType, Integer> count = new HashMap<>();
        count.put(TweetAggregate.AccountType.ONE_THOUSAND_FOLLOWERS, 0);
        count.put(TweetAggregate.AccountType.LESS_THAN_ONE_THOUSAND_FOLLOWERS, 0);

        TweetAggregate aggregate = new TweetAggregate();
        aggregate.setCountByAccountType(count);

        return aggregate;
    };

    protected static final Aggregator<String, TweetData, TweetAggregate> AGGREGATOR = ((key, tweet, aggregate) -> {

        Map<TweetAggregate.AccountType, Integer> countMap = aggregate.getCountByAccountType();

        if (tweet.isUserHasOneThousandFollowers()) {
            int oldCount = aggregate.getCountByAccountType().get(TweetAggregate.AccountType.ONE_THOUSAND_FOLLOWERS);
            countMap.put(TweetAggregate.AccountType.ONE_THOUSAND_FOLLOWERS, oldCount + 1);
        } else {
            int oldCount = aggregate.getCountByAccountType().get(TweetAggregate.AccountType.LESS_THAN_ONE_THOUSAND_FOLLOWERS);
            countMap.put(TweetAggregate.AccountType.LESS_THAN_ONE_THOUSAND_FOLLOWERS, oldCount + 1);
        }

        logger.info("Merged one tweet data into aggregate for: " + key);
        aggregate.setCountByAccountType(countMap);

        if (aggregate.getTerm() == null) {
            aggregate.setTerm(key);
        }

        return aggregate;
    });
}
