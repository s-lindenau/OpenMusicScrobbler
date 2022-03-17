package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientFactory;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientMock;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private DiscogsService service;

    @Mock
    private DiscogsClientFactory discogsClientFactory;

    @BeforeEach
    void setUp() {
        setupDiscogsClientFactory();
        MusicReleaseService musicReleaseService = new MusicReleaseService();
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
