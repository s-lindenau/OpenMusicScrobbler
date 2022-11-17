package nl.slindenau.openmusicscrobbler.lastfm.client;

import de.umass.lastfm.Session;
import de.umass.lastfm.scrobble.ScrobbleData;
import de.umass.lastfm.scrobble.ScrobbleResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@ExtendWith(MockitoExtension.class)
class LastFmClientWrapperTest {

    private static final String ARTIST = "Artist";
    private static final String ALBUM = "Album";
    private static final String TRACK = "Track";
    private static final Instant SCROBBLE_AT = Instant.now();

    private LastFmClientWrapper clientWrapper;

    @Captor
    private ArgumentCaptor<ScrobbleData> scrobbleDataArgumentCaptor;

    @Mock
    private Session session;

    @Mock
    private ScrobbleResult scrobbleResult;

    @Mock
    private LastFmClientFacade lastFmClientFacade;

    @BeforeEach
    void setUp() {
        clientWrapper = new LastFmClientWrapper(lastFmClientFacade, session);
        when(lastFmClientFacade.scrobbleTrack(any(), any())).thenReturn(scrobbleResult);
    }

    @Test
    void scrobbleTrack() {
        ScrobbleResult actualScrobbleResult = clientWrapper.scrobbleTrack(ARTIST, ALBUM, TRACK, SCROBBLE_AT);
        Assertions.assertEquals(scrobbleResult, actualScrobbleResult, "Scrobble result object mismatch");
        verify(lastFmClientFacade).scrobbleTrack(scrobbleDataArgumentCaptor.capture(), eq(session));
        ScrobbleData actualScrobbleData = scrobbleDataArgumentCaptor.getValue();

        Assertions.assertEquals(ARTIST, actualScrobbleData.getArtist(), "Artist mismatch");
        Assertions.assertEquals(ALBUM, actualScrobbleData.getAlbum(), "Album mismatch");
        Assertions.assertEquals(TRACK, actualScrobbleData.getTrack(), "Track mismatch");
        Assertions.assertEquals(SCROBBLE_AT.getEpochSecond(), actualScrobbleData.getTimestamp(), "Scrobble at mismatch");
    }
}