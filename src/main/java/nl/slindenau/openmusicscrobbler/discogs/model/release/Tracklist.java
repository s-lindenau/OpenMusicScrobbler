package nl.slindenau.openmusicscrobbler.discogs.model.release;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@SuppressWarnings("unused")
public class Tracklist {
    public String position;
    @JsonProperty("type_")
    public String type;
    public String title;
    public String duration;
    public List<Artist> artists;
    @JsonProperty("sub_tracks")
    public List<Tracklist> subTracks;
}
