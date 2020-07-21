package co.adeshina.c19ta.datavisualizer.service;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.datavisualizer.dto.DataPacket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class DataPacketServiceImpl implements DataPacketService {

    private Set<DataPacket> packetContainer = new HashSet<>(1);

    @Override
    public void buildPacket(List<TweetAggregate> tweets) {

        Map<String, DataPacket.Data> dataMap = new HashMap<>();
        int totalTweetsVerifiedUsers = 0;
        int totalTweetsUnverifiedUsers = 0;

        for (TweetAggregate tweetAggregate: tweets) {

            String term = tweetAggregate.getTerm();
            DataPacket.Data data = new DataPacket.Data();
            data.setTerm(term);
            dataMap.put(term, data);

            Map<TweetAggregate.AccountType, Integer> tweetCountMap = tweetAggregate.getCountByAccountType();
            totalTweetsVerifiedUsers += tweetCountMap.get(TweetAggregate.AccountType.VERIFIED);
            totalTweetsUnverifiedUsers += tweetCountMap.get(TweetAggregate.AccountType.UNVERIFIED);
        }

        for (TweetAggregate tweetAggregate: tweets) {

            Map<TweetAggregate.AccountType, Integer> countMap = tweetAggregate.getCountByAccountType();
            int tweetsVerified = countMap.get(TweetAggregate.AccountType.VERIFIED);
            int tweetsUnverified = countMap.get(TweetAggregate.AccountType.UNVERIFIED);

            DataPacket.Data data = dataMap.get(tweetAggregate.getTerm());
            data.setPercentageTweetsByVerifiedUsers(percentageOfTotal(totalTweetsVerifiedUsers, tweetsVerified));
            data.setPercentageTweetsByUnverifiedUsers(percentageOfTotal(totalTweetsUnverifiedUsers, tweetsUnverified));
        }

        DataPacket dataPacket = new DataPacket();
        dataPacket.setData(new HashSet<>(dataMap.values()));
        dataPacket.setTotalTweetsUnverifiedUsers(totalTweetsUnverifiedUsers);
        dataPacket.setTotalTweetsVerifiedUsers(totalTweetsVerifiedUsers);
        dataPacket.setBuildTime(ZonedDateTime.now(ZoneId.of("Africa/Lagos")));

        packetContainer.add(dataPacket);
    }

    @Override
    public Optional<DataPacket> getPacket() {

        Optional<DataPacket> optional = Optional.empty();

        if (!packetContainer.isEmpty()) {
            optional = packetContainer.stream().findFirst();
            packetContainer.clear();
        }

        return optional;
    }

    // Calculates the percentage of 'value' in 'total'
    private double percentageOfTotal(double total, double value) {
        BigDecimal bd = BigDecimal.valueOf((value / total) * 100);
        return bd.setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
