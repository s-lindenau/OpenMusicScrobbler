package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.MusicReleaseBasicInformation;
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

    private static final int RELEASE_ID = 0;

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
        MusicReleaseBasicInformation emptyBasicInformationForReleaseId = getMusicReleaseBasicInformation();
        ReleaseCollection releaseCollection = new ReleaseCollection(Collections.singleton(emptyBasicInformationForReleaseId));
        MusicRelease release = getService().getRelease(releaseCollection, RELEASE_ID);
        verifyRelease(release);
    }

    private MusicReleaseBasicInformation getMusicReleaseBasicInformation() {
        return new MusicReleaseBasicInformation(RELEASE_ID, RELEASE_ID, null, null, null, null);
    }

    protected abstract List<Track> getExpectedTracksInRelease();

    protected abstract String getReleaseArtist();

    protected abstract String getReleaseTitle();

    protected void verifyRelease(MusicRelease release) {
        Assertions.assertEquals(getReleaseArtist(), release.basicInformation().artist(), "Release artist mismatch");
        Assertions.assertEquals(getReleaseTitle(), release.basicInformation().title(), "Release title mismatch");

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
