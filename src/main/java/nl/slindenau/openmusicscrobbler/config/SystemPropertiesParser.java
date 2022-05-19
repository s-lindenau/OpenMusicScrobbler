package nl.slindenau.openmusicscrobbler.config;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;

import java.time.Duration;
import java.time.format.DateTimeParseException;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class SystemPropertiesParser {

    public Boolean asBooleanProperty(String input) {
        return Boolean.parseBoolean(input);
    }

    public Duration asDuration(String key, String input) {
        try {
            return Duration.parse(input);
        } catch (DateTimeParseException ex) {
            String message = String.format("Invalid Duration format '%s' for property %s", input, key);
            throw new OpenMusicScrobblerException(message, ex);
        }
    }

}
