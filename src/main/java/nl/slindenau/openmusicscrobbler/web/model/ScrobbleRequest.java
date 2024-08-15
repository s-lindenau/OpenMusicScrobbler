package nl.slindenau.openmusicscrobbler.web.model;

import nl.slindenau.openmusicscrobbler.lastfm.validation.LastFmScrobbleDateConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.ws.rs.FormParam;
import java.time.Instant;
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

    @NotNull(message = "Scrobble date is required!")
    @PastOrPresent(message = "Scrobble date must not be in the future!")
    @LastFmScrobbleDateConstraint(message = "Scrobble date is too far in the past! Last.fm only processes historic scrobbles from the past %s days!")
    @FormParam("lastTrackEndedAt")
    public Instant lastTrackEndedAt;
}
