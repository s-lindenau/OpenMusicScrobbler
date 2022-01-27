package nl.slindenau.openmusicscrobbler.discogs.model.collection;

import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;

import java.util.List;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class UserCollection extends DiscogsApiResponse {

    private List<CollectionFolder> folders;

    public List<CollectionFolder> getFolders() {
        return folders;
    }

    public void setFolders(List<CollectionFolder> folders) {
        this.folders = folders;
    }
}
