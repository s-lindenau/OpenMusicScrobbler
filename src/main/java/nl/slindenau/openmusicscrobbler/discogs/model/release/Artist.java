package nl.slindenau.openmusicscrobbler.discogs.model.release;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@SuppressWarnings("unused")
public class Artist extends DiscogsApiResponse {
    public String name;
    @JsonProperty("anv")
    public String artistNameVariation;
    public String join;
    public String role;
    public String tracks;
    public int id;
    @JsonProperty("resource_url")
    public String resourceUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistNameVariation() {
        return artistNameVariation;
    }

    public void setArtistNameVariation(String artistNameVariation) {
        this.artistNameVariation = artistNameVariation;
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

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
