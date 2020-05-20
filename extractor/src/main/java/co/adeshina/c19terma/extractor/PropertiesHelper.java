package co.adeshina.c19terma.extractor;

import co.adeshina.c19terma.extractor.exception.ConfigPropertiesInitException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class PropertiesHelper {

    // TODO: Logger

    private final Properties properties = new Properties();
    private final Map<String, String> twitterProps = new HashMap<>();
    private final Map<String, String> kafkaProps = new HashMap<>();
    private String file;

    public PropertiesHelper(String file) {
        this.file = file;
    }

    public Map<String, String> twitterProperties() throws ConfigPropertiesInitException {

        if(properties.isEmpty()) {
            loadProperties();
        }

        return new HashMap<>(twitterProps);
    }

    public Map<String, String> kafkaProperties() throws ConfigPropertiesInitException {

        if(properties.isEmpty()) {
            loadProperties();
        }

        return new HashMap<>(kafkaProps);
    }


    private void loadProperties() throws ConfigPropertiesInitException {
        try {
            properties.load(new FileReader(file));

            for (String key: properties.stringPropertyNames()) {
                if (key.startsWith("kafka")) {
                    kafkaProps.put(key, properties.getProperty(key));
                } else if (key.startsWith("twitter")) {
                    twitterProps.put(key, properties.getProperty(key));
                }
            }

            // TODO: Log success of properties loading...
        } catch (IOException e) {
            throw new ConfigPropertiesInitException("Failed to load properties from file", e);
        }
    }

}
