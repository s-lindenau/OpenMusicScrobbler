package nl.slindenau.openmusicscrobbler.discogs.model.collection;

import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Artist;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Format;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Label;

import java.util.List;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class BasicInformation extends DiscogsApiResponse {

    public int id;
    public int master_id;
    public String master_url;
    public String resource_url;
    public String thumb;
    public String cover_image;
    public String title;
    public int year;
    public List<Format> formats;
    public List<Label> labels;
    public List<Artist> artists;
    public List<String> genres;
    public List<String> styles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaster_id() {
        return master_id;
    }

    public void setMaster_id(int master_id) {
        this.master_id = master_id;
    }

    public String getMaster_url() {
        return master_url;
    }

    public void setMaster_url(String master_url) {
        this.master_url = master_url;
    }

    public String getResource_url() {
        return resource_url;
    }

    public void setResource_url(String resource_url) {
        this.resource_url = resource_url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Format> getFormats() {
        return formats;
    }

    public void setFormats(List<Format> formats) {
        this.formats = formats;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getStyles() {
        return styles;
    }

    public void setStyles(List<String> styles) {
        this.styles = styles;
    }
}
