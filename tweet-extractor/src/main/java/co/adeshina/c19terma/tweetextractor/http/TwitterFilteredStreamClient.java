package co.adeshina.c19terma.tweetextractor.http;

import java.util.List;
import java.util.function.Consumer;

public interface TwitterFilteredStreamClient {

    // cleanup any old rules and replace with 'rules'
    void resetRules(List<String> rules);

    // todo: use consumer to process tweets
    void connect(Consumer<TweetDto> tweetConsumer);

}
