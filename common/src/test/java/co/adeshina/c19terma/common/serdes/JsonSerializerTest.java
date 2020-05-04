package co.adeshina.c19terma.common.serdes;

import co.adeshina.c19terma.common.dto.TweetAggregate;
import co.adeshina.c19terma.common.dto.TweetCount;
import org.junit.jupiter.api.Test;

public class JsonSerializerTest {

    private JsonSerializer<TweetAggregate> aggregateJsonSerializer = new JsonSerializer<>();
    private JsonSerializer<TweetCount> countJsonSerializer = new JsonSerializer<>();

    @Test
    public void todoName() {


        // todo: serialize to JSON, then use Deser to convert it back and check...
    }

}
