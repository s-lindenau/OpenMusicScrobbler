package nl.slindenau.openmusicscrobbler.service.release;

import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.DiscogsServiceReleaseTest;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Test with a release that has non-track type tracks (headings)
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsRelease13264226Test extends DiscogsServiceReleaseTest {

    private static final String ARTIST = "Damjan Mravunac";
    private static final String ALBUM = "The Talos Principle - Soundtrack";

    private static final List<Track> EXPECTED_TRACKS_IN_RELEASE = Arrays.asList(
            new Track("1", ARTIST, "Welcome To Heaven", "2:21", Duration.parse("PT2M21S")),
            new Track("1_Alt", ARTIST, "Welcome To Heaven", "2:21", Duration.parse("PT2M21S")),
            new Track("2", ARTIST, "When In Rome", "2:27", Duration.parse("PT2M27S")),
            new Track("3", ARTIST, "Trials", "3:48", Duration.parse("PT3M48S")),
            new Track("3_Alt", ARTIST, "Trials", "3:48", Duration.parse("PT3M48S")),
            new Track("4", ARTIST, "The Sigils Of Our Name", "2:32", Duration.parse("PT2M32S")),
            new Track("4_Alt", ARTIST, "The Sigils Of Our Name", "2:32", Duration.parse("PT2M32S")),
            new Track("5", ARTIST, "Temple Of My Father", "2:48", Duration.parse("PT2M48S")),
            new Track("5_Alt", ARTIST, "Temple Of My Father", "2:48", Duration.parse("PT2M48S")),
            new Track("6", ARTIST, "Made Of Words", "4:45", Duration.parse("PT4M45S")),
            new Track("6_Alt", ARTIST, "Made Of Words", "4:45", Duration.parse("PT4M45S")),
            new Track("7", ARTIST, "Sanctuary", "1:54", Duration.parse("PT1M54S")),
            new Track("8", ARTIST, "A Land Of Great Beauty", "3:18", Duration.parse("PT3M18S")),
            new Track("8_Alt", ARTIST, "A Land Of Great Beauty", "3:18", Duration.parse("PT3M18S")),
            new Track("9", ARTIST, "A Land Of Ruins", "2:42", Duration.parse("PT2M42S")),
            new Track("10", ARTIST, "Before Was Only Chaos", "2:16", Duration.parse("PT2M16S")),
            new Track("10_Alt", ARTIST, "Before Was Only Chaos", "2:16", Duration.parse("PT2M16S")),
            new Track("11", ARTIST, "All Else Is Decay", "2:53", Duration.parse("PT2M53S")),
            new Track("12", ARTIST, "The Worlds Of My Garden Are Many", "2:49", Duration.parse("PT2M49S")),
            new Track("12_Alt", ARTIST, "The Worlds Of My Garden Are Many", "2:49", Duration.parse("PT2M49S")),
            new Track("13", ARTIST, "The Temple Of The Sands", "2:00", Duration.parse("PT2M")),
            new Track("14", ARTIST, "The Guardians", "2:20", Duration.parse("PT2M20S")),
            new Track("14_Alt", ARTIST, "The Guardians", "2:20", Duration.parse("PT2M20S")),
            new Track("15", ARTIST, "The Dance Of Eternity", "1:24", Duration.parse("PT1M24S")),
            new Track("16", ARTIST, "Your Wisdom Grows", "2:09", Duration.parse("PT2M9S")),
            new Track("17", ARTIST, "Blessed And Beloved", "1:49", Duration.parse("PT1M49S")),
            new Track("17 Alt", ARTIST, "Blessed And Beloved", "1:49", Duration.parse("PT1M49S")),
            new Track("18", ARTIST, "A Land Of Tombs", "2:16", Duration.parse("PT2M16S")),
            new Track("19", ARTIST, "To Seek Salvation", "2:21", Duration.parse("PT2M21S")),
            new Track("20", ARTIST, "Do With It As You Will", "2:09", Duration.parse("PT2M9S")),
            new Track("20 Alt", ARTIST, "Do With It As You Will", "2:09", Duration.parse("PT2M9S")),
            new Track("21", ARTIST, "Virgo Serena", "1:31", Duration.parse("PT1M31S")),
            new Track("22", ARTIST, "The Forbidden Tower", "3:44", Duration.parse("PT3M44S")),
            new Track("22_Alt", ARTIST, "The Forbidden Tower", "3:44", Duration.parse("PT3M44S")),
            new Track("23", ARTIST, "Out There", "2:49", Duration.parse("PT2M49S")),
            new Track("24", ARTIST, "Heavenly Clouds", "2:40", Duration.parse("PT2M40S")),
            new Track("24", ARTIST, "Heavenly Clouds", "2:40", Duration.parse("PT2M40S")),
            new Track("25", ARTIST, "False God", "3:57", Duration.parse("PT3M57S")),
            new Track("26", ARTIST, "Blessed Messenger", "0:58", Duration.parse("PT58S")),
            new Track("27", ARTIST, "The End Of The Process (Finale)", "3:29", Duration.parse("PT3M29S")),
            new Track("28", ARTIST, "Welcome to Gehenna", "1:44", Duration.parse("PT1M44S")),
            new Track("29", ARTIST, "Worlds Apart", "5:00", Duration.parse("PT5M")),
            new Track("30", ARTIST, "The Fall Of Gehenna (Finale)", "1:05", Duration.parse("PT1M5S"))
    );

    @Override
    protected String getReleaseFileName() {
        return "release-13264226.json";
    }

    @Override
    protected Collection<Track> getExpectedTracksInRelease() {
        return EXPECTED_TRACKS_IN_RELEASE;
    }

    @Override
    protected Collection<ReleasePart> getExpectedPartsInRelease() {
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
