package nl.slindenau.openmusicscrobbler;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class SystemProperties {

    // todo: change to settings file

    private static final String DISCOGS_USERNAME_PROPERTY = "discogs.username";
    private static final String LAST_FM_USERNAME_PROPERTY = "lastfm.username";
    private static final String LAST_FM_PASSWORD_PROPERTY = "lastfm.password";
    private static final String LAST_FM_API_KEY_PROPERTY = "lastfm.api.key";
    private static final String LAST_FM_API_SECRET_PROPERTY = "lastfm.api.secret";

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

    private String getProperty(String key) {
        return System.getProperty(key);
    }
}
