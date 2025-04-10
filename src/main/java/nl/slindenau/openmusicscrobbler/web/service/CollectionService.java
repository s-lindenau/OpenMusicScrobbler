package nl.slindenau.openmusicscrobbler.web.service;

import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.lastfm.model.LastFmScrobbleResultHolder;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.DiscogsService;
import nl.slindenau.openmusicscrobbler.service.ScrobbleService;
import nl.slindenau.openmusicscrobbler.service.search.SearchService;
import nl.slindenau.openmusicscrobbler.web.model.ScrobbleRequest;
import nl.slindenau.openmusicscrobbler.web.view.ReleaseCollectionView;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class CollectionService {

    //todo: add dependency injection
    private final ApplicationProperties applicationProperties = new ApplicationProperties();
    private final ScrobbleService scrobbleService = new ScrobbleService();
    private final DiscogsService discogsService = new DiscogsService();
    private final SearchService searchService = new SearchService();

    public LastFmScrobbleResultHolder scrobbleRelease(ScrobbleRequest scrobbleRequest) {
        MusicRelease releaseToScrobble = findMusicRelease(scrobbleRequest.discogsId);
        Collection<Track> tracksToScrobble = getTracksToScrobble(releaseToScrobble, scrobbleRequest);
        Instant firstTrackScrobbleAt = scrobbleService.getFirstTrackScrobbleDateRelativeTo(tracksToScrobble, scrobbleRequest.lastTrackEndedAt);
        return scrobbleService.scrobbleTracks(releaseToScrobble, firstTrackScrobbleAt, tracksToScrobble);
    }

    private Collection<Track> getTracksToScrobble(MusicRelease releaseToScrobble, ScrobbleRequest scrobbleRequest) {
        Collection<Track> allTracks = releaseToScrobble.getAllTracks();
        return allTracks.stream()
                .filter(trackIsSelectedInRequest(scrobbleRequest))
                .toList();
    }

    private Predicate<Track> trackIsSelectedInRequest(ScrobbleRequest scrobbleRequest) {
        return track -> scrobbleRequest.selectedTracks.contains(track.position());
    }

    public ReleaseCollectionView getEmptyReleaseCollectionView() {
        ReleaseCollection emptyCollection = new ReleaseCollection(Collections.emptyList());
        return new ReleaseCollectionView(emptyCollection, ReleaseCollectionView.EMPTY_SEARCH);
    }

    public ReleaseCollectionView getReleaseCollectionViewMatchingSearchQuery(String searchQuery) {
        ReleaseCollection releaseCollectionMatchingSearch = findReleaseCollectionMatchingQuery(searchQuery);
        return new ReleaseCollectionView(releaseCollectionMatchingSearch, searchQuery);
    }

    private ReleaseCollection findReleaseCollectionMatchingQuery(String searchQuery) {
        searchService.loadCollection(getUserCollection());
        return searchService.findMatching(searchQuery);
    }

    public MusicRelease findMusicRelease(long discogsId) {
        return discogsService.getReleaseByDiscogsId(getUserCollection(), Math.toIntExact(discogsId));
    }

    public ReleaseCollection getUserCollection() {
        return discogsService.getUserCollection(getDiscogsUsername());
    }

    private String getDiscogsUsername() {
        return applicationProperties.getDiscogsUsername();
    }
}
