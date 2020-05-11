package co.adeshina.c19terma.tweetextractor.kafka;

import co.adeshina.c19terma.common.dto.TweetData;
import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;

public class KafkaProducerService {

    private Map<String, Object> kafkaConfigs;
    private KafkaProducer<String, TweetData> kafkaProducer;

    public KafkaProducerService(Map<String, Object> kafkaConfigs) {
        this.kafkaConfigs = kafkaConfigs;
        this.kafkaProducer = new KafkaProducer<String, TweetData>(kafkaConfigs);
    }

    // todo: see kakfa docs and first demo for how to configure this producer

}
