package nl.slindenau.openmusicscrobbler.service.release;

import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.DiscogsServiceReleaseTest;

import java.util.Arrays;
import java.util.List;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsRelease2047006Test extends DiscogsServiceReleaseTest {

    private static final String ARTIST = "Richard Searles";
    private static final String ALBUM = "Celtic Cross";

    private static final List<Track> EXPECTED_TRACKS_IN_RELEASE = Arrays.asList(
            new Track("1", ARTIST, "Cabalgata", "3:50", 230),
            new Track("2", ARTIST, "A Thousand Curses On Love", "2:46", 166),
            new Track("3", ARTIST, "Wet Is The Night", "1:40", 100),
            new Track("4", ARTIST, "Abercairney House", "2:26", 146),
            new Track("5", ARTIST, "An Dro", "2:53", 173),
            new Track("6", ARTIST, "My Dark Haired Maid From Cornaig", "2:55", 175),
            new Track("7", ARTIST, "Rondes", "2:35", 155),
            new Track("8", ARTIST, "Sir Festus Burke", "2:14", 134),
            new Track("9", ARTIST, "Laridé A Huit Temps", "2:44", 164),
            new Track("10", ARTIST, "The White Houses Of Sheildaig", "3:01", 181),
            new Track("11", ARTIST, "The Bedding Of The Bride", "3:31", 211),
            new Track("12", ARTIST, "Laridé A Six Temps", "2:26", 146),
            new Track("13", ARTIST, "Clanranald", "2:25", 145),
            new Track("14", ARTIST, "The Fyket", "2:33", 153),
            new Track("15", ARTIST, "An Occasional Song", "2:57", 177),
            new Track("16", ARTIST, "Galician Dance", "2:26", 146),
            new Track("17", ARTIST, "The Haggis", "2:00", 120),
            new Track("18", ARTIST, "No Surrender", "2:38", 158),
            new Track("19", ARTIST, "Tavern On The Moor", "1:57", 117)
    );

    @Override
    protected String getReleaseFileName() {
        return "release-2047006.json";
    }

    @Override
    protected List<Track> getExpectedTracksInRelease() {
        return EXPECTED_TRACKS_IN_RELEASE;
    }

    @Override
    protected String getReleaseArtist() {
        return ARTIST;
    }

    @Override
    protected String getReleaseTitle() {
        return ALBUM;
    }
}