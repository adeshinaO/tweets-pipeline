# About 

This is a stream processing application written in Java using the Kafka Streams library. It consumes tweet metadata
written to a Kafka broker by `extractor`, merges them into aggregate records which are written back to Kafka for
 `data-api` to consume. 
