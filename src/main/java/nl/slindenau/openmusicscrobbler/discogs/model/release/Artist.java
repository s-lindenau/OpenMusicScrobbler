package nl.slindenau.openmusicscrobbler.discogs.model.release;

import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class Artist extends DiscogsApiResponse {
    public String name;
    public String anv;
    public String join;
    public String role;
    public String tracks;
    public int id;
    public String resource_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnv() {
        return anv;
    }

    public void setAnv(String anv) {
        this.anv = anv;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTracks() {
        return tracks;
    }

    public void setTracks(String tracks) {
        this.tracks = tracks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResource_url() {
        return resource_url;
    }

    public void setResource_url(String resource_url) {
        this.resource_url = resource_url;
    }
}
