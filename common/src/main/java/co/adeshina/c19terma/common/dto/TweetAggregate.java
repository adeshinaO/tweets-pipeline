package co.adeshina.c19terma.common.dto;

import co.adeshina.c19terma.common.serdes.JsonSerializable;
import java.time.ZonedDateTime;
import java.util.Map;

public class TweetAggregate extends JsonSerializable {

    // todo: this would be the value type in a aggregation operations.
    private String term;
    private Map<String, Integer> countByCountry;
    private ZonedDateTime time;
}
