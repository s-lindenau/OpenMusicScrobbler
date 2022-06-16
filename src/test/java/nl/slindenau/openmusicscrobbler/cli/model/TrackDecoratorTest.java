package nl.slindenau.openmusicscrobbler.cli.model;

import nl.slindenau.openmusicscrobbler.model.Track;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
class TrackDecoratorTest {

    private static final String POSITION = "1";
    private static final String ARTIST = "Artist";
    private static final String TITLE = "Title";
    private static final String DURATION = "01:00";
    private static final String MISSING_DURATION = "";

    @Test
    void testWithDuration() {
        Track track = new Track(POSITION, ARTIST, TITLE, DURATION, Duration.ofMinutes(1));
        assertTrackMatches(track, " 1: Artist - Title (01:00)");
    }

    @Test
    void testWithoutDuration() {
        Track track = new Track(POSITION, ARTIST, TITLE, MISSING_DURATION, null);
        assertTrackMatches(track, " 1: Artist - Title");
    }

    private void assertTrackMatches(Track track, String expectedValue) {
        TrackDecorator trackDecorator = new TrackDecorator(track);
        String actualValue = trackDecorator.toString();
        Assertions.assertEquals(expectedValue, actualValue, "Decorated track toString mismatch");
    }
}