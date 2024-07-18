package nl.slindenau.openmusicscrobbler.web.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.ws.rs.FormParam;
import java.util.List;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ScrobbleRequest {

    @NotNull(message = "Release ID is missing in request!")
    @Positive(message = "Release ID should be a positive number!")
    @FormParam("discogsId")
    public Long discogsId;

    @NotEmpty(message = "Select at least one track to scrobble!")
    @FormParam("selectedTrack")
    public List<String> selectedTracks;

}
