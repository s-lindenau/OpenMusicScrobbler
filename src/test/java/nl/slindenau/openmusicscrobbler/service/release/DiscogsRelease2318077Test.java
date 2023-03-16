package nl.slindenau.openmusicscrobbler.service.release;

import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.DiscogsServiceReleaseTest;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Test with a release that has tracks of type index (with sub tracks)
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsRelease2318077Test extends DiscogsServiceReleaseTest {

    private static final String ARTIST = "Crosby, Stills, Nash & Young";
    private static final String ALBUM = "Déjà Vu";

    private static final List<Track> EXPECTED_TRACKS_IN_RELEASE = Arrays.asList(
            new Track("A1", ARTIST, "Carry On", "4:25", Duration.parse("PT4M25S")),
            new Track("A2", ARTIST, "Teach Your Children", "2:53", Duration.parse("PT2M53S")),
            new Track("A3", ARTIST, "Almost Cut My Hair", "4:25", Duration.parse("PT4M25S")),
            new Track("A4", ARTIST, "Helpless", "3:30", Duration.parse("PT3M30S")),
            new Track("A5", ARTIST, "Woodstock", "3:52", Duration.parse("PT3M52S")),
            new Track("B1", ARTIST, "Déjà Vu", "4:10", Duration.parse("PT4M10S")),
            new Track("B2", ARTIST, "Our House", "2:59", Duration.parse("PT2M59S")),
            new Track("B3", ARTIST, "4 + 20", "1:55", Duration.parse("PT1M55S")),
            new Track("B4a", ARTIST, "Country Girl: Whiskey Boot Hill", "", DEFAULT_TRACK_LENGTH),
            new Track("B4b", ARTIST, "Country Girl: Down, Down, Down", "", DEFAULT_TRACK_LENGTH),
            new Track("B4c", ARTIST, "Country Girl: Country Girl (I Think You're Pretty)", "", DEFAULT_TRACK_LENGTH),
            new Track("B5", ARTIST, "Everybody I Love You", "2:20", Duration.parse("PT2M20S"))
    );

    @Override
    protected String getReleaseFileName() {
        return "release-2318077.json";
    }

    @Override
    protected Collection<Track> getExpectedTracksInRelease() {
        return EXPECTED_TRACKS_IN_RELEASE;
    }

    @Override
    protected Collection<ReleasePart> getExpectedPartsInRelease() {
        // TODO: B4(abc)
        return List.of(createReleasePart("", EXPECTED_TRACKS_IN_RELEASE));
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
