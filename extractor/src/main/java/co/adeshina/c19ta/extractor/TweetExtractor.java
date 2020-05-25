package co.adeshina.c19ta.extractor;

import co.adeshina.c19ta.common.util.PropertiesHelper;
import co.adeshina.c19ta.extractor.exception.ApiClientException;
import co.adeshina.c19ta.common.util.PropertiesInitFailedException;
import co.adeshina.c19ta.extractor.http.TwitterBearerTokenApiApiClientImpl;
import co.adeshina.c19ta.extractor.http.TwitterBearerTokenApiClient;
import co.adeshina.c19ta.extractor.http.TwitterFilteredStreamApiApiClientImpl;
import co.adeshina.c19ta.extractor.http.TwitterFilteredStreamApiClient;
import co.adeshina.c19ta.extractor.http.TwitterUserApiClientImpl;
import co.adeshina.c19ta.extractor.http.dto.UserDto;
import co.adeshina.c19ta.common.dto.TweetData;
import co.adeshina.c19ta.extractor.http.TwitterUserApiClient;
import co.adeshina.c19ta.extractor.kafka.KafkaProducerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import org.apache.kafka.clients.producer.ProducerConfig;

public class TweetExtractor {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public static void main(String[] args) throws PropertiesInitFailedException, ApiClientException {

        PropertiesHelper propertiesHelper = new PropertiesHelper("application.properties");
        Map<String, String> kafkaProps = propertiesHelper.kafkaProperties();
        Map<String, String> twitterProps = propertiesHelper.twitterProperties();

        Map<String, Object> kafkaConfig = new HashMap<>();
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, kafkaProps.get(PropertiesHelper.KAFKA_ACKS));
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.get(PropertiesHelper.KAFKA_BOOTSTRAP_SERVERS));
        kafkaProps.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProps.get(PropertiesHelper.KAFKA_EXTRACTOR_ID));

        KafkaProducerService kafkaService = new KafkaProducerService(kafkaConfig, kafkaProps.get(PropertiesHelper.KAFKA_INPUT_TOPIC));
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaService::close));

        String twitterConsumerSecret = twitterProps.get(PropertiesHelper.TWITTER_CONSUMER_SECRET);
        String twitterConsumerKey = twitterProps.get(PropertiesHelper.TWITTER_CONSUMER_KEY);

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

    private static final String STREAM_RULES = "{\n"
            + "  \"add\": [\n"
            + "    {\"value\":  \"\\\"SARS-CoV-2\\\"\"},\n"
            + "    {\"value\":  \"COVID19\"},\n"
            + "    {\"value\":  \"coronavirus\"},\n"
            + "    {\"value\":  \"\\\"Chinese Virus\\\"\"},\n"
            + "    {\"value\":  \"\\\"Wuhan Virus\\\"\"}\n"
            + "  ]\n"
            + "}";
}
