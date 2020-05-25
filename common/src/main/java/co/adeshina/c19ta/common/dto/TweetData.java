package co.adeshina.c19ta.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TweetData {

    private String term;

    @JsonProperty("is_verified_user")
    private boolean verifiedUser;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public boolean isVerifiedUser() {
        return verifiedUser;
    }

    public void setVerifiedUser(boolean verifiedUser) {
        this.verifiedUser = verifiedUser;
    }
}
