package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DateTimeService {

    public static final String DATE_TIME_FORMAT = "d-M-yyyy H:m";

    public Instant parseInstant(String input) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.getDefault());
        LocalDateTime localDateTime = parseLocalDateTime(input, format);
        return toInstant(localDateTime);
    }

    private LocalDateTime parseLocalDateTime(String input, DateTimeFormatter format) {
        try {
            return LocalDateTime.parse(input, format);
        } catch (DateTimeParseException e) {
            String errorMessage = String.format("Input '%s' does not match date format %s", input, DATE_TIME_FORMAT);
            throw new OpenMusicScrobblerException(errorMessage, e);
        }
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        return ZonedDateTime.of(localDateTime, zoneId).toInstant();
    }
}
