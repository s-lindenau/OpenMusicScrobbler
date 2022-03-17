package nl.slindenau.openmusicscrobbler.discogs.client;

import com.adamdonegan.Discogs4J.client.DiscogsClient;
import nl.slindenau.openmusicscrobbler.test.ResourceFileReader;

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
    private String userCollectionFileName;
    private String userCollectionFolderFileName;
    private String userCollectionFolderReleasesFileName;

    private final DiscogsClient clientMock;

    public DiscogsClientMock() {
        this.clientMock = mock(DiscogsClient.class);
        this.resourceFileReader = new ResourceFileReader();
        setupUserCollection();
        setupFolder();
        setupReleases();
    }

    private void setupUserCollection() {
        when(clientMock.collection(any())).thenAnswer(invocation -> getFileContents(userCollectionFileName));
    }

    private void setupFolder() {
        when(clientMock.collectionFolder(any(), any())).thenAnswer(invocation -> getFileContents(userCollectionFolderFileName));
    }

    private void setupReleases() {
        when(clientMock.collectionReleases(any(), any())).thenAnswer(invocation -> getFileContents(userCollectionFolderReleasesFileName));
    }

    public DiscogsClient getClient() {
        return clientMock;
    }

    public void setUserCollectionFileName(String userCollectionFileName) {
        this.userCollectionFileName = userCollectionFileName;
    }

    public void setUserCollectionFolderFileName(String userCollectionFolderFileName) {
        this.userCollectionFolderFileName = userCollectionFolderFileName;
    }

    public void setUserCollectionFolderReleasesFileName(String userCollectionFolderReleasesFileName) {
        this.userCollectionFolderReleasesFileName = userCollectionFolderReleasesFileName;
    }

    private String getFileContents(String fileName) {
        return resourceFileReader.readFileContents(this.getClass(), fileName);
    }
}