package co.adeshina.c19ta.processor;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.common.dto.TweetData;
import co.adeshina.c19ta.common.util.PropertiesHelper;
import co.adeshina.c19ta.common.util.PropertiesInitFailedException;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamProcessor {

    private static Logger logger = LoggerFactory.getLogger(StreamProcessor.class);

    public static void main(String[] args) throws PropertiesInitFailedException {

        PropertiesHelper propertiesHelper = new PropertiesHelper("common.properties");
        Map<String, String> kafkaProps = propertiesHelper.kafkaProperties();

        StreamsBuilder builder = new StreamsBuilder();
        Consumed<String, TweetData> consumedWith = Consumed.with(Serdes.String(), StreamProcessorHelper.TWEET_DATA_SERDE);
        String inputTopic = kafkaProps.get(PropertiesHelper.KAFKA_INPUT_TOPIC);

        KStream<String, TweetData> source = builder.stream(inputTopic, consumedWith);
        KGroupedStream<String, TweetData> groupedStream = source.groupByKey();

        KTable<String, TweetAggregate> aggregateData =
                groupedStream.aggregate(StreamProcessorHelper.INITIALIZER, StreamProcessorHelper.AGGREGATOR,
                        Materialized.with(Serdes.String(), StreamProcessorHelper.TWEET_AGGREGATE_SERDE));

        String outputTopic = kafkaProps.get(PropertiesHelper.KAFKA_OUTPUT_TOPIC);
        aggregateData.toStream()
                     .to(outputTopic, Produced.with(Serdes.String(), StreamProcessorHelper.TWEET_AGGREGATE_SERDE));

        Topology topology = builder.build();

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaProps.get(PropertiesHelper.KAFKA_PROCESSOR_ID));
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.get(PropertiesHelper.KAFKA_BOOTSTRAP_SERVERS));

        KafkaStreams streamProcessor = new KafkaStreams(topology, config);

        streamProcessor.setUncaughtExceptionHandler((thread, throwable) -> {
            logger.error(throwable.getMessage());
        });

        streamProcessor.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streamProcessor::close));
    }
}
