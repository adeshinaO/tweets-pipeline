package co.adeshina.c19terma.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class TweetAggregate {

    private String term;

    @JsonProperty("countries_count")
    private Map<String, Integer> countriesCount;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Map<String, Integer> getCountriesCount() {
        return countriesCount;
    }

    public void setCountriesCount(Map<String, Integer> countriesCount) {
        this.countriesCount = countriesCount;
    }
}
