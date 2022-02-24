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
    private final Map<String, Release> releaseCache = new HashMap<>();
    private final DiscogsClientWrapper discogsClient;
    private final MusicReleaseService musicReleaseService;
    private int nextId = 0;

    public DiscogsService() {
        this(new DiscogsClientFactory(), new MusicReleaseService());
    }

    public DiscogsService(DiscogsClientFactory discogsClientFactory, MusicReleaseService musicReleaseService) {
        this.discogsClient = discogsClientFactory.getClient();
        this.musicReleaseService = musicReleaseService;
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
        collectionReleases.getReleases().stream().map(this::findRelease).forEach(releasesInCollection::add);
    }

    private MusicRelease findRelease(CollectionRelease release) {
        int releaseId = release.getId();
        BasicInformation basicInformation = release.getBasicInformation();
        String title = basicInformation.getTitle();
        String format = basicInformation.getFormats().stream().findFirst().map(Format::getName).orElse("unknown format");
        String artist = basicInformation.getArtists().stream().findFirst().map(Artist::getName).orElse("unknown artist");
        return new MusicRelease(nextId++, releaseId, artist, title, format, Collections.emptyList());
    }

    public MusicRelease getRelease(ReleaseCollection userCollection, Integer releaseId) {
        Optional<MusicRelease> release = userCollection.releases().stream().filter(musicRelease -> musicRelease.id() == releaseId).findFirst();
        return release.map(this::getMusicReleaseTracks).orElseThrow(() -> new OpenMusicScrobblerException("Unknown release with id: " + releaseId));
    }

    private MusicRelease getMusicReleaseTracks(MusicRelease musicRelease) {
        Release discogsRelease = getDiscogsRelease(musicRelease);
        return musicReleaseService.createRelease(musicRelease, discogsRelease);
    }

    private Release getDiscogsRelease(MusicRelease release) {
        String releaseId = String.valueOf(release.discogsId());
        return releaseCache.computeIfAbsent(releaseId, this::findRelease);
    }

    private Release findRelease(String releaseId) {
        Release release = discogsClient.getRelease(releaseId);
        checkError(release);
        return release;
    }

    private void checkError(DiscogsApiResponse discogsApiResponse) {
        if (discogsApiResponse.isError()) {
            throw new OpenMusicScrobblerException(discogsApiResponse.getErrorMessage());
        }
    }
}
