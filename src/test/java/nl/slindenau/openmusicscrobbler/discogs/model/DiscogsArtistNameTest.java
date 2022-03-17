package nl.slindenau.openmusicscrobbler.discogs.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
class DiscogsArtistNameTest {

    private final DiscogsArtistName artistName = new DiscogsArtistName();

    @Test
    void getArtistNameNoDuplicationRemoved() {
        testArtistName("Artist");
        testArtistName("Artist (Not a digit)");
        testArtistName("Artist (1,2)");
        testArtistName("Artist (1.2)");
    }

    @Test
    void getArtistNameDuplicationRemoved() {
        testArtistName("Artist Duplicate (3)", "Artist Duplicate");
    }

    private void testArtistName(String input) {
        testArtistName(input, input);
    }

    private void testArtistName(String input, String expected) {
        String actual = artistName.getArtistName(input);
        Assertions.assertEquals(expected, actual, "Artist name mismatch");
    }
}