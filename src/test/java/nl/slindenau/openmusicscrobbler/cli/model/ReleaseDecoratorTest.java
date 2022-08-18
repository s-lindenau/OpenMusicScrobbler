package nl.slindenau.openmusicscrobbler.cli.model;

import nl.slindenau.openmusicscrobbler.model.MusicReleaseBasicInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
class ReleaseDecoratorTest {

    private static final int ID = 1;
    private static final String ARTIST = "Artist";
    private static final String TITLE = "Title";
    private static final String FORMAT = "CD";
    private static final int YEAR = 2000;
    private static final int MISSING_YEAR = 0;

    @Test
    void testWithYear() {
        MusicReleaseBasicInformation release = new MusicReleaseBasicInformation(ID, ID, ARTIST, TITLE, FORMAT, YEAR);
        assertReleaseMatches(release, "01: Artist - Title (CD, 2000)");
    }

    @Test
    void testWithoutYear() {
        MusicReleaseBasicInformation release = new MusicReleaseBasicInformation(ID, ID, ARTIST, TITLE, FORMAT, MISSING_YEAR);
        assertReleaseMatches(release, "01: Artist - Title (CD)");
    }

    private void assertReleaseMatches(MusicReleaseBasicInformation release, String expectedValue) {
        ReleaseDecorator releaseDecorator = new ReleaseDecorator(release);
        String actualValue = releaseDecorator.toString();
        Assertions.assertEquals(expectedValue, actualValue, "Decorated release toString mismatch");
    }
}