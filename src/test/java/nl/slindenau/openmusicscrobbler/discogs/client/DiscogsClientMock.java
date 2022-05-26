package nl.slindenau.openmusicscrobbler.discogs.client;

import com.adamdonegan.Discogs4J.client.DiscogsClient;
import nl.slindenau.openmusicscrobbler.util.ResourceFileReader;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsClientMock {

    private final ResourceFileReader resourceFileReader;
    private final DiscogsClient clientMock;

    public DiscogsClientMock() {
        this.clientMock = mock(DiscogsClient.class);
        this.resourceFileReader = new ResourceFileReader();
    }

    public DiscogsClient getClient() {
        return clientMock;
    }

    public void setUserCollectionFileName(String userCollectionFileName) {
        if (userCollectionFileName != null) {
            setupUserCollection(userCollectionFileName);
        }
    }

    public void setUserCollectionFolderFileName(String userCollectionFolderFileName) {
        if (userCollectionFolderFileName != null) {
            setupFolder(userCollectionFolderFileName);
        }
    }

    public void setUserCollectionFolderReleasesFileName(String userCollectionFolderReleasesFileName) {
        if (userCollectionFolderReleasesFileName != null) {
            setupReleases(userCollectionFolderReleasesFileName);
        }
    }

    public void setReleaseFileName(String releaseFileName) {
        if (releaseFileName != null) {
            setupRelease(releaseFileName);
        }
    }

    private void setupUserCollection(String userCollectionFileName) {
        when(clientMock.collection(any())).thenAnswer(invocation -> getFileContents(userCollectionFileName));
    }

    private void setupFolder(String userCollectionFolderFileName) {
        when(clientMock.collectionFolder(any(), any())).thenAnswer(invocation -> getFileContents(userCollectionFolderFileName));
    }

    private void setupReleases(String userCollectionFolderReleasesFileName) {
        when(clientMock.collectionReleases(any(), any())).thenAnswer(invocation -> getFileContents(userCollectionFolderReleasesFileName));
    }

    private void setupRelease(String releaseFileName) {
        when(clientMock.release(any())).thenAnswer(invocation -> getFileContents(releaseFileName));
    }

    private String getFileContents(String fileName) {
        return resourceFileReader.readFileContents(this.getClass(), fileName);
    }
}