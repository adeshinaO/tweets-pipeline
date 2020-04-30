package co.adeshina.c19terma.common.dto;

import co.adeshina.c19terma.common.serdes.JsonSerializable;

public class TweetCount extends JsonSerializable { // todo: rename to simple Tweet

    private String term;

    // 2 letter ISO code.
    private String country;

    // todo: The extractor sends an instance of this for every tweet that meets the requirement for processing

}
