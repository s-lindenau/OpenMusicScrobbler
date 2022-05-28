package nl.slindenau.openmusicscrobbler.service.release;

import nl.slindenau.openmusicscrobbler.config.ApplicationProperty;
import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.DiscogsServiceReleaseTest;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Test with a release that has no track durations.<br/>
 * Should use the configured fallback via {@link ApplicationProperty#DISCOGS_TRACK_LENGTH}
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsRelease5753364Test extends DiscogsServiceReleaseTest {

    private static final String ARTIST = "Aileach";
    private static final String ALBUM = "Ard Rí";
    private static final Duration DEFAULT_TRACK_LENGTH = Duration.ofMinutes(4);

    private static final List<Track> EXPECTED_TRACKS_IN_RELEASE = Arrays.asList(
            new Track("A1", ARTIST, "'P' Stands For Paddy", "", DEFAULT_TRACK_LENGTH),
            new Track("A2", ARTIST, "The Heather Breeze; The Yellow Tinker", "", DEFAULT_TRACK_LENGTH),
            new Track("A3", ARTIST, "The Green Fields Of America", "", DEFAULT_TRACK_LENGTH),
            new Track("A4", ARTIST, "Banish Misfortune; The Fermoy Lasses; The Chair Against The Door", "", DEFAULT_TRACK_LENGTH),
            new Track("A5", ARTIST, "Banks Of The Bann", "", DEFAULT_TRACK_LENGTH),
            new Track("A6", ARTIST, "Bold Sean And The Tinker", "", DEFAULT_TRACK_LENGTH),
            new Track("B1", ARTIST, "Paddy Ryans Dream; The Dublin Lasses", "", DEFAULT_TRACK_LENGTH),
            new Track("B2", ARTIST, "The Bold Tenant Farmer; The Nine Points Of Roguery", "", DEFAULT_TRACK_LENGTH),
            new Track("B3", ARTIST, "The Friendly Visit; The Cup Of Tea", "", DEFAULT_TRACK_LENGTH),
            new Track("B4", ARTIST, "The Lakes Of Coolfin", "", DEFAULT_TRACK_LENGTH),
            new Track("B5", ARTIST, "Four Drunken Nights", "", DEFAULT_TRACK_LENGTH),
            new Track("B6", ARTIST, "Bunch Of Thyme", "", DEFAULT_TRACK_LENGTH),
            new Track("B7", ARTIST, "Jigs Of Aileach II", "", DEFAULT_TRACK_LENGTH)
    );

    @Override
    protected String getReleaseFileName() {
        return "release-5753364.json";
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
