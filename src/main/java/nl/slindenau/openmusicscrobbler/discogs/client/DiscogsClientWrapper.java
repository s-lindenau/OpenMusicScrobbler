package nl.slindenau.openmusicscrobbler.discogs.client;

import com.adamdonegan.Discogs4J.client.DiscogsClient;
import com.adamdonegan.Discogs4J.models.AuthenticationType;
import nl.slindenau.openmusicscrobbler.discogs.json.JsonParser;
import nl.slindenau.openmusicscrobbler.discogs.model.Pagination;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.CollectionFolder;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.CollectionReleases;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.UserCollection;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Release;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsClientWrapper {

    private final Logger logger = LoggerFactory.getLogger(DiscogsClientWrapper.class);

    private final DiscogsClient discogsClient;
    private final DiscogsPaginationParameterFactory discogsPagination;
    private final JsonParser jsonParser;

    public DiscogsClientWrapper(DiscogsClient discogsClient) {
        this.discogsClient = discogsClient;
        this.discogsPagination = new DiscogsPaginationParameterFactory();
        this.jsonParser = new JsonParser();
    }

    public UserCollection getUserCollection(String userName) {
        String userCollection = discogsClient.collection(userName);
        debugLog(userCollection);
        return jsonParser.parseJsonObject(userCollection, UserCollection.class);
    }

    public CollectionFolder getFolder(String username, String folderId) {
        String folder = discogsClient.collectionFolder(username, folderId);
        debugLog(folder);
        return jsonParser.parseJsonObject(folder, CollectionFolder.class);
    }

    public CollectionReleases getReleases(String username, String folderId) {
        String collectionReleases = discogsClient.collectionReleases(username, folderId);
        debugLog(collectionReleases);
        return jsonParser.parseJsonObject(collectionReleases, CollectionReleases.class);
    }

    public CollectionReleases getReleases(String username, String folderId, Pagination pagination, int pageNumber) {
        Map<String, String> pageParameters = getPageParameters(pagination, pageNumber);
        String collectionReleases = discogsClient.collectionReleases(username, folderId, pageParameters);
        debugLog(collectionReleases);
        return jsonParser.parseJsonObject(collectionReleases, CollectionReleases.class);
    }

    public Release getRelease(String releaseId) {
        String release = discogsClient.release(releaseId);
        debugLog(release);
        return jsonParser.parseJsonObject(release, Release.class);
    }

    public boolean isUsingAuthentication() {
        return !AuthenticationType.NONE.equals(discogsClient.getAuthenticationType());
    }

    private Map<String, String> getPageParameters(Pagination pagination, int pageNumber) {
        return discogsPagination.getPageParameters(pagination, pageNumber);
    }

    private void debugLog(String debugInfoText) {
        if(discogsClient.isDebugEnabled()) {
            logger.info(debugInfoText);
        }
    }
}
