package co.adeshina.c19terma.tweetextractor;

import co.adeshina.c19terma.common.dto.TweetData;
import co.adeshina.c19terma.tweetextractor.http.dto.TweetDto;
import java.util.Map;
import java.util.function.Consumer;
import okhttp3.OkHttpClient;

public class TweetExtractor {

    // todo: pass to all API clients, so they reuse this object...
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public static void main(String[] args ) {

        // todo: Number 1: Set as rules...........
        String[] terms = {"COVID19", "Chinese Virus", "Corona Virus", "Sars-CoV-2"};


        Map<String, String> credentials = PropertiesHelper.twitterCredentials();

        Consumer<TweetDto> tweetDtoConsumer = (dto) -> {

            // todo convert dto to... TweetData

            // Todo: write this to Kafka..... in helper class.
            TweetData tweetData = new TweetData();


        };


    }
}
