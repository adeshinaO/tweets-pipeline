package co.adeshina.c19terma.tweetextractor;

import co.adeshina.c19terma.common.dto.TweetData;
import co.adeshina.c19terma.tweetextractor.http.dto.TweetDto;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import okhttp3.OkHttpClient;
import org.apache.kafka.clients.producer.ProducerConfig;

public class TweetExtractor {

    // todo: pass to all API clients, so they reuse this object...
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    // todo: put the JSON here -- see guide on how to write the JSon.. see json file
    public static final String FILTERED_STREAM_RULES = "";

    public static void main(String[] args) {

        //

        PropertiesHelper propertiesHelper = new PropertiesHelper("application.properties");

        Map<String, String> credentials = propertiesHelper.twitterCredentials();

        Map<String, String> kafkaProps = propertiesHelper.kafkaConfig();

        Map<String, Object> kafkaConfig = new HashMap<>();
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, kafkaProps.get("acks"));
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.get("bootstrap_servers"));
        kafkaProps.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProps.get("client_id"));

        Consumer<TweetDto> tweetDtoConsumer = (dto) -> {

            // todo convert dto to... TweetData

            // Todo: write this to Kafka....
            TweetData tweetData = new TweetData();
        };

        Runnable kafkaProducerShutdown = () -> {
            // todo: call producer service #close()
        };

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaProducerShutdown));
    }
}
