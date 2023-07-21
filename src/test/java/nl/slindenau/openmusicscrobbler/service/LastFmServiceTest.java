package nl.slindenau.openmusicscrobbler.service;

import de.umass.lastfm.scrobble.ScrobbleResult;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientFactory;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientSupplier;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientWrapper;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.MusicReleaseBasicInformation;
import nl.slindenau.openmusicscrobbler.model.Track;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@ExtendWith(MockitoExtension.class)
class LastFmServiceTest {

    private static final int LAST_FM_MAX_HISTORIC_SCROBBLE_WEEKS = 2;
    private static final int DAYS_PER_WEEK = 7;

    private final Duration track1Duration = Duration.ofSeconds(60 + 60 + 45);
    private final Duration track2Duration = Duration.ofSeconds(60 + 15);
    private Track track1, track2;

    private LastFmService lastFmService;

    @Mock
    private LastFmClientFactory clientFactory;

    @Mock
    private LastFmClientWrapper clientWrapper;

    @BeforeEach
    void setUp() {
        setupTracks();
        setupClient();
        this.lastFmService = new LastFmService(clientFactory);
    }

    private void setupClient() {
        // lenient because the client is not used in all cases
        Mockito.lenient().when(clientFactory.getClient()).thenReturn(clientWrapper);
        Mockito.lenient().when(clientWrapper.scrobbleTrack(any(), any(), any(), any())).thenAnswer(invocation -> mock(ScrobbleResult.class));
    }

    private void setupTracks() {
        this.track1 = new Track("1", "Artist", "Title", "not used", track1Duration);
        this.track2 = new Track("2", "Artist", "Title", "not used", track2Duration);
    }

    @Test
    void testGetLastFmClientMultipleCallsSameResult() {
        LastFmClientSupplier clientSupplier = getClientSupplier();
        LastFmClientWrapper client = clientSupplier.getClient();
        Assertions.assertEquals(clientWrapper, client, "ClientSupplier should return the clientWrapper from the clientFactory");
        LastFmClientWrapper client2 = clientSupplier.getClient();
        Assertions.assertEquals(client, client2, "Multiple calls to getClient should result in the same object (cached)");
        // verify that the expensive operation of constructing the actual client is done only once
        verify(clientFactory, times(1)).getClient();
    }

    @Test
    void testScrobbleTracksTooFarInPast() {
        int numberOfDaysBeyondLastFmHistoricLimit = (LAST_FM_MAX_HISTORIC_SCROBBLE_WEEKS + 1) * DAYS_PER_WEEK;
        Instant firstTrackStartedAt = Instant.now().minus(numberOfDaysBeyondLastFmHistoricLimit, ChronoUnit.DAYS);
        assertThrows(() -> scrobbleTracks(firstTrackStartedAt), "Expected exception on scrobble too far in the past");
    }

    @Test
    void testScrobbleTracksInFuture() {
        Instant firstTrackStartedAt = Instant.now().plus(getExpectedTracksDuration());
        assertThrows(() -> scrobbleTracks(firstTrackStartedAt), "Expected exception on scrobble in the future");
    }

    @Test
    void testScrobbleTracks() {
        Instant firstTrackStartedAt = Instant.now().minus(getExpectedTracksDuration());
        scrobbleTracks(firstTrackStartedAt);
    }

    private void scrobbleTracks(Instant firstTrackStartedAt) {
        MusicRelease release = setupMusicRelease();
        List<Track> tracks = getTracks();
        lastFmService.scrobbleTracks(release, firstTrackStartedAt, tracks, getClientSupplier());
        Assertions.assertFalse(tracks.isEmpty());
        verify(clientWrapper, times(tracks.size())).scrobbleTrack(any(), any(), any(), any());
    }

    private MusicRelease setupMusicRelease() {
        MusicReleaseBasicInformation information = new MusicReleaseBasicInformation(0, 0, "Artist", "Title", "Format", 0, "Thumb");
        return new MusicRelease(information, Collections.emptyList());
    }

    @Test
    void testGetTotalPlayTimeInSeconds() {
        long totalPlayTimeInSeconds = lastFmService.getTotalPlayTimeInSeconds(getTracks());
        long expectedDuration = getExpectedTracksDuration().toSeconds();
        Assertions.assertEquals(expectedDuration, totalPlayTimeInSeconds, "TotalPlayTimeInSeconds does not match combined track length");
    }

    private Duration getExpectedTracksDuration() {
        return track1Duration.plus(track2Duration);
    }

    private List<Track> getTracks() {
        return Arrays.asList(track1, track2);
    }

    private LastFmClientSupplier getClientSupplier() {
        return lastFmService.getLastFmClient();
    }

    private void assertThrows(Executable executable, String message) {
        Assertions.assertThrows(OpenMusicScrobblerException.class, executable, message);
    }
}
