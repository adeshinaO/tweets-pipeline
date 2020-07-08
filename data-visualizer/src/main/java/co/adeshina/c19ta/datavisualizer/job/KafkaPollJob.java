package co.adeshina.c19ta.datavisualizer.job;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.datavisualizer.service.DataPacketService;
import co.adeshina.c19ta.datavisualizer.service.KafkaConsumerService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class KafkaPollJob {

    private Logger logger = LoggerFactory.getLogger(KafkaPollJob.class);
    private List<TweetAggregate> buffer = new ArrayList<>();
    private static final int MIN_BUFFER_SIZE = 5;

    private DataPacketService dataPacketService;
    private KafkaConsumerService<TweetAggregate> kafkaConsumerService;

    @Autowired
    public KafkaPollJob(KafkaConsumerService<TweetAggregate> kafkaConsumerService, DataPacketService dataPacketService) {
        this.kafkaConsumerService = kafkaConsumerService;
        this.dataPacketService = dataPacketService;
    }

    // Test Helper
    protected KafkaPollJob(List<TweetAggregate> buffer, KafkaConsumerService<TweetAggregate> consumerService,
            DataPacketService packetService) {
        this.buffer = buffer;
        this.kafkaConsumerService = consumerService;
        this.dataPacketService = packetService;
    }

    @Scheduled(fixedRate = 3500)
    public void pollBroker() {
        logger.info("Starting to poll kafka broker...");
        List<TweetAggregate> data = kafkaConsumerService.poll();
        buffer.addAll(data);
        logger.info("Adding {} data items into buffer", data.size());
    }

    @Scheduled(fixedRate = 35000)
    public void flushBuffer() {
        logger.info("Checking buffer size");
        if (buffer.size() > MIN_BUFFER_SIZE) {
            dataPacketService.buildPacket(buffer);
            kafkaConsumerService.commitOffsets();
            int size = buffer.size();
            buffer.clear();
            logger.info("{} data items moved from buffer", size);
        } else {
            logger.info("Buffer size too small. No flush");
        }
    }
}
