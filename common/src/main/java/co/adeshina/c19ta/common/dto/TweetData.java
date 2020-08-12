package co.adeshina.c19ta.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TweetData {

    private String term;

    @JsonProperty("has_one_thousand_followers")
    private boolean userHasOneThousandFollowers;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public boolean isUserHasOneThousandFollowers() {
        return userHasOneThousandFollowers;
    }

    public void setUserHasOneThousandFollowers(boolean userHasOneThousandFollowers) {
        this.userHasOneThousandFollowers = userHasOneThousandFollowers;
    }
}
