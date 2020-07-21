package co.adeshina.c19ta.datavisualizer.service;

import co.adeshina.c19ta.common.dto.TweetAggregate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService<TweetAggregate> {

    private Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private KafkaConsumer<String, TweetAggregate> consumer;

    @Value("${topic}")
    private String topic;

    @Autowired
    public KafkaConsumerServiceImpl(KafkaConsumer<String, TweetAggregate> consumer) {
        this.consumer = consumer;
    }

    @Override
    public List<TweetAggregate> poll() {
        List<TweetAggregate> result = new ArrayList<>();
        consumer.poll(Duration.ofMillis(150)).records(topic).forEach(record -> result.add(record.value()));
        logger.info("Polled " + result.size() + " records from Kafka broker");
        return result;
    }

    @Override
    public void commitOffsets() {
        consumer.commitSync();
    }
}
