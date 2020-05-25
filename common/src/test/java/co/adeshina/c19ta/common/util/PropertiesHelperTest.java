package co.adeshina.c19ta.common.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertiesHelperTest {

    @Test
    public void shouldLoadProperties() throws Exception {

        Path resourceDirectory = Paths.get("src","test","resources", "test.properties");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        PropertiesHelper propertiesHelper = new PropertiesHelper(absolutePath);
        Map<String, String> kafkaProperties =  propertiesHelper.kafkaProperties();
        Map<String, String> twitterProperties = propertiesHelper.twitterProperties();

        assertEquals("topic1234", kafkaProperties.get(PropertiesHelper.KAFKA_INPUT_TOPIC));
        assertEquals("key1234", twitterProperties.get(PropertiesHelper.TWITTER_CONSUMER_KEY));
    }
}
