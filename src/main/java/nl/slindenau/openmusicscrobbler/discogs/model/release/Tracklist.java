package nl.slindenau.openmusicscrobbler.discogs.model.release;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class Tracklist {
    public String position;
    @JsonProperty("type_")
    public String type;
    public String title;
    public String duration;
}
