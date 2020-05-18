package co.adeshina.c19terma.tweetextractor.http;

import co.adeshina.c19terma.tweetextractor.exception.ApiClientException;
import co.adeshina.c19terma.tweetextractor.http.dto.TweetDto;
import java.util.List;
import java.util.function.Consumer;

public interface TwitterFilteredStreamApiClient {

    // todo: cleanup any old rules and replace with 'rules'
    void resetRules(String rulesJson) throws ApiClientException;

    // todo: use consumer to process tweets
    void connect(Consumer<TweetDto> tweetConsumer) throws ApiClientException;

}
