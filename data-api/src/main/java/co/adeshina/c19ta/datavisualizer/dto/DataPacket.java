package co.adeshina.c19ta.datavisualizer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataPacket {

    private Set<Data> data = new HashSet<>();
    private int totalTweetsUnverifiedUsers;
    private int totalTweetsVerifiedUsers;
    private ZonedDateTime buildTime;

    public Set<Data> getData() {
        return new HashSet<>(data);
    }

    public void setData(Set<Data> data) {
        this.data.addAll(data);
    }

    public int getTotalTweetsUnverifiedUsers() {
        return totalTweetsUnverifiedUsers;
    }

    public int getTotalTweetsVerifiedUsers() {
        return totalTweetsVerifiedUsers;
    }

    public void setTotalTweetsVerifiedUsers(int totalTweetsVerifiedUsers) {
        this.totalTweetsVerifiedUsers = totalTweetsVerifiedUsers;
    }

    public ZonedDateTime getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(ZonedDateTime buildTime) {
        this.buildTime = buildTime;
    }

    public void setTotalTweetsUnverifiedUsers(int totalTweetsUnverifiedUsers) {
        this.totalTweetsUnverifiedUsers = totalTweetsUnverifiedUsers;
    }

    public static class Data {

        private String term;

        @JsonProperty("verified_users_percentage")
        private double percentageTweetsByVerifiedUsers;

        @JsonProperty("unverified_users_percentage")
        private double percentageTweetsByUnverifiedUsers;

        public double getPercentageTweetsByUnverifiedUsers() {
            return percentageTweetsByUnverifiedUsers;
        }

        public void setPercentageTweetsByUnverifiedUsers(double percentageTweetsByUnverifiedUsers) {
            this.percentageTweetsByUnverifiedUsers = percentageTweetsByUnverifiedUsers;
        }

        public double getPercentageTweetsByVerifiedUsers() {
            return percentageTweetsByVerifiedUsers;
        }

        public void setPercentageTweetsByVerifiedUsers(double percentageTweetsByVerifiedUsers) {
            this.percentageTweetsByVerifiedUsers = percentageTweetsByVerifiedUsers;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }
    }
}
