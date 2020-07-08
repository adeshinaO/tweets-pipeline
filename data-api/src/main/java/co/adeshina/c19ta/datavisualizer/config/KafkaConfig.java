package co.adeshina.c19ta.datavisualizer.config;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.common.serdes.TweetAggregateSerde;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@PropertySource(value = "classpath:kafka-consumer-config.properties")
public class KafkaConfig {

    @Value("${bootstrap.servers}")
    private String bootstrapServers;

    @Value("${enable.auto.commit}")
    private String enableAutocommit;

    @Value("${max.poll.records}")
    private String maxPollRecords;

    @Value("${max.poll.interval.ms}")
    private String maxPollIntervalMs;

    @Value("${group.id}")
    private String groupId;

    @Value("${session.timeout.ms}")
    private String sessionTimeoutMs;

    @Value("${kafka.output.topic}")
    private String topic;

    private KafkaConsumer<String, TweetAggregate> kafkaConsumer;

    @Bean
    public KafkaConsumer<String, TweetAggregate> kafkaConsumerBean() {

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutocommit);
        config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);

        kafkaConsumer = new KafkaConsumer<>(config, new StringDeserializer(), new TweetAggregateSerde());
        kafkaConsumer.subscribe(Collections.singletonList(topic));

        return kafkaConsumer;
    }

    @PreDestroy
    private void close() {
        kafkaConsumer.close();
    }
}
