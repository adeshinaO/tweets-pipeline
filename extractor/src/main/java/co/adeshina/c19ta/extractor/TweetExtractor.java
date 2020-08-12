package co.adeshina.c19ta.extractor;

import co.adeshina.c19ta.common.dto.TweetData;
import co.adeshina.c19ta.extractor.exception.ApiClientException;
import co.adeshina.c19ta.extractor.http.TwitterFilteredStreamApiClient;
import co.adeshina.c19ta.extractor.http.TwitterUserApiClient;
import co.adeshina.c19ta.extractor.http.dto.TweetDto;
import co.adeshina.c19ta.extractor.http.dto.UserDto;
import co.adeshina.c19ta.extractor.kafka.KafkaProducerService;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TweetExtractor {

    private Logger logger = LoggerFactory.getLogger(TweetExtractor.class);
    private static final int BENCHMARK_NUMBER_OF_FOLLOWERS = 1000;

    private TwitterFilteredStreamApiClient filteredStreamApiClient;
    private TwitterUserApiClient userApiClient;
    private KafkaProducerService<TweetData> kafkaProducerService;

    public TweetExtractor(
            TwitterFilteredStreamApiClient filteredStreamApiClient,
            TwitterUserApiClient userApiClient,
            KafkaProducerService<TweetData> kafkaProducerService) {
        this.filteredStreamApiClient = filteredStreamApiClient;
        this.userApiClient = userApiClient;
        this.kafkaProducerService = kafkaProducerService;
    }

    public void start() throws ApiClientException {
        filteredStreamApiClient.resetRules(STREAM_RULES);
        filteredStreamApiClient.connect(streamConsumer);
    }

    private int userApiRequestsCounter = 0;

    private Consumer<TweetDto> streamConsumer = (tweet) -> {

        try {
            UserDto userDto = userApiClient.findUser(tweet.getData().getAuthorId());
            userApiRequestsCounter++;

            Function<String, TweetData> mapper = (term) -> {
                TweetData tweetData = new TweetData();
                tweetData.setTerm(term);
                tweetData.setUserHasOneThousandFollowers(userDto.getFollowerCount() >= BENCHMARK_NUMBER_OF_FOLLOWERS);
                return tweetData;
            };

            List<String> termsInTweet = findTerms(tweet.getData().getText());
            termsInTweet.stream()
                        .map(mapper)
                        .forEach(data -> kafkaProducerService.send(data.getTerm(), data));

            logger.info("Sent data from one tweet to Kafka Producer Service");

            // Sleep to stay within Twitter API limits.
            if (userApiRequestsCounter == 25){
                logger.info("Thread will sleep for 15 seconds");
                userApiRequestsCounter = 0;
                Thread.sleep(6000);
            }

        } catch (ApiClientException e) {
            String msg = "Could not retrieve data for user with id: " + tweet.getData().getAuthorId();
            logger.error(msg, e);
        } catch (InterruptedException e) {
            logger.error("Sleep interrupted", e);
        }
    };

    // Checks the tweet for terms describing COVID-19.
    private static List<String> findTerms(String tweet) {
        String[] terms = {
                "New York",
                "London",
                "Paris",
                "Berlin",
                "Amsterdam"
        };

        return Arrays.stream(terms).filter(tweet::contains).collect(Collectors.toList());
    }

    private static final String STREAM_RULES = "{\n"
            + "  \"add\": [\n"
            + "    {\"value\":  \"\\\"New York\\\"\"},\n"
            + "    {\"value\":  \"\\\"London\\\"\"},\n"
            + "    {\"value\":  \"\\\"Paris\\\"\"},\n"
            + "    {\"value\":  \"\\\"Berlin\\\"\"},\n"
            + "    {\"value\":  \"\\\"Amsterdam\\\"\"}\n"
            + "  ]\n"
            + "}";
}
