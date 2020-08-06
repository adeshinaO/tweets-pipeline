package co.adeshina.c19ta.data_api.service;

import java.util.List;

/**
 * Base interface for Kafka consumer service that polls records and commits offsets.
 * @param <T> The type of the records the consumer would poll.
 */
public interface KafkaConsumerService<T> {
    List<T> poll();
    void commitOffsets();
}
