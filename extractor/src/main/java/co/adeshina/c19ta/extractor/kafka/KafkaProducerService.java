package co.adeshina.c19ta.extractor.kafka;

/**
 * Base Interface for any services that write records to a Kafka broker.
 */
public interface KafkaProducerService<T> {

    /**
     * Writes a record to the Kafka broker using the given key.
     *
     * @param key The key to associate the data item with.
     * @param record The data item to be written to Kafka.
     */
    void send(String key, T record);

    /**
     * Closes the underlying Kafka producer. Typically called when the system is shutting down.
     */
    void close();
}
