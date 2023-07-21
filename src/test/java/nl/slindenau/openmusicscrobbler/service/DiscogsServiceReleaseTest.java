package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.MusicReleaseBasicInformation;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Collection;
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
    private static final int DISCOGS_ID = 1;

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

        MusicRelease discogsRelease = getService().getReleaseByDiscogsId(releaseCollection, DISCOGS_ID);
        Assertions.assertEquals(release.basicInformation().id(), discogsRelease.basicInformation().id(), "Release by discogs ID does not match release by ID");
    }

    private MusicReleaseBasicInformation getMusicReleaseBasicInformation() {
        return new MusicReleaseBasicInformation(RELEASE_ID, DISCOGS_ID, null, null, null, null, null);
    }

    protected ReleasePart createReleasePart(String identification, List<Track> tracks) {
        return new ReleasePart(identification, Collections.emptyList(), tracks);
    }

    @SafeVarargs
    protected final List<Track> combineList(List<Track>... trackLists) {
        List<Track> allTracks = new ArrayList<>();
        for (List<Track> trackList : trackLists) {
            allTracks.addAll(trackList);
        }
        return allTracks;
    }

    protected abstract Collection<Track> getExpectedTracksInRelease();

    protected abstract Collection<ReleasePart> getExpectedPartsInRelease();

    protected abstract String getReleaseArtist();

    protected abstract String getReleaseTitle();

    protected void verifyRelease(MusicRelease release) {
        Assertions.assertEquals(getReleaseArtist(), release.basicInformation().artist(), "Release artist mismatch");
        Assertions.assertEquals(getReleaseTitle(), release.basicInformation().title(), "Release title mismatch");

        // check that all returned tracks are in our expected list
        release.getAllTracks().forEach(actualTrack -> assertTrackIsExpected(actualTrack, release));
        // check that all our expected tracks are in the actual list
        getExpectedTracksInRelease().forEach(expectedTrack -> assertTrackIsInActual(expectedTrack, release));
        // same for parts
        release.releaseParts().forEach(actualReleasePart -> assertReleasePartIsExpected(actualReleasePart, release));
        getExpectedPartsInRelease().forEach(expectedReleasePart -> assertReleasePartIsInActual(expectedReleasePart, release));
    }

    private void assertTrackIsExpected(Track actualTrack, MusicRelease release) {
        boolean expectedTracksContainsActualTrack = getExpectedTracksInRelease().contains(actualTrack);
        debugPrintTracks(expectedTracksContainsActualTrack, release);
        Assertions.assertTrue(expectedTracksContainsActualTrack, "Track not expected: " + actualTrack);
    }

    private void assertTrackIsInActual(Track expectedTrack, MusicRelease release) {
        boolean releaseContainsExpectedTrack = release.getAllTracks().contains(expectedTrack);
        debugPrintTracks(releaseContainsExpectedTrack, release);
        Assertions.assertTrue(releaseContainsExpectedTrack, "Expected track not found: " + expectedTrack);
    }

    private void assertReleasePartIsExpected(ReleasePart actualReleasePart, MusicRelease release) {
        boolean expectedPartsContainsActualReleasePart = getExpectedPartsInRelease().contains(actualReleasePart);
        debugPrintReleaseParts(expectedPartsContainsActualReleasePart, release);
        Assertions.assertTrue(expectedPartsContainsActualReleasePart, "ReleasePart not expected: " + actualReleasePart);
    }

    private void assertReleasePartIsInActual(ReleasePart expectedReleasePart, MusicRelease release) {
        boolean releaseContainsExpectedReleasePart = release.releaseParts().contains(expectedReleasePart);
        debugPrintReleaseParts(releaseContainsExpectedReleasePart, release);
        Assertions.assertTrue(releaseContainsExpectedReleasePart, "Expected ReleasePart not found: " + expectedReleasePart);
    }

    private void debugPrintTracks(boolean checkSucceeded, MusicRelease release) {
        if (!checkSucceeded) {
            System.out.println("Expected:");
            getExpectedTracksInRelease().forEach(System.out::println);
            System.out.println();
            System.out.println("Actual:");
            release.getAllTracks().forEach(System.out::println);
        }
    }

    private void debugPrintReleaseParts(boolean checkSucceeded, MusicRelease release) {
        if (!checkSucceeded) {
            System.out.println("Expected:");
            getExpectedPartsInRelease().forEach(System.out::println);
            System.out.println();
            System.out.println("Actual:");
            release.releaseParts().forEach(System.out::println);
        }
    }
}
