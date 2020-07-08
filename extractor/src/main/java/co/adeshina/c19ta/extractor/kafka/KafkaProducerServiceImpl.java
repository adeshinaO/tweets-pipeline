package co.adeshina.c19ta.extractor.kafka;

import co.adeshina.c19ta.common.dto.TweetData;
import co.adeshina.c19ta.common.serdes.TweetDataSerde;

import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProducerServiceImpl implements KafkaProducerService<TweetData> {

    private Logger logger = LoggerFactory.getLogger(KafkaProducerServiceImpl.class);
    private KafkaProducer<String, TweetData> kafkaProducer;
    private final String topic;

    public KafkaProducerServiceImpl(Map<String, Object> kafkaConfigs, String topic) {
        this.kafkaProducer = new KafkaProducer<>(kafkaConfigs, new StringSerializer(), new TweetDataSerde());
        this.topic = topic;
    }

    @Override
    public void send(String key, TweetData data) {
        ProducerRecord<String, TweetData> record = new ProducerRecord<>(topic, key, data);
        kafkaProducer.send(record, ((metadata, exception) -> {
            if (exception != null) {
                logger.error("Failed to write record", exception);
            } else {
                logger.info("one record written to partion: " + metadata.partition() + "of topic: " + metadata.topic());
            }
        }));
    }

    @Override
    public void close() {
        logger.info("Closing the Kafka producer");
        kafkaProducer.close();
    }
}
