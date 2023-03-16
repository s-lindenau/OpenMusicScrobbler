package nl.slindenau.openmusicscrobbler.web.api;

import com.codahale.metrics.annotation.Timed;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import nl.slindenau.openmusicscrobbler.service.DiscogsService;
import nl.slindenau.openmusicscrobbler.web.view.ReleaseCollectionView;
import nl.slindenau.openmusicscrobbler.web.view.ReleaseView;

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

    private final ApplicationProperties applicationProperties = new ApplicationProperties();
    //todo: make thread safe
    private final DiscogsService discogsService = new DiscogsService();

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
    @Path("/release")
    public ReleaseView getReleaseAsView(@QueryParam("id") Optional<Long> discogsId) {
        if (discogsId.isPresent()) {
            return new ReleaseView(findMusicRelease(discogsId.get()));
        } else {
            throw new IllegalArgumentException("Missing ID query parameter");
        }
    }

    @POST
    @Path("/scrobble")
    public void scrobble(@QueryParam("id") Optional<Long> discogsId) {
        discogsId.ifPresent(this::scrobbleRelease);
    }

    private void scrobbleRelease(Long discogsId) {
        // todo: implement
        System.out.println("Scrobble: " + discogsId);
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
