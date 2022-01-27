package nl.slindenau.openmusicscrobbler.discogs.model.release;

import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;

import java.util.Date;
import java.util.List;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class Release extends DiscogsApiResponse {
    public int id;
    public String status;
    public int year;
    public String resource_url;
    public String uri;
    public List<Artist> artists;
    public String artists_sort;
    public List<Label> labels;
    // todo add model for series
    public List<Object> series;
    // todo add model for companies
    public List<Object> companies;
    public List<Format> formats;
    public String data_quality;
    public Community community;
    public int format_quantity;
    public Date date_added;
    public Date date_changed;
    public int num_for_sale;
    public double lowest_price;
    public String title;
    public String country;
    public String released;
    public String released_formatted;
    public List<Identifier> identifiers;
    public List<String> genres;
    public List<String> styles;
    public List<Tracklist> tracklist;
    public List<Extraartist> extraartists;
    public List<Image> images;
    public String thumb;
    public int estimated_weight;
    public boolean blocked_from_sale;
}
