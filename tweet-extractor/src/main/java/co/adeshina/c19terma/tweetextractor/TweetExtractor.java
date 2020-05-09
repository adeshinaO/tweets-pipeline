package co.adeshina.c19terma.tweetextractor;

import co.adeshina.c19terma.common.dto.TweetData;
import co.adeshina.c19terma.tweetextractor.http.TweetDto;
import java.util.function.Consumer;

public class TweetExtractor {
    public static void main(String[] args ) {

        String[] terms = {"COVID19", "Chinese Virus", "Corona Virus", "Sars-CoV-2"};


        Consumer<TweetDto> tweetDtoConsumer = (dto) -> {

            // todo convert dto to... TweetData

            // Todo: write this to Kafka..... in helper class.
            TweetData tweetData = new TweetData();

        };


    }
}
