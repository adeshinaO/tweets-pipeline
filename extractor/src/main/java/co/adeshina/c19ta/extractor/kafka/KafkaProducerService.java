package co.adeshina.c19ta.extractor.kafka;

import co.adeshina.c19ta.common.dto.TweetData;
import co.adeshina.c19ta.common.serdes.TweetDataSerde;

import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaProducerService {

    // TODO: Logger

    private KafkaProducer<String, TweetData> kafkaProducer;
    private final String topic;

    public KafkaProducerService(Map<String, Object> kafkaConfigs, String topic) {
        this.kafkaProducer = new KafkaProducer<>(kafkaConfigs, new StringSerializer(), new TweetDataSerde());
        this.topic = topic;
    }

    public void send(String key, TweetData data) {
        ProducerRecord<String, TweetData> record = new ProducerRecord<>(topic, key, data);
        kafkaProducer.send(record, ((metadata, exception) -> {
            if (exception != null) {
                // todo: Send failed.. log error
            } else {
                // todo: Send successful, log the partition it went.
            }
        }));
    }

    public void close() {
        // todo: log
        kafkaProducer.close();
    }
}
