package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientFactory;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientMock;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.mockito.Mockito.when;

/**
 * Base class for all {@link DiscogsService} tests
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@ExtendWith(MockitoExtension.class)
public abstract class DiscogsServiceTest {

    protected static final Duration DEFAULT_TRACK_LENGTH = Duration.ofMinutes(1);

    private DiscogsService service;

    @Mock
    private DiscogsClientFactory discogsClientFactory;
    @Mock
    private ApplicationProperties applicationProperties;

    @BeforeEach
    void setUp() {
        setupDiscogsClientFactory();
        Mockito.lenient().when(applicationProperties.getDiscogsDefaultTrackLength()).thenReturn(DEFAULT_TRACK_LENGTH);
        MusicReleaseService musicReleaseService = new MusicReleaseService(applicationProperties);
        service = new DiscogsService(discogsClientFactory, musicReleaseService);
    }

    private void setupDiscogsClientFactory() {
        DiscogsClientMock clientMock = new DiscogsClientMock();
        clientMock.setUserCollectionFileName(getUserCollectionFileName());
        clientMock.setUserCollectionFolderFileName(getUserCollectionFolderFileName());
        clientMock.setUserCollectionFolderReleasesFileName(getUserCollectionFolderReleasesFileName());
        clientMock.setReleaseFileName(getReleaseFileName());
        DiscogsClientWrapper clientWrapper = new DiscogsClientWrapper(clientMock.getClient());
        when(discogsClientFactory.getClient()).thenReturn(clientWrapper);
    }

    protected abstract String getUserCollectionFolderReleasesFileName();

    protected abstract String getUserCollectionFolderFileName();

    protected abstract String getUserCollectionFileName();

    protected abstract String getReleaseFileName();

    @Test
    public void runTest() {
        runDiscogsServiceTest();
    }

    protected abstract void runDiscogsServiceTest();

    protected DiscogsService getService() {
        return service;
    }
}
