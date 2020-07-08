package co.adeshina.c19ta.datavisualizer.service;

import co.adeshina.c19ta.common.dto.TweetAggregate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class KafkaConsumerServiceImpl implements KafkaConsumerService<TweetAggregate> {

    private KafkaConsumer<String, TweetAggregate> consumer;

    @Value("${kafka.output.topic}")
    private String topic;

    @Autowired
    public KafkaConsumerServiceImpl(KafkaConsumer<String, TweetAggregate> consumer) {
        this.consumer = consumer;
    }

    @Override
    public List<TweetAggregate> poll() {
        List<TweetAggregate> result = new ArrayList<>();
        consumer.poll(Duration.ofMillis(100)).records(topic).forEach(record -> result.add(record.value()));
        return result;
    }

    @Override
    public void commitOffsets() {
        consumer.commitSync();
    }
}
