package nl.slindenau.openmusicscrobbler.web.api;

import com.codahale.metrics.annotation.Timed;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import nl.slindenau.openmusicscrobbler.service.DiscogsService;
import nl.slindenau.openmusicscrobbler.web.view.ReleaseCollectionView;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/collection")
@Produces({MediaType.APPLICATION_JSON+";charset=UTF-8", MediaType.TEXT_HTML+";charset=UTF-8"})
public class CollectionResource {

    private final ApplicationProperties applicationProperties = new ApplicationProperties();

    //todo: make thread safe
    DiscogsService discogsService = new DiscogsService();

    @GET
    @Timed
    @Consumes({MediaType.APPLICATION_JSON})
    public ReleaseCollection getCollectionAsJson() {
        return discogsService.getUserCollection(getDiscogsUsername());
    }

    @GET
    @Timed
    public ReleaseCollectionView getCollectionAsView() {
        return new ReleaseCollectionView(discogsService.getUserCollection(getDiscogsUsername()));
    }

    @POST
    @Path("/scrobble")
    public void scrobble(@QueryParam("id") Optional<Long> id) {
        // todo: implement
        System.out.println("Scrobble: " + id.get());
    }

    private String getDiscogsUsername() {
        return applicationProperties.getDiscogsUsername();
    }
}
