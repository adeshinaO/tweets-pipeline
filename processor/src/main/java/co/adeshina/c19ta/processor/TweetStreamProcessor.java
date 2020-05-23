package co.adeshina.c19ta.processor;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.common.dto.TweetData;
import co.adeshina.c19ta.common.util.PropertiesHelper;
import co.adeshina.c19ta.common.util.PropertiesInitFailedException;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KGroupedTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

public class TweetStreamProcessor {

    public static void main( String[] args ) throws PropertiesInitFailedException {

        PropertiesHelper propertiesHelper = new PropertiesHelper("application.properties");
        Map<String, String> kafkaProps = propertiesHelper.kafkaProperties();

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaProps.get(PropertiesHelper.KAFKA_PROCESSOR_ID));
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.get(PropertiesHelper.KAFKA_BOOTSTRAP_SERVERS));

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, TweetData> source = builder.stream(kafkaProps.get(PropertiesHelper.KAFKA_INPUT_TOPIC));
        // TODO: Build on this.. groupByKey and then aggregate...
        KGroupedStream<String, TweetData> groupedStream = source.groupByKey();
        // TODO: Fix Aggregate building.... HOW is the data represented as a KTable, see docs
        KTable<String, TweetAggregate> aggregateData = groupedStream.aggregate(null, null);
        // TODO: Figure out the data transformations on paper
        KGroupedTable<String, TweetAggregate> groupedAgg = aggregateData.groupBy(null); // TODO,


        Topology topology = builder.build();
        KafkaStreams streamProcessor = new KafkaStreams(topology, config);

        streamProcessor.setUncaughtExceptionHandler((thread, throwable) -> {
            // TODO: What to do here ?? See Java docs for this method.
        });

        streamProcessor.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streamProcessor::close));
    }
}
