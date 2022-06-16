package nl.slindenau.openmusicscrobbler.service.release;

import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.DiscogsServiceReleaseTest;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Test with a release that has tracks of type index (with sub tracks)
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsRelease2647399Test extends DiscogsServiceReleaseTest {

    private static final String ARTIST = "Vivaldi";
    private static final String ALBUM = "Die Vier Jahreszeiten & Concerto Grosso Op 3 Nr. II D-moll, P.V. 250";
    private static final Duration DEFAULT_TRACK_LENGTH = Duration.ofMinutes(4);

    private static final List<Track> EXPECTED_TRACKS_IN_RELEASE = Arrays.asList(
            new Track("A1", ARTIST, "II Cimento Dell' Armonia E Dell' Inventione: La Primavera (Frühling)", "", DEFAULT_TRACK_LENGTH),
            new Track("A2", ARTIST, "II Cimento Dell' Armonia E Dell' Inventione: L'Estate (Sommer)", "", DEFAULT_TRACK_LENGTH),
            new Track("B1", ARTIST, "II Cimento Dell' Armonia E Dell' Inventione: L'Autumno (Herbst)", "", DEFAULT_TRACK_LENGTH),
            new Track("B2", ARTIST, "II Cimento Dell' Armonia E Dell' Inventione: L'Inverno (Winter)", "", DEFAULT_TRACK_LENGTH),
            new Track("B3.1", ARTIST, "Concerto Grosso D-moll Op. 3 Nr. 11 P.V. 250: Allegro - Allegro", "", DEFAULT_TRACK_LENGTH),
            new Track("B3.2", ARTIST, "Concerto Grosso D-moll Op. 3 Nr. 11 P.V. 250: Largo", "", DEFAULT_TRACK_LENGTH),
            new Track("B3.3", ARTIST, "Concerto Grosso D-moll Op. 3 Nr. 11 P.V. 250: Allegro", "", DEFAULT_TRACK_LENGTH)
    );

    @Override
    protected String getReleaseFileName() {
        return "release-2647399.json";
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