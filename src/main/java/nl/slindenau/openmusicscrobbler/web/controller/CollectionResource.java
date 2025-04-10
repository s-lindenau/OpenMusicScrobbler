package nl.slindenau.openmusicscrobbler.web.controller;

import com.codahale.metrics.annotation.Timed;
import nl.slindenau.openmusicscrobbler.lastfm.model.LastFmScrobbleResultHolder;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import nl.slindenau.openmusicscrobbler.util.OptionalString;
import nl.slindenau.openmusicscrobbler.web.model.ScrobbleRequest;
import nl.slindenau.openmusicscrobbler.web.model.ScrobbleResult;
import nl.slindenau.openmusicscrobbler.web.service.CollectionService;
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
import java.util.Optional;

/**
 * @author davidvollmar
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@Path("/collection")
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8", MediaType.TEXT_HTML + ";charset=UTF-8"})
public class CollectionResource {

    //todo: add dependency injection
    private final CollectionService collectionService = new CollectionService();

    @GET
    @Timed
    @Consumes({MediaType.APPLICATION_JSON})
    public ReleaseCollection getCollectionAsJson() {
        // todo: check if we need this endpoint as JSON
        return collectionService.getUserCollection();
    }

    @GET
    @Timed
    public ReleaseCollectionView getCollectionAsView() {
        return new ReleaseCollectionView(collectionService.getUserCollection());
    }

    @GET
    @Path("/search")
    public ReleaseCollectionView getSearchView(@QueryParam("query") String searchQueryParameter) {
        return OptionalString.ofNullableOrBlank(searchQueryParameter)
                .map(collectionService::getReleaseCollectionViewMatchingSearchQuery)
                .orElseGet(collectionService::getEmptyReleaseCollectionView);
    }

    @GET
    @Path("/release")
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public ReleaseView getReleaseAsView(@QueryParam("id") Optional<Long> discogsId) {
        if (discogsId.isPresent()) {
            return new ReleaseView(collectionService.findMusicRelease(discogsId.get()));
        } else {
            throw new IllegalArgumentException("Missing ID query parameter");
        }
    }

    @POST
    @Path("/scrobble")
    public ScrobbleResult scrobble(@Valid @BeanParam ScrobbleRequest scrobbleRequest) {
        LastFmScrobbleResultHolder resultHolder = collectionService.scrobbleRelease(scrobbleRequest);
        return new ScrobbleResult(resultHolder.isSuccess(), resultHolder.getMessage());
    }

}
