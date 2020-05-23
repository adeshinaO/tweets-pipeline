package co.adeshina.c19ta.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        this.countByAccountType = countByAccountType;
    }

    public enum AccountType {

        @JsonProperty("verified_accounts")
        VERIFIED,

        @JsonProperty("unverified_accounts")
        UNVERIFIED
    }
}
