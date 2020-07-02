package co.adeshina.c19ta.datavisualizer.service;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.datavisualizer.dto.DataPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataPacketServiceImplTest {

    @Test
    public void shouldBuildCorrectDataPacket() {
        DataPacketService dataPacketService = new DataPacketServiceImpl();
        dataPacketService.buildPacket(dataHelper());
        Optional<DataPacket> dataPacketOptional = dataPacketService.getPacket();

        assertTrue(dataPacketOptional.isPresent());

        DataPacket packet = dataPacketOptional.get();
        Optional<DataPacket.Data> coronavirusOptional = packet.getData()
                                                              .stream()
                                                              .filter(data -> data.getTerm().equalsIgnoreCase("coronavirus"))
                                                              .findFirst();

        assertTrue(coronavirusOptional.isPresent());
        DataPacket.Data coronavirusData = coronavirusOptional.get();

        double percentageTweetsByVerifiedUsers = coronavirusData.getPercentageTweetsByVerifiedUsers();
        double percentageTweetsByUnverifiedUsers = coronavirusData.getPercentageTweetsByUnverifiedUsers();

        assertEquals(57.9, percentageTweetsByUnverifiedUsers);
        assertEquals(42.9, percentageTweetsByVerifiedUsers);
    }

    private List<TweetAggregate> dataHelper() {
        List<TweetAggregate> result = new ArrayList<>();
        result.add(construct("COVID-19", 12, 8));
        result.add(construct("Coronavirus", 9, 11));
        return result;
    }

    private TweetAggregate construct(String term, int verifiedTweetCount, int unverifiedTweetCount) {
        TweetAggregate tweetAggregate = new TweetAggregate();
        tweetAggregate.setTerm(term);
        Map<TweetAggregate.AccountType, Integer> countMap = new HashMap<>();
        countMap.put(TweetAggregate.AccountType.VERIFIED, verifiedTweetCount);
        countMap.put(TweetAggregate.AccountType.UNVERIFIED, unverifiedTweetCount);
        tweetAggregate.setCountByAccountType(countMap);
        return tweetAggregate;
    }
}
