package nl.slindenau.openmusicscrobbler.web.api;

import com.codahale.metrics.annotation.Timed;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.lastfm.model.LastFmScrobbleResultHolder;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.DiscogsService;
import nl.slindenau.openmusicscrobbler.service.ScrobbleService;
import nl.slindenau.openmusicscrobbler.service.search.SearchService;
import nl.slindenau.openmusicscrobbler.util.OptionalString;
import nl.slindenau.openmusicscrobbler.web.model.ScrobbleRequest;
import nl.slindenau.openmusicscrobbler.web.model.ScrobbleResult;
import nl.slindenau.openmusicscrobbler.web.view.ReleaseCollectionView;
import nl.slindenau.openmusicscrobbler.web.view.ReleaseView;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author davidvollmar
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@Path("/collection")
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8", MediaType.TEXT_HTML + ";charset=UTF-8"})
public class CollectionResource {

    private final ApplicationProperties applicationProperties = new ApplicationProperties();
    //todo: add dependency injection
    private final ScrobbleService scrobbleService = new ScrobbleService();
    private final DiscogsService discogsService = new DiscogsService();
    private final SearchService searchService = new SearchService();

    @GET
    @Timed
    @Consumes({MediaType.APPLICATION_JSON})
    public ReleaseCollection getCollectionAsJson() {
        // todo: check if we need this endpoint as JSON
        return getUserCollection();
    }

    @GET
    @Timed
    public ReleaseCollectionView getCollectionAsView() {
        return new ReleaseCollectionView(getUserCollection());
    }

    @GET
    @Path("/search")
    public ReleaseCollectionView getSearchView(@QueryParam("query") String searchQueryParameter) {
        return OptionalString.ofNullableOrBlank(searchQueryParameter)
                .map(this::getReleaseCollectionViewMatchingSearchQuery)
                .orElseGet(this::getEmptyReleaseCollectionView);
    }

    @GET
    @Path("/release")
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public ReleaseView getReleaseAsView(@QueryParam("id") Optional<Long> discogsId) {
        if (discogsId.isPresent()) {
            return new ReleaseView(findMusicRelease(discogsId.get()));
        } else {
            throw new IllegalArgumentException("Missing ID query parameter");
        }
    }

    @POST
    @Path("/scrobble")
    public ScrobbleResult scrobble(@Valid @BeanParam ScrobbleRequest scrobbleRequest) {
        LastFmScrobbleResultHolder resultHolder = scrobbleRelease(scrobbleRequest);
        return new ScrobbleResult(resultHolder.isSuccess(), resultHolder.getMessage());
    }

    private LastFmScrobbleResultHolder scrobbleRelease(ScrobbleRequest scrobbleRequest) {
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

    private ReleaseCollectionView getEmptyReleaseCollectionView() {
        ReleaseCollection emptyCollection = new ReleaseCollection(Collections.emptyList());
        return new ReleaseCollectionView(emptyCollection, ReleaseCollectionView.EMPTY_SEARCH);
    }

    private ReleaseCollectionView getReleaseCollectionViewMatchingSearchQuery(String searchQuery) {
        ReleaseCollection releaseCollectionMatchingSearch = findReleaseCollectionMatchingQuery(searchQuery);
        return new ReleaseCollectionView(releaseCollectionMatchingSearch, searchQuery);
    }

    private ReleaseCollection findReleaseCollectionMatchingQuery(String searchQuery) {
        searchService.loadCollection(getUserCollection());
        return searchService.findMatching(searchQuery);
    }

    private MusicRelease findMusicRelease(long discogsId) {
        return discogsService.getReleaseByDiscogsId(getUserCollection(), Math.toIntExact(discogsId));
    }

    private ReleaseCollection getUserCollection() {
        return discogsService.getUserCollection(getDiscogsUsername());
    }

    private String getDiscogsUsername() {
        return applicationProperties.getDiscogsUsername();
    }
}
