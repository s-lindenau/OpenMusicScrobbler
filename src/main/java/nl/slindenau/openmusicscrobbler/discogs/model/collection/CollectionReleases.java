package nl.slindenau.openmusicscrobbler.discogs.model.collection;

import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;
import nl.slindenau.openmusicscrobbler.discogs.model.Pagination;

import java.util.List;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class CollectionReleases extends DiscogsApiResponse {
    public Pagination pagination;
    public List<CollectionRelease> releases;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<CollectionRelease> getReleases() {
        return releases;
    }

    public void setReleases(List<CollectionRelease> releases) {
        this.releases = releases;
    }
}
