package nl.slindenau.openmusicscrobbler.discogs.model.release;

import java.util.List;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class Community {
    public int have;
    public int want;
    public Rating rating;
    public Submitter submitter;
    public List<Contributor> contributors;
    public String data_quality;
    public String status;
}
