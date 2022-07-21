package nl.slindenau.openmusicscrobbler.config;

import java.time.Duration;
import java.util.Properties;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ApplicationProperties {

    // todo: change to settings file

    private final PropertiesParser parser = new PropertiesParser();
    private final Properties properties;

    public ApplicationProperties() {
        this(System.getProperties());
    }

    protected ApplicationProperties(Properties properties) {
        this.properties = properties;
    }

    public boolean isDebugEnabled() {
        return parser.asBooleanProperty(getDebugProperty());
    }

    private String getDebugProperty() {
        return getProperty(ApplicationProperty.DEBUG);
    }

    public String getLogLevel() {
        return getProperty(ApplicationProperty.LOG_LEVEL);
    }

    public String getDiscogsUsername() {
        return getProperty(ApplicationProperty.DISCOGS_USERNAME);
    }

    public String getLastFmUsername() {
        return getProperty(ApplicationProperty.LAST_FM_USERNAME);
    }

    public String getLastFmPassword() {
        return getProperty(ApplicationProperty.LAST_FM_PASSWORD);
    }

    public String getLastFmApiKey() {
        return getProperty(ApplicationProperty.LAST_FM_API_KEY);
    }

    public String getLastFmApiSecret() {
        return getProperty(ApplicationProperty.LAST_FM_API_SECRET);
    }

    public Duration getDiscogsConnectionTimeout() {
        return getDuration(ApplicationProperty.DISCOGS_CONNECTION_TIMEOUT);
    }

    public Duration getDiscogsReadTimeout() {
        return getDuration(ApplicationProperty.DISCOGS_READ_TIMEOUT);
    }

    public Duration getDiscogsDefaultTrackLength() {
        return getDuration(ApplicationProperty.DISCOGS_TRACK_LENGTH);
    }

    private Duration getDuration(ApplicationProperty applicationProperty) {
        return parser.asDuration(applicationProperty.getKey(), getProperty(applicationProperty));
    }

    private String getProperty(ApplicationProperty applicationProperty) {
        if (applicationProperty.getDefaultValue().isPresent()) {
            return getOptionalProperty(applicationProperty.getKey(), applicationProperty.getDefaultValue().get());
        } else {
            return getProperty(applicationProperty.getKey());
        }
    }

    private String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    private String getOptionalProperty(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    private Properties getProperties() {
        return properties;
    }
}
