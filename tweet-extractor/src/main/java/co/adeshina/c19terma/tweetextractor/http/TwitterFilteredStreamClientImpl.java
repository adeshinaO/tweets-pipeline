package co.adeshina.c19terma.tweetextractor.http;

import co.adeshina.c19terma.tweetextractor.TweetExtractor;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;

public class TwitterFilteredStreamClientImpl implements TwitterFilteredStreamClient {

    private final String RULES_URL = "";
    private final String STREAM_URL = "";

    private final TwitterOAuth2BearerTokenClient bearerTokenClient;

    public TwitterFilteredStreamClientImpl(String consumerSecret, String consumerKey) {
        bearerTokenClient = new TwitterOAuth2BearerTokenClient(consumerKey, consumerSecret);
    }

    @Override
    public void resetRules(List<String> rules) {

    }

    @Override
    public void connect(Consumer<TweetDto> tweetConsumer) {

        // todo: for each tweetDto in stream, pass them to accept..
        tweetConsumer.accept(null);
    }

    // todo: use OkHttp -- But not response.asString() since that closes the stream..

    // Todo: Summary; To work with the Filtered Stream endpoint
    //      1. Exchange client credentials for Bearer token and use it to auth all future requests
    //      2. Setup rules for stream -- these decide what tweets are sent
    //      3. Connect to the streaming endpoint and process tweets.

    // todo: remember to delete any existing rules before setting up new ones. This ensures that rules
    //      used for testing are cleaned up.

}
