package nl.slindenau.openmusicscrobbler.service.release;

import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.DiscogsServiceReleaseTest;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Test with a release that has a track with duration > 1 hour
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsRelease505500Test extends DiscogsServiceReleaseTest {

    private static final String ARTIST = "Sleep";
    private static final String ALBUM = "Dopesmoker";

    private static final List<Track> EXPECTED_TRACKS_IN_RELEASE = Arrays.asList(
            new Track("1", ARTIST, "Dopesmoker", "1:03:31", Duration.ofSeconds(3811)),
            new Track("2", ARTIST, "Sonic Titan", "9:36", Duration.ofSeconds(576))
    );

    @Override
    protected String getReleaseFileName() {
        return "release-505500.json";
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
