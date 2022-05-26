package nl.slindenau.openmusicscrobbler.web;

import com.codahale.metrics.annotation.Timed;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import nl.slindenau.openmusicscrobbler.service.DiscogsService;
import nl.slindenau.openmusicscrobbler.web.view.ReleaseCollectionView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/collection")
@Produces({MediaType.APPLICATION_JSON+";charset=UTF-8", MediaType.TEXT_HTML+";charset=UTF-8"})
public class CollectionResource {

    //todo ding thread safe maken
    DiscogsService discogsService = new DiscogsService();

    @GET
    @Timed
    @Consumes({MediaType.APPLICATION_JSON})
    public ReleaseCollection getCollectionAsJson() {
        return discogsService.getUserCollection("slindenau2");
    }

    @GET
    @Timed
    public ReleaseCollectionView getCollectionAsView() {
        return new ReleaseCollectionView(discogsService.getUserCollection("slindenau2"));
    }

    @POST
    @Path("/scrobble")
    public void scrobble(@QueryParam("id") Optional<Long> id) {
        System.out.println("Scrobble: " + id.get());
    }
}
