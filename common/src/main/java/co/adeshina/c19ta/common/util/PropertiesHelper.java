package co.adeshina.c19ta.common.util;


import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesHelper {

    public static final String KAFKA_BOOTSTRAP_SERVERS = "kafka.bootstrap.servers";
    public static final String KAFKA_EXTRACTOR_ID = "kafka.extractor.id";
    public static final String KAFKA_PROCESSOR_ID = "kafka.processor.id";
    public static final String KAFKA_INPUT_TOPIC = "kafka.topic.input";
    public static final String KAFKA_OUTPUT_TOPIC = "kafka.topic.output";
    public static final String KAFKA_ACKS = "kafka.acks";

    public static final String TWITTER_CONSUMER_SECRET = "twitter.consumer.secret";
    public static final String TWITTER_CONSUMER_KEY = "twitter.consumer.key";

    private final Properties properties = new Properties();
    private final Map<String, String> twitterProps = new HashMap<>();
    private final Map<String, String> kafkaProps = new HashMap<>();
    private String file;

    public PropertiesHelper(String file) {
        this.file = file;
    }

    public Map<String, String> twitterProperties() throws PropertiesInitFailedException {

        if(properties.isEmpty()) {
            loadProperties();
        }

        return new HashMap<>(twitterProps);
    }

    public Map<String, String> kafkaProperties() throws PropertiesInitFailedException {

        if(properties.isEmpty()) {
            loadProperties();
        }

        return new HashMap<>(kafkaProps);
    }


    private void loadProperties() throws PropertiesInitFailedException {
        try {
            properties.load(new FileReader(file));

            for (String key: properties.stringPropertyNames()) {
                if (key.startsWith("kafka")) {
                    kafkaProps.put(key, properties.getProperty(key));
                } else if (key.startsWith("twitter")) {
                    twitterProps.put(key, properties.getProperty(key));
                }
            }

        } catch (IOException e) {
            throw new PropertiesInitFailedException("Failed to load properties from file", e);
        }
    }

}
