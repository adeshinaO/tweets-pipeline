package co.adeshina.c19ta.data_api.service;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.data_api.dto.DataPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataPacketServiceImplTest {

    KafkaConsumerService<TweetAggregate> mockKafkaConsumer =
            (KafkaConsumerService<TweetAggregate>) mock(KafkaConsumerService.class);

    @BeforeEach
    public void setup() {
        when(mockKafkaConsumer.poll()).thenReturn(dataHelper());
        doNothing().when(mockKafkaConsumer).commitOffsets();
    }

    @Test
    public void shouldBuildCorrectDataPacket() {
        DataPacketService dataPacketService = new DataPacketServiceImpl(mockKafkaConsumer);
        dataPacketService.buildPacket();
        Optional<DataPacket> dataPacketOptional = dataPacketService.buildPacket();

        assertTrue(dataPacketOptional.isPresent());

        DataPacket packet = dataPacketOptional.get();
        Optional<DataPacket.Data> coronavirusOptional = packet.getData()
                                                              .stream()
                                                              .filter(data -> data.getTerm().equalsIgnoreCase("coronavirus"))
                                                              .findFirst();

        assertTrue(coronavirusOptional.isPresent());

        DataPacket.Data coronavirusData = coronavirusOptional.get();
        double percentageThousandFollowers = coronavirusData.getPercentageThousandFollowers();
        double percentageLessThanThousandFollowers = coronavirusData.getPercentageLessThanThousandFollowers();

        assertEquals(44.0, percentageLessThanThousandFollowers);
        assertEquals(34.6, percentageThousandFollowers);
    }

    private List<TweetAggregate> dataHelper() {
        List<TweetAggregate> result = new ArrayList<>();
        result.add(construct("COVID-19", 12, 8));
        result.add(construct("Coronavirus", 9, 11));
        result.add(construct("Chinese Virus", 5, 6));
        return result;
    }

    private TweetAggregate construct(String term, int tweetCountThousandFollowers, int countLessThanThousandFollowers) {
        TweetAggregate tweetAggregate = new TweetAggregate();
        tweetAggregate.setTerm(term);
        Map<TweetAggregate.AccountType, Integer> countMap = new HashMap<>();
        countMap.put(TweetAggregate.AccountType.ONE_THOUSAND_FOLLOWERS, tweetCountThousandFollowers);
        countMap.put(TweetAggregate.AccountType.LESS_THAN_ONE_THOUSAND_FOLLOWERS, countLessThanThousandFollowers);
        tweetAggregate.setCountByAccountType(countMap);
        return tweetAggregate;
    }
}
