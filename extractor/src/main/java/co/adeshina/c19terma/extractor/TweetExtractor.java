package co.adeshina.c19terma.extractor;

import co.adeshina.c19terma.common.dto.TweetData;
import co.adeshina.c19terma.extractor.exception.ApiClientException;
import co.adeshina.c19terma.extractor.exception.ConfigPropertiesInitException;
import co.adeshina.c19terma.extractor.http.TwitterBearerTokenApiApiClientImpl;
import co.adeshina.c19terma.extractor.http.TwitterBearerTokenApiClient;
import co.adeshina.c19terma.extractor.http.TwitterFilteredStreamApiApiClientImpl;
import co.adeshina.c19terma.extractor.http.TwitterFilteredStreamApiClient;
import co.adeshina.c19terma.extractor.http.TwitterUserApiClient;
import co.adeshina.c19terma.extractor.http.TwitterUserApiClientImpl;

import co.adeshina.c19terma.extractor.http.dto.UserDto;
import co.adeshina.c19terma.extractor.kafka.KafkaProducerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import org.apache.kafka.clients.producer.ProducerConfig;

public class TweetExtractor {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public static void main(String[] args) throws ConfigPropertiesInitException, ApiClientException {

        PropertiesHelper propertiesHelper = new PropertiesHelper("application.properties");
        Map<String, String> kafkaProps = propertiesHelper.kafkaProperties();
        Map<String, String> twitterProps = propertiesHelper.twitterProperties();

        Map<String, Object> kafkaConfig = new HashMap<>();
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, kafkaProps.get("kafka.acks"));
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.get("kafka.bootstrap.servers"));
        kafkaProps.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProps.get("kafka.client.id"));

        KafkaProducerService kafkaService = new KafkaProducerService(kafkaConfig, kafkaProps.get("kafka.topic"));
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaService::close));

        String twitterConsumerSecret = twitterProps.get("twitter.consumer.secret");
        String twitterConsumerKey = twitterProps.get("twitter.consumer.key");

        TwitterBearerTokenApiClient bearerTokenApiClient =
                new TwitterBearerTokenApiApiClientImpl(twitterConsumerKey, twitterConsumerSecret, HTTP_CLIENT);

        TwitterFilteredStreamApiClient streamApiClient =
                new TwitterFilteredStreamApiApiClientImpl(bearerTokenApiClient, HTTP_CLIENT);

        TwitterUserApiClient userApiClient = new TwitterUserApiClientImpl(bearerTokenApiClient, HTTP_CLIENT);

        streamApiClient.resetRules(STREAM_RULES);

        streamApiClient.connect(tweetDto -> {

            String authorId = tweetDto.getAuthorId();
            List<String> terms = findTerms(tweetDto.getText());

            try {
                UserDto userDto = userApiClient.findUser(authorId);
                TweetData data;
                for (String term: terms) {
                    data = new TweetData();
                    data.setTerm(term);
                    data.setVerifiedUser(userDto.isVerified());
                    kafkaService.send(term, data);
                }

            } catch (ApiClientException e) {
                // todo: logger - will skip this DTO
            }
        });
    }

    private static List<String> findTerms(String tweet) {

        String[] terms = {"Chinese Virus", "SARS-CoV-2", "Wuhan Virus", "coronavirus"};
        List<String> result = new ArrayList<>();

        for (String term: terms) {
            if (tweet.contains(term)) {
                result.add(term);
            }
        }

        return result;
    }

    public static final String STREAM_RULES = "{\n"
            + "  \"add\": [\n"
            + "    {\"value\":  \"\\\"SARS-CoV-2\\\"\"},\n"
            + "    {\"value\":  \"COVID19\"},\n"
            + "    {\"value\":  \"coronavirus\"},\n"
            + "    {\"value\":  \"\\\"Chinese Virus\\\"\"},\n"
            + "    {\"value\":  \"\\\"Wuhan Virus\\\"\"}\n"
            + "  ]\n"
            + "}";
}
