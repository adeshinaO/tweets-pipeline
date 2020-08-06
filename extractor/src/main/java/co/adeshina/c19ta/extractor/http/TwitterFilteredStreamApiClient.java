package co.adeshina.c19ta.extractor.http;

import co.adeshina.c19ta.extractor.exception.ApiClientException;
import co.adeshina.c19ta.extractor.http.dto.TweetDto;
import java.util.function.Consumer;

public interface TwitterFilteredStreamApiClient {

    /**
     * Replaces any existing Filtered Stream rules.
     * @param rulesJson The new rules to use.
     * @throws ApiClientException If there is an API error.
     */
    void resetRules(String rulesJson) throws ApiClientException;

    /**
     * Open streaming HTTP connection to Filtered Stream API.
     * @param tweetConsumer A {@link Consumer<TweetDto>} that defines the logic to apply on each received tweet.
     * @throws ApiClientException Thrown if there is any error opening the stream.
     */
    void connect(Consumer<TweetDto> tweetConsumer) throws ApiClientException;
}
