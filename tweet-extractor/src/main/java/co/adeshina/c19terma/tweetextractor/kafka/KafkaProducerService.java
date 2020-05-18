package co.adeshina.c19terma.tweetextractor.kafka;

import co.adeshina.c19terma.common.dto.TweetData;
import co.adeshina.c19terma.common.serdes.TweetDataSerde;
import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaProducerService {

    private KafkaProducer<String, TweetData> kafkaProducer;
    private final String topic;

    public KafkaProducerService(Map<String, Object> kafkaConfigs, String topic) {
        this.kafkaProducer = new KafkaProducer<>(kafkaConfigs, new StringSerializer(), new TweetDataSerde());
        this.topic = topic;
    }

    public void send(String key, TweetData data) {
        ProducerRecord<String, TweetData> record = new ProducerRecord<>(topic, key, data);
        kafkaProducer.send(record, ((metadata, exception) -> {
            // todo: What to do here? use proper logger?
            //      check what kind of exception and react accordingly?
        }));
    }

    public void close() {
        kafkaProducer.close();
    }

}
