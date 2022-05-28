package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import nl.slindenau.openmusicscrobbler.model.Track;
import org.junit.jupiter.api.Assertions;

import java.util.Collections;
import java.util.List;

/**
 * Base class for testing a specific release (with track listing)
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public abstract class DiscogsServiceReleaseTest extends DiscogsServiceTest {

    @Override
    protected String getUserCollectionFolderReleasesFileName() {
        return null;
    }

    @Override
    protected String getUserCollectionFolderFileName() {
        return null;
    }

    @Override
    protected String getUserCollectionFileName() {
        return null;
    }

    @Override
    protected void runDiscogsServiceTest() {
        MusicRelease fakeReleaseForDiscogsId = new MusicRelease(0, 0, null, null, null, Collections.emptyList());
        ReleaseCollection releaseCollection = new ReleaseCollection(Collections.singleton(fakeReleaseForDiscogsId));
        MusicRelease release = getService().getRelease(releaseCollection, 0);
        verifyRelease(release);
    }

    protected abstract List<Track> getExpectedTracksInRelease();

    protected abstract String getReleaseArtist();

    protected abstract String getReleaseTitle();

    protected void verifyRelease(MusicRelease release) {
        Assertions.assertEquals(getReleaseArtist(), release.artist(), "Release artist mismatch");
        Assertions.assertEquals(getReleaseTitle(), release.title(), "Release title mismatch");

        // check that all returned tracks are in our expected list
        release.getAllTracks().forEach(actualTrack -> assertTrackIsExpected(actualTrack, release));
        // check that all our expected tracks are in the actual list
        getExpectedTracksInRelease().forEach(expectedTrack -> assertTrackIsInActual(expectedTrack, release));
    }

    private void assertTrackIsInActual(Track expectedTrack, MusicRelease release) {
        boolean releaseContainsExpectedTrack = release.getAllTracks().contains(expectedTrack);
        debugPrint(releaseContainsExpectedTrack, release);
        Assertions.assertTrue(releaseContainsExpectedTrack, "Expected track not found: " + expectedTrack);
    }

    private void assertTrackIsExpected(Track actualTrack, MusicRelease release) {
        boolean expectedTracksContainsActualTrack = getExpectedTracksInRelease().contains(actualTrack);
        debugPrint(expectedTracksContainsActualTrack, release);
        Assertions.assertTrue(expectedTracksContainsActualTrack, "Track not expected: " + actualTrack);
    }

    private void debugPrint(boolean checkSucceeded, MusicRelease release) {
        if (!checkSucceeded) {
            System.out.println("Expected:");
            getExpectedTracksInRelease().forEach(System.out::println);
            System.out.println();
            System.out.println("Actual:");
            release.getAllTracks().forEach(System.out::println);
        }
    }
}
