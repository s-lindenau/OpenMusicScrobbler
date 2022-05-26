package nl.slindenau.openmusicscrobbler.config;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;

import java.time.Duration;
import java.time.format.DateTimeParseException;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class PropertiesParser {

    public boolean asBooleanProperty(String input) {
        return Boolean.parseBoolean(input);
    }

    public Duration asDuration(String key, String input) {
        try {
            return parseDuration(input);
        } catch (DateTimeParseException | OpenMusicScrobblerException ex) {
            String message = String.format("Invalid Duration format '%s' for property %s", input, key);
            throw new OpenMusicScrobblerException(message, ex);
        }
    }

    private Duration parseDuration(String input) {
        Duration duration = Duration.parse(input);
        checkValidDuration(duration);
        return duration;
    }

    private void checkValidDuration(Duration duration) {
        if (duration == null || duration.isNegative()) {
            throw new OpenMusicScrobblerException("Negative duration length not allowed");
        }
    }

}
