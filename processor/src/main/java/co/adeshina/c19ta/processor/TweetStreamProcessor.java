package co.adeshina.c19ta.processor;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.common.dto.TweetData;
import co.adeshina.c19ta.common.serdes.TweetAggregateSerde;
import co.adeshina.c19ta.common.serdes.TweetDataSerde;
import co.adeshina.c19ta.common.util.PropertiesHelper;
import co.adeshina.c19ta.common.util.PropertiesInitFailedException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

public class TweetStreamProcessor {

    public static void main(String[] args) throws PropertiesInitFailedException {

        Serde<TweetData> tweetDataSerde = Serdes.serdeFrom(new TweetDataSerde(), new TweetDataSerde());
        Serde<TweetAggregate> tweetAggSerde = Serdes.serdeFrom(new TweetAggregateSerde(), new TweetAggregateSerde());

        PropertiesHelper propertiesHelper = new PropertiesHelper("application.properties");
        Map<String, String> kafkaProps = propertiesHelper.kafkaProperties();

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaProps.get(PropertiesHelper.KAFKA_PROCESSOR_ID));
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.get(PropertiesHelper.KAFKA_BOOTSTRAP_SERVERS));

        StreamsBuilder builder = new StreamsBuilder();

        Consumed<String, TweetData> consumed = Consumed.with(Serdes.String(), tweetDataSerde);
        String inputTopic = kafkaProps.get(PropertiesHelper.KAFKA_INPUT_TOPIC);
        KStream<String, TweetData> source = builder.stream(inputTopic, consumed);

        KGroupedStream<String, TweetData> groupedStream = source.groupByKey();

        Initializer<TweetAggregate> initializer = () -> {
            Map<TweetAggregate.AccountType, Integer> count = new HashMap<>();
            count.put(TweetAggregate.AccountType.VERIFIED, 0);
            count.put(TweetAggregate.AccountType.UNVERIFIED, 0);

            TweetAggregate aggregate = new TweetAggregate();
            aggregate.setCountByAccountType(count);

            return aggregate;
        };

        Aggregator<String, TweetData, TweetAggregate> aggregator = ((key, tweet, aggregate) -> {

            Map<TweetAggregate.AccountType, Integer> countMap = aggregate.getCountByAccountType();

            if (tweet.isVerifiedUser()) {
                int oldCount = aggregate.getCountByAccountType().get(TweetAggregate.AccountType.VERIFIED);
                countMap.put(TweetAggregate.AccountType.VERIFIED, oldCount + 1);
            } else {
                int oldCount = aggregate.getCountByAccountType().get(TweetAggregate.AccountType.UNVERIFIED);
                countMap.put(TweetAggregate.AccountType.UNVERIFIED, oldCount + 1);
            }

            aggregate.setCountByAccountType(countMap);

            if (aggregate.getTerm() == null) {
                aggregate.setTerm(key);
            }

            return aggregate;
        });

        KTable<String, TweetAggregate> aggregateData = groupedStream.aggregate(initializer, aggregator);
        String outputTopic = kafkaProps.get(PropertiesHelper.KAFKA_OUTPUT_TOPIC);
        aggregateData.toStream().to(outputTopic, Produced.with(Serdes.String(), tweetAggSerde));

        Topology topology = builder.build();
        KafkaStreams streamProcessor = new KafkaStreams(topology, config);

        streamProcessor.setUncaughtExceptionHandler((thread, throwable) -> {
            // TODO: What to do here ?? See Java docs for this method.
        });

        streamProcessor.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streamProcessor::close));
    }
}
