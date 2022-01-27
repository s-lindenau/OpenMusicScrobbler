package nl.slindenau.openmusicscrobbler.discogs.model.collection;

import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class CollectionFolder extends DiscogsApiResponse {

    private int id;
    private String name;
    private int count;
    private String resource_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getResource_url() {
        return resource_url;
    }

    public void setResource_url(String resource_url) {
        this.resource_url = resource_url;
    }
}
