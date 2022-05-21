package nl.slindenau.openmusicscrobbler.config;

import java.time.Duration;
import java.util.Properties;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class SystemProperties {

    // todo: change to settings file

    private static final String DEBUG_PROPERTY = "oms.debug";
    private static final String DEBUG_DEFAULT = Boolean.FALSE.toString();
    private static final String DISCOGS_USERNAME_PROPERTY = "discogs.username";
    private static final String LAST_FM_USERNAME_PROPERTY = "lastfm.username";
    private static final String LAST_FM_PASSWORD_PROPERTY = "lastfm.password";
    private static final String LAST_FM_API_KEY_PROPERTY = "lastfm.api.key";
    private static final String LAST_FM_API_SECRET_PROPERTY = "lastfm.api.secret";
    private static final String DISCOGS_CONNECTION_TIMEOUT_PROPERTY = "discogs.connection.timeout";
    private static final String DISCOGS_DEFAULT_CONNECTION_TIMEOUT = "PT10S";
    private static final String DISCOGS_READ_TIMEOUT_PROPERTY = "discogs.read.timeout";
    private static final String DISCOGS_DEFAULT_READ_TIMEOUT = "PT60S";
    private static final String DISCOGS_TRACK_LENGTH_PROPERTY = "discogs.track.length.default";
    private static final String DISCOGS_DEFAULT_TRACK_LENGTH = "PT4M";

    private final SystemPropertiesParser parser;
    private final Properties properties;

    public SystemProperties() {
        this(new SystemPropertiesParser(), System.getProperties());
    }

    protected SystemProperties(SystemPropertiesParser parser, Properties properties) {
        this.parser = parser;
        this.properties = properties;
    }

    public Boolean isDebugEnabled() {
        return parser.asBooleanProperty(getDebugProperty());
    }

    private String getDebugProperty() {
        return getOptionalProperty(DEBUG_PROPERTY, DEBUG_DEFAULT);
    }

    public String getDiscogsUsername() {
        return getProperty(DISCOGS_USERNAME_PROPERTY);
    }

    public String getLastFmUsername() {
        return getProperty(LAST_FM_USERNAME_PROPERTY);
    }

    public String getLastFmPassword() {
        return getProperty(LAST_FM_PASSWORD_PROPERTY);
    }

    public String getLastFmApiKey() {
        return getProperty(LAST_FM_API_KEY_PROPERTY);
    }

    public String getLastFmApiSecret() {
        return getProperty(LAST_FM_API_SECRET_PROPERTY);
    }

    public Duration getDiscogsConnectionTimeout() {
        return getOptionalDuration(DISCOGS_CONNECTION_TIMEOUT_PROPERTY, DISCOGS_DEFAULT_CONNECTION_TIMEOUT);
    }

    public Duration getDiscogsReadTimeout() {
        return getOptionalDuration(DISCOGS_READ_TIMEOUT_PROPERTY, DISCOGS_DEFAULT_READ_TIMEOUT);
    }

    public Duration getDiscogsDefaultTrackLength() {
        return getOptionalDuration(DISCOGS_TRACK_LENGTH_PROPERTY, DISCOGS_DEFAULT_TRACK_LENGTH);
    }

    private Duration getOptionalDuration(String key, String defaultValue) {
        return parser.asDuration(key, getOptionalProperty(key, defaultValue));
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
