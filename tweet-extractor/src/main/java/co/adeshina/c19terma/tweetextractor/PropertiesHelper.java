package co.adeshina.c19terma.tweetextractor;

import java.util.Map;
import java.util.Properties;

class PropertiesHelper {

    private Properties properties = new Properties();

    private String file;

    public PropertiesHelper(String file) {
        this.file = file;
    }

    public Map<String, String> twitterCredentials() {

        // todo use Properties() to load app.properties file
        //      return twitter credentials as map

        if(properties.isEmpty()) {
            loadProperties();
        }

        return null;
    }

    public Map<String, String> kafkaConfig() {

        if(properties.isEmpty()) {
            loadProperties();
        }

        return null;
    }


    private void loadProperties() {
        // todo: properties.load(..); from app.props file


    }




}
