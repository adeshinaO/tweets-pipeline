package co.adeshina.c19ta.extractor.http;

import co.adeshina.c19ta.extractor.exception.ApiClientException;
import co.adeshina.c19ta.extractor.http.dto.TweetDto;
import java.util.function.Consumer;

public interface TwitterFilteredStreamApiClient {

    /**
     *
     * @param rulesJson
     * @throws ApiClientException
     */
    void resetRules(String rulesJson) throws ApiClientException;

    /**
     *
     * @param tweetConsumer
     * @throws ApiClientException
     */
    void connect(Consumer<TweetDto> tweetConsumer) throws ApiClientException;

}
