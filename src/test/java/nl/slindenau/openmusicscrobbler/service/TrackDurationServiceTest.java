package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.Duration;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
class TrackDurationServiceTest {

    private final TrackDurationService service = new TrackDurationService();

    @Test
    void testValidTrackLength() {
        parseTrackLength("1:00", 60);
        parseTrackLength("1:10", 70);
        parseTrackLength("10:00", 600);
        parseTrackLength("10:15", 615);
        parseTrackLength("1:15:05", 4505);
    }

    @Test
    void testUnsupportedTrackLength() {
        assertThrows(() -> parseTrackLength("1:5:15:05"));
        assertThrows(() -> parseTrackLength("-1:00"));
    }

    @Test
    void testUnsupportedSeparatorTrackLength() {
        assertThrows(() -> parseTrackLength("1.5.15.05"));
    }

    @Test
    void testInvalidTrackLength() {
        assertThrows(() -> parseTrackLength("abc:def"));
    }

    private void assertThrows(Executable executable) {
        Assertions.assertThrows(OpenMusicScrobblerException.class, executable, "Expected exception for invalid track length format was not thrown");
    }

    private void parseTrackLength(String input, int expectedLength) {
        Duration actualLength = parseTrackLength(input);
        Assertions.assertEquals(expectedLength, actualLength.toSeconds(), "Track Length mismatch");
    }

    private Duration parseTrackLength(String input) {
        return service.parseTrackLength(input);
    }
}