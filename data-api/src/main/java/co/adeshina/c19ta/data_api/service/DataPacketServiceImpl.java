package co.adeshina.c19ta.data_api.service;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.data_api.dto.DataPacket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataPacketServiceImpl implements DataPacketService {

    private KafkaConsumerService<TweetAggregate> kafkaConsumerService;

    @Autowired
    public DataPacketServiceImpl(KafkaConsumerService<TweetAggregate> kafkaConsumerService) {
        this.kafkaConsumerService = kafkaConsumerService;
    }

    @Override
    public Optional<DataPacket> buildPacket() {

        List<TweetAggregate> tweets = kafkaConsumerService.poll();

        if (tweets == null || tweets.isEmpty()) {
            return Optional.empty();
        }

        Map<String, DataPacket.Data> dataMap = new HashMap<>();

        // Total tweets from accounts with at least 1k followers
        int thousandFollowersTotal = 0;

        // Total from accts with < 1k followers
        int lessThanThousandFollowersTotal = 0;

        for (TweetAggregate tweetAggregate: tweets) {

            String term = tweetAggregate.getTerm();
            DataPacket.Data data = new DataPacket.Data();
            data.setTerm(term);
            dataMap.put(term, data);

            Map<TweetAggregate.AccountType, Integer> tweetCountMap = tweetAggregate.getCountByAccountType();
            thousandFollowersTotal += tweetCountMap.get(TweetAggregate.AccountType.ONE_THOUSAND_FOLLOWERS);
            lessThanThousandFollowersTotal += tweetCountMap.get(TweetAggregate.AccountType.LESS_THAN_ONE_THOUSAND_FOLLOWERS);
        }

        for (TweetAggregate tweetAggregate: tweets) {

            Map<TweetAggregate.AccountType, Integer> countMap = tweetAggregate.getCountByAccountType();
            int thousandFollowers = countMap.get(TweetAggregate.AccountType.ONE_THOUSAND_FOLLOWERS);
            int lessThanThousandFollowers = countMap.get(TweetAggregate.AccountType.LESS_THAN_ONE_THOUSAND_FOLLOWERS);

            DataPacket.Data data = dataMap.get(tweetAggregate.getTerm());
            data.setPercentageThousandFollowers(percentageOfTotal(thousandFollowersTotal, thousandFollowers));
            data.setPercentageLessThanThousandFollowers(percentageOfTotal(lessThanThousandFollowersTotal, lessThanThousandFollowers));
        }

        DataPacket dataPacket = new DataPacket();
        dataPacket.setData(new ArrayList<>(dataMap.values()));
        dataPacket.setTotalLessThanThousandFollowers(lessThanThousandFollowersTotal);
        dataPacket.setTotalThousandFollowers(thousandFollowersTotal);
        dataPacket.setBuildTime(ZonedDateTime.now(ZoneId.of("Africa/Lagos")));
        kafkaConsumerService.commitOffsets();
        return Optional.of(dataPacket);
    }

    // Calculates the percentage of 'value' in 'total'
    private double percentageOfTotal(double total, double value) {

        if (total == 0.0 || value == 0.0){
            return 0.0;
        }

        double percentage = (value / total) * 100;
        BigDecimal bd = new BigDecimal(percentage);
        return bd.setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
