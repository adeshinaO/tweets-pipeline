package co.adeshina.c19ta.data_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataPacket {

    private List<Data> data = new ArrayList<>();

    @JsonProperty("thousand_followers_total")
    private int totalThousandFollowers;

    @JsonProperty("less_than_thousand_followers_total")
    private int totalLessThanThousandFollowers;

    @JsonProperty("build_time")
    private ZonedDateTime buildTime;

    public List<Data> getData() {
        return new ArrayList<>(data);
    }

    public void setData(List<Data> data) {
        this.data.addAll(data);
    }

    public int getTotalLessThanThousandFollowers() {
        return totalLessThanThousandFollowers;
    }

    public int getTotalThousandFollowers() {
        return totalThousandFollowers;
    }

    public void setTotalThousandFollowers(int totalThousandFollowers) {
        this.totalThousandFollowers = totalThousandFollowers;
    }

    public ZonedDateTime getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(ZonedDateTime buildTime) {
        this.buildTime = buildTime;
    }

    public void setTotalLessThanThousandFollowers(int totalLessThanThousandFollowers) {
        this.totalLessThanThousandFollowers = totalLessThanThousandFollowers;
    }

    public static class Data {

        private String term;

        // Percentage of tweets from users with at least a thousand followers
        @JsonProperty("thousand_followers_percentage")
        private double percentageThousandFollowers;

        // less than 1k followers.
        @JsonProperty("less_than_thousand_followers_percentage")
        private double percentageLessThanThousandFollowers;

        public double getPercentageLessThanThousandFollowers() {
            return percentageLessThanThousandFollowers;
        }

        public void setPercentageLessThanThousandFollowers(double percentageLessThanThousandFollowers) {
            this.percentageLessThanThousandFollowers = percentageLessThanThousandFollowers;
        }

        public double getPercentageThousandFollowers() {
            return percentageThousandFollowers;
        }

        public void setPercentageThousandFollowers(double percentageThousandFollowers) {
            this.percentageThousandFollowers = percentageThousandFollowers;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }
    }
}
