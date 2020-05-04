package co.adeshina.c19terma.common.dto;

public class TweetData {

    private String term;

    // 2-letter ISO code.
    private String country;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
