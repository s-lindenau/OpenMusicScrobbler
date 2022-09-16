package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.MusicReleaseBasicInformation;
import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@ExtendWith(MockitoExtension.class)
class ScrobbleServiceTest {

    private static final String ARTIST = "Artist";
    private static final String TITLE = "Title";
    private static final String FORMAT = "2x Vinyl";
    private static final Integer YEAR = 2000;
    private static final Instant SCROBBLE_AT = Instant.now();
    private static final int SCROBBLE_TRACK_LIST_METHOD_ARGUMENT_INDEX = 2;

    private static final List<Track> TRACKS_PART_1_A = Arrays.asList(
            new Track("A1", ARTIST, "Track 1", "1:00", Duration.ofMinutes(1)),
            new Track("A2", ARTIST, "Track 2", "2:00", Duration.ofMinutes(2))
    );

    private static final List<Track> TRACKS_PART_1_B = Arrays.asList(
            new Track("B1", ARTIST, "Track 3", "3:00", Duration.ofMinutes(3)),
            new Track("B2", ARTIST, "Track 4", "4:00", Duration.ofMinutes(4))
    );

    private static final List<Track> TRACKS_PART_2_C = Arrays.asList(
            new Track("C1", ARTIST, "Track 5", "5:00", Duration.ofMinutes(5)),
            new Track("C2", ARTIST, "Track 6", "6:00", Duration.ofMinutes(6))
    );

    private static final List<Track> TRACKS_PART_2_D = Arrays.asList(
            new Track("D1", ARTIST, "Track 7", "7:00", Duration.ofMinutes(7)),
            new Track("D2", ARTIST, "Track 8", "8:00", Duration.ofMinutes(8))
    );

    @Mock
    private LastFmService lastFmService;

    private Collection<Track> actualScrobbles;
    private ScrobbleService scrobbleService;
    private MusicRelease release;
    private ReleasePart releasePart1, releasePart2;
    private ReleasePart releasePartA, releasePartB;

    @BeforeEach
    void setUp() {
        scrobbleService = new ScrobbleService(lastFmService);
        actualScrobbles = new ArrayList<>();
        setupMusicRelease();
        setupLastFmService();
    }

    @Test
    void testScrobbleMusicRelease() {
        scrobbleService.scrobbleTracks(release, SCROBBLE_AT);
        verifyGetLastFmClientOnce();
        verifyScrobbleTracks(combineList(TRACKS_PART_1_A, TRACKS_PART_1_B, TRACKS_PART_2_C, TRACKS_PART_2_D));
    }

    @Test
    void testScrobbleReleaseParts() {
        scrobbleService.scrobbleTracks(release, SCROBBLE_AT, releasePart1);
        verifyGetLastFmClientOnce();
        verifyScrobbleTracks(combineList(TRACKS_PART_1_A, TRACKS_PART_1_B));
    }

    @Test
    void testScrobbleReleaseParts2() {
        scrobbleService.scrobbleTracks(release, SCROBBLE_AT, releasePart2);
        verifyGetLastFmClientOnce();
        verifyScrobbleTracks(combineList(TRACKS_PART_2_C, TRACKS_PART_2_D));
    }

    @Test
    void testScrobbleReleasePartsAB() {
        scrobbleService.scrobbleTracks(release, SCROBBLE_AT, releasePartA, releasePartB);
        verifyGetLastFmClientOnce();
        verifyScrobbleTracks(combineList(TRACKS_PART_1_A, TRACKS_PART_1_B));
    }

    @Test
    void testScrobbleTracks() {
        scrobbleService.scrobbleTracks(release, SCROBBLE_AT, TRACKS_PART_1_A);
        verifyGetLastFmClientOnce();
        verifyScrobbleTracks(TRACKS_PART_1_A);
    }

    private void verifyScrobbleTracks(List<Track> expectedTracks) {
        for (Track expectedTrack : expectedTracks) {
            boolean actualScrobbleContainsExpectedTrack = actualScrobbles.contains(expectedTrack);
            debugPrint(actualScrobbleContainsExpectedTrack, expectedTracks);
            Assertions.assertTrue(actualScrobbleContainsExpectedTrack, "Expected track was not found in actual scrobbles");
        }
        for (Track actualTrack : actualScrobbles) {
            boolean expectedTracksContainsActualScrobble = expectedTracks.contains(actualTrack);
            debugPrint(expectedTracksContainsActualScrobble, expectedTracks);
            Assertions.assertTrue(expectedTracksContainsActualScrobble, "Actual scrobble was not found in expected tracks");
        }
    }

    private void setupMusicRelease() {
        releasePartA = new ReleasePart("A", Collections.emptyList(), TRACKS_PART_1_A);
        releasePartB = new ReleasePart("B", Collections.emptyList(), TRACKS_PART_1_B);
        ReleasePart releasePartC = new ReleasePart("C", Collections.emptyList(), TRACKS_PART_2_C);
        ReleasePart releasePartD = new ReleasePart("D", Collections.emptyList(), TRACKS_PART_2_D);
        releasePart1 = new ReleasePart("Vinyl Record 1", Arrays.asList(releasePartA, releasePartB), Collections.emptyList());
        releasePart2 = new ReleasePart("Vinyl Record 2", Arrays.asList(releasePartC, releasePartD), Collections.emptyList());
        MusicReleaseBasicInformation basicInformation = new MusicReleaseBasicInformation(0, 0, ARTIST, TITLE, FORMAT, YEAR);
        release = new MusicRelease(basicInformation, Arrays.asList(releasePart1, releasePart2));
    }

    private void setupLastFmService() {
        doAnswer(this::storeActualTracks).when(lastFmService).scrobbleTracks(any(), any(), any(), any());
    }

    private Void storeActualTracks(InvocationOnMock invocationOnMock) {
        Collection<Track> scrobbles = invocationOnMock.getArgument(SCROBBLE_TRACK_LIST_METHOD_ARGUMENT_INDEX);
        this.actualScrobbles.addAll(scrobbles);
        return null;
    }

    private void verifyGetLastFmClientOnce() {
        verify(lastFmService, times(1)).getLastFmClient();
    }

    @SafeVarargs
    private List<Track> combineList(List<Track>... trackLists) {
        List<Track> allTracks = new ArrayList<>();
        for (List<Track> trackList : trackLists) {
            allTracks.addAll(trackList);
        }
        return allTracks;
    }

    private void debugPrint(boolean checkSucceeded, List<Track> expectedTracks) {
        if (!checkSucceeded) {
            System.out.println("Expected:");
            expectedTracks.forEach(System.out::println);
            System.out.println();
            System.out.println("Actual:");
            actualScrobbles.forEach(System.out::println);
        }
    }
}