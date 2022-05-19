package nl.slindenau.openmusicscrobbler.config;

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

    public Boolean isDebugEnabled() {
        return Boolean.parseBoolean(getDebugProperty());
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

    public String getDiscogsConnectionTimeout() {
        return getOptionalProperty(DISCOGS_CONNECTION_TIMEOUT_PROPERTY, DISCOGS_DEFAULT_CONNECTION_TIMEOUT);
    }

    public String getDiscogsReadTimeout() {
        return getOptionalProperty(DISCOGS_READ_TIMEOUT_PROPERTY, DISCOGS_DEFAULT_READ_TIMEOUT);
    }

    private String getProperty(String key) {
        return System.getProperty(key);
    }

    private String getOptionalProperty(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }
}
