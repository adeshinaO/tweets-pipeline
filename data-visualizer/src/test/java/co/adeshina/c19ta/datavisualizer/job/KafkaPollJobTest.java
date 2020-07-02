package co.adeshina.c19ta.datavisualizer.job;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.datavisualizer.service.DataPacketService;
import co.adeshina.c19ta.datavisualizer.service.DataPacketServiceImpl;
import co.adeshina.c19ta.datavisualizer.service.KafkaConsumerService;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class KafkaPollJobTest {

    private static final int SAMPLE_DATA_SIZE = 7;
    private List<TweetAggregate> buffer = (List<TweetAggregate>) mock(List.class);
    private DataPacketService mockPacketService = mock(DataPacketServiceImpl.class);
    private KafkaConsumerService<TweetAggregate> mockConsumerService =
            (KafkaConsumerService<TweetAggregate>) mock(KafkaConsumerService.class);

    @BeforeEach
    public void setup() {
        when(mockConsumerService.poll()).thenReturn(dataHelper());
        when(buffer.addAll(anyList())).thenReturn(true);
        when(buffer.size()).thenReturn(SAMPLE_DATA_SIZE);
    }

    @Test
    public void shouldPollKafkaForSampleData() {
        KafkaPollJob pollJob = new KafkaPollJob(buffer, mockConsumerService, mockPacketService);
        pollJob.pollBroker();
        assertEquals(SAMPLE_DATA_SIZE, buffer.size());
    }

    @Test
    public void shouldFlushBuffer() {
        KafkaPollJob pollJob = new KafkaPollJob(buffer, mockConsumerService, mockPacketService);
        pollJob.pollBroker();
        pollJob.flushBuffer();
        verify(mockConsumerService).commitOffsets();
        verify(mockPacketService).buildPacket(any());
    }

    private List<TweetAggregate> dataHelper() {
        List<TweetAggregate> result = new ArrayList<>();
        for (int i = 0; i < SAMPLE_DATA_SIZE; i++) {
            result.add(new TweetAggregate());
        }
        return result;
    }
}
