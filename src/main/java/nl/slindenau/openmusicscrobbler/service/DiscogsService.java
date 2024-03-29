package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientFactory;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientWrapper;
import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;
import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsArtistNameCollector;
import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsReleaseFormatCollector;
import nl.slindenau.openmusicscrobbler.discogs.model.Pagination;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.BasicInformation;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.CollectionFolder;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.CollectionRelease;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.CollectionReleases;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.UserCollection;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Release;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.MusicReleaseBasicInformation;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import nl.slindenau.openmusicscrobbler.util.OptionalString;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsService {

    private static final String DISCOGS_USER_COLLECTION_PUBLIC_FOLDER_ID = "0";

    private final Map<String, ReleaseCollection> releaseCollectionCache = new HashMap<>();
    private final Map<String, Release> releaseCache = new HashMap<>();
    private final DiscogsClientWrapper discogsClient;
    private final MusicReleaseService musicReleaseService;
    private int nextId = 1;

    public DiscogsService() {
        this(new DiscogsClientFactory(), new MusicReleaseService());
    }

    protected DiscogsService(DiscogsClientFactory discogsClientFactory, MusicReleaseService musicReleaseService) {
        this.discogsClient = discogsClientFactory.getClient();
        this.musicReleaseService = musicReleaseService;
    }

    public synchronized ReleaseCollection getUserCollection(String discogsUsername) {
        return releaseCollectionCache.computeIfAbsent(discogsUsername, this::findUserCollection);
    }

    private ReleaseCollection findUserCollection(String discogsUsername) {
        Collection<MusicReleaseBasicInformation> releasesInCollection = new LinkedList<>();
        UserCollection userCollection = this.discogsClient.getUserCollection(discogsUsername);
        checkError(userCollection);
        String folderId = DISCOGS_USER_COLLECTION_PUBLIC_FOLDER_ID;
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

    private void processReleases(CollectionReleases collectionReleases, Collection<MusicReleaseBasicInformation> releasesInCollection) {
        checkError(collectionReleases);
        collectionReleases.getReleases().stream()
                .map(this::createRelease)
                .forEach(releasesInCollection::add);
    }

    private MusicReleaseBasicInformation createRelease(CollectionRelease release) {
        int releaseId = release.getId();
        BasicInformation basicInformation = release.getBasicInformation();
        String title = basicInformation.getTitle();
        String format = getFormat(basicInformation);
        String artist = getArtist(basicInformation);
        Integer year = getYear(basicInformation);
        String thumbnail = getThumbnail(basicInformation);
        return new MusicReleaseBasicInformation(nextId++, releaseId, artist, title, format, year, thumbnail);
    }

    private String getFormat(BasicInformation basicInformation) {
        return new DiscogsReleaseFormatCollector().getFormat(basicInformation);
    }

    private String getArtist(BasicInformation basicInformation) {
        return basicInformation.getArtists().stream().collect(new DiscogsArtistNameCollector());
    }

    private Integer getYear(BasicInformation basicInformation) {
        return basicInformation.year == 0 ? null : basicInformation.year;
    }

    private String getThumbnail(BasicInformation basicInformation) {
        return OptionalString.ofNullableOrBlank(basicInformation.thumb).orElse(null);
    }

    public MusicRelease getRelease(ReleaseCollection userCollection, Integer releaseId) {
        return getReleaseById(userCollection::findById, releaseId);
    }

    public MusicRelease getReleaseByDiscogsId(ReleaseCollection userCollection, Integer discogsId) {
        return getReleaseById(userCollection::findByDiscogsId, discogsId);
    }

    private MusicRelease getReleaseById(Function<Integer, Optional<MusicReleaseBasicInformation>> findReleaseById, Integer releaseId) {
        Optional<MusicReleaseBasicInformation> release = findReleaseById.apply(releaseId);
        return release.map(this::getMusicReleaseTracks).orElseThrow(() -> new OpenMusicScrobblerException("Unknown release with id: " + releaseId));
    }

    private MusicRelease getMusicReleaseTracks(MusicReleaseBasicInformation musicRelease) {
        Release discogsRelease = getDiscogsRelease(musicRelease);
        return musicReleaseService.createRelease(musicRelease, discogsRelease);
    }

    private synchronized Release getDiscogsRelease(MusicReleaseBasicInformation release) {
        String releaseId = String.valueOf(release.discogsId());
        return releaseCache.computeIfAbsent(releaseId, this::createDiscogsRelease);
    }

    private Release createDiscogsRelease(String releaseId) {
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
