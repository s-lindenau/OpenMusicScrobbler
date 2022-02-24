package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.Constants;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientFactory;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientWrapper;
import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;
import nl.slindenau.openmusicscrobbler.discogs.model.Pagination;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.*;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Artist;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Format;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Release;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;

import java.util.*;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsService {
    private final Map<String, ReleaseCollection> releaseCollectionCache = new HashMap<>();
    private final DiscogsClientWrapper discogsClient;

    public DiscogsService() {
        this(new DiscogsClientFactory());
    }

    public DiscogsService(DiscogsClientFactory discogsClientFactory) {
        this.discogsClient = discogsClientFactory.getClient();
    }

    public ReleaseCollection getUserCollection(String discogsUsername) {
        return releaseCollectionCache.computeIfAbsent(discogsUsername, this::findUserCollection);
    }

    private ReleaseCollection findUserCollection(String discogsUsername) {
        Collection<MusicRelease> releasesInCollection = new LinkedList<>();
        UserCollection userCollection = this.discogsClient.getUserCollection(discogsUsername);
        checkError(userCollection);
        String folderId = Constants.DISCOGS_USER_COLLECTION_PUBLIC_FOLDER_ID;
        CollectionFolder folder = discogsClient.getFolder(discogsUsername, folderId);
        checkError(folder);
        CollectionReleases collectionReleases = discogsClient.getReleases(discogsUsername, folderId);
        processReleases(collectionReleases, releasesInCollection);
        Pagination pagination = collectionReleases.getPagination();
        for (int nextPage = 2; nextPage <= pagination.getPages(); nextPage++) {
            CollectionReleases collectionReleasesNextPage = discogsClient.getReleases(discogsUsername, folderId, pagination, nextPage);
            processReleases(collectionReleasesNextPage, releasesInCollection);
        }
        return new ReleaseCollection(releasesInCollection);
    }

    private void processReleases(CollectionReleases collectionReleases, Collection<MusicRelease> releasesInCollection) {
        checkError(collectionReleases);
        collectionReleases.getReleases().stream().map(this::createRelease).forEach(releasesInCollection::add);
    }

    private MusicRelease createRelease(CollectionRelease release) {
        int releaseId = release.getId();
        BasicInformation basicInformation = release.getBasicInformation();
        String title = basicInformation.getTitle();
        String format = basicInformation.getFormats().stream().findFirst().map(Format::getName).orElse("unknown format");
        String artist = basicInformation.getArtists().stream().findFirst().map(Artist::getName).orElse("unknown artist");
        return new MusicRelease(releaseId, artist, title, format, Collections.emptyList());
    }

    private void checkError(DiscogsApiResponse discogsApiResponse) {
        if (discogsApiResponse.isError()) {
            throw new OpenMusicScrobblerException(discogsApiResponse.getErrorMessage());
        }
    }

    public Release getRelease(Integer releaseId) {
        // todo change to model & cache
        return discogsClient.getRelease(String.valueOf(releaseId));
    }
}
