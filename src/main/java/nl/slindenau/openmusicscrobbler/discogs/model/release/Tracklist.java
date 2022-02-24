package nl.slindenau.openmusicscrobbler.discogs.model.release;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tracklist {
    public String position;
    public String type_;
    public String title;
    public String duration;
}
