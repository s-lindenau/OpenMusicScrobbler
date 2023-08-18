package nl.slindenau.openmusicscrobbler.config;

import java.util.Optional;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public enum ApplicationProperty {

    DISCOGS_USERNAME("discogs.username", "Discogs username"),
    DISCOGS_TOKEN("discogs.personal.token", "Discogs personal access token (Optional), see https://www.discogs.com/settings/developers", true),
    LAST_FM_USERNAME("lastfm.username", "Last.fm username"),
    LAST_FM_PASSWORD("lastfm.password", "Last.fm password", true),
    LAST_FM_API_KEY("lastfm.api.key", "Last.fm API key, see https://www.last.fm/api/authentication"),
    LAST_FM_API_SECRET("lastfm.api.secret", "Last.fm API secret", true),
    DISCOGS_CONNECTION_TIMEOUT("discogs.connection.timeout", "Timeout for Discogs API connection setup (Java Duration format)", "PT10S"),
    DISCOGS_READ_TIMEOUT("discogs.read.timeout", "Timeout for Discogs API call results (Java Duration format)", "PT60S"),
    DISCOGS_TRACK_LENGTH("discogs.track.length.default", "Default track duration to substitute a missing duration on Discogs (Java Duration format)", "PT1M"),
    DEBUG("oms.debug", "Enable debug logging [true or false]. Scrobbles are not sent to Last.FM when enabled.", Boolean.FALSE.toString()),
    LOG_LEVEL("oms.log.level", "Determines what logging information is written to the log file. Possible values are Logback levels (logging library)", "info"),

    ;

    private final String key;
    private final String description;
    private final String defaultValue;
    private final boolean isSensitive;

    ApplicationProperty(String key, String description) {
        this(key, description, null);
    }

    ApplicationProperty(String key, String description, boolean isSensitive) {
        this(key, description, null, isSensitive);
    }

    ApplicationProperty(String key, String description, String defaultValue) {
        this(key, description, defaultValue, false);
    }

    ApplicationProperty(String key, String description, String defaultValue, boolean isSensitive) {
        this.key = key;
        this.description = description;
        this.defaultValue = defaultValue;
        this.isSensitive = isSensitive;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public Optional<String> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    public boolean isSensitive() {
        return isSensitive;
    }
}
