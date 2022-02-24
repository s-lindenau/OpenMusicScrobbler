package nl.slindenau.openmusicscrobbler.discogs.client;

import com.adamdonegan.Discogs4J.client.DiscogsClient;
import nl.slindenau.openmusicscrobbler.discogs.json.JsonParser;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.CollectionFolder;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.CollectionReleases;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.UserCollection;
import nl.slindenau.openmusicscrobbler.test.ResourceFileReader;

import static org.mockito.Mockito.mock;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsClientWrapperMock extends DiscogsClientWrapper {

    private final ResourceFileReader resourceFileReader;
    private final JsonParser jsonParser;
    private String userCollectionFileName;
    private String userCollectionFolderFileName;
    private String userCollectionFolderReleasesFileName;

    public DiscogsClientWrapperMock() {
        super(mock(DiscogsClient.class));
        this.resourceFileReader = new ResourceFileReader();
        this.jsonParser = new JsonParser();
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

    @Override
    public UserCollection getUserCollection(String userName) {
        String jsonFileContents = getFileContents(userCollectionFileName);
        return jsonParser.parseJsonObject(jsonFileContents, UserCollection.class);
    }

    @Override
    public CollectionFolder getFolder(String username, String folderId) {
        String jsonFileContents = getFileContents(userCollectionFolderFileName);
        return jsonParser.parseJsonObject(jsonFileContents, CollectionFolder.class);
    }

    @Override
    public CollectionReleases getReleases(String username, String folderId) {
        String jsonFileContents = getFileContents(userCollectionFolderReleasesFileName);
        return jsonParser.parseJsonObject(jsonFileContents, CollectionReleases.class);
    }

    private String getFileContents(String fileName) {
        return resourceFileReader.readFileContents(this.getClass(), fileName);
    }
}