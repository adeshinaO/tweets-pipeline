package co.adeshina.c19ta.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class TweetAggregate {

    private String term;

    @JsonProperty("acct_type_count")
    private Map<AccountType, Integer> countByAccountType;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Map<AccountType, Integer> getCountByAccountType() {
        return countByAccountType;
    }

    public void setCountByAccountType(Map<AccountType, Integer> countByAccountType) {
        this.countByAccountType = new HashMap<>(countByAccountType);
    }

    public enum AccountType {

        @JsonProperty("one_thousand_followers")
        ONE_THOUSAND_FOLLOWERS,

        @JsonProperty("less_than_one_thousand_followers")
        LESS_THAN_ONE_THOUSAND_FOLLOWERS
    }
}
