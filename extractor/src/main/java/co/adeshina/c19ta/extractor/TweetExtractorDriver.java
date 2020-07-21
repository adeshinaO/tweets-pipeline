package co.adeshina.c19ta.extractor;

import co.adeshina.c19ta.common.util.PropertiesHelper;
import co.adeshina.c19ta.extractor.exception.ApiClientException;
import co.adeshina.c19ta.common.util.PropertiesInitFailedException;
import co.adeshina.c19ta.extractor.http.TwitterBearerTokenApiClientImpl;
import co.adeshina.c19ta.extractor.http.TwitterBearerTokenApiClient;
import co.adeshina.c19ta.extractor.http.TwitterFilteredStreamApiClientImpl;
import co.adeshina.c19ta.extractor.http.TwitterFilteredStreamApiClient;
import co.adeshina.c19ta.extractor.http.TwitterUserApiClientImpl;
import co.adeshina.c19ta.common.dto.TweetData;
import co.adeshina.c19ta.extractor.http.TwitterUserApiClient;
import co.adeshina.c19ta.extractor.kafka.KafkaProducerService;
import co.adeshina.c19ta.extractor.kafka.KafkaProducerServiceImpl;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

import org.apache.kafka.clients.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TweetExtractorDriver {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    private static Logger logger = LoggerFactory.getLogger(TweetExtractorDriver.class);

    public static void main(String[] args) throws PropertiesInitFailedException, ApiClientException {

        PropertiesHelper propertiesHelper = new PropertiesHelper("common.properties");
        Map<String, String> kafkaProps = propertiesHelper.kafkaProperties();
        Map<String, String> twitterProps = propertiesHelper.twitterProperties();

        Map<String, Object> kafkaConfig = new HashMap<>();
        kafkaConfig.put(ProducerConfig.ACKS_CONFIG, kafkaProps.get(PropertiesHelper.KAFKA_ACKS));

        kafkaConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.get(PropertiesHelper.KAFKA_BOOTSTRAP_SERVERS));
        kafkaConfig.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProps.get(PropertiesHelper.KAFKA_EXTRACTOR_ID));

        KafkaProducerService<TweetData> kafkaService =
                new KafkaProducerServiceImpl(kafkaConfig, kafkaProps.get(PropertiesHelper.KAFKA_INPUT_TOPIC));

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaService::close));

        String twitterConsumerSecret = twitterProps.get(PropertiesHelper.TWITTER_CONSUMER_SECRET);
        String twitterConsumerKey = twitterProps.get(PropertiesHelper.TWITTER_CONSUMER_KEY);

        TwitterBearerTokenApiClient bearerTokenApiClient =
                new TwitterBearerTokenApiClientImpl(twitterConsumerKey, twitterConsumerSecret, HTTP_CLIENT);

        TwitterFilteredStreamApiClient streamApiClient =
                new TwitterFilteredStreamApiClientImpl(bearerTokenApiClient, HTTP_CLIENT);

        TwitterUserApiClient userApiClient = new TwitterUserApiClientImpl(bearerTokenApiClient, HTTP_CLIENT);

        TweetExtractor extractor = new TweetExtractor(streamApiClient, userApiClient, kafkaService);
        extractor.start();
        logger.info("Extractor config complete. Starting Extractor");
    }
}
