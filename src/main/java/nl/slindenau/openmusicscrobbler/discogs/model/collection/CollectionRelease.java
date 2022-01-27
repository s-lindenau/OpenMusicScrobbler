package nl.slindenau.openmusicscrobbler.discogs.model.collection;

import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;

import java.util.Date;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class CollectionRelease extends DiscogsApiResponse {
    public int id;
    public int instance_id;
    public Date date_added;
    public int rating;
    public BasicInformation basic_information;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInstance_id() {
        return instance_id;
    }

    public void setInstance_id(int instance_id) {
        this.instance_id = instance_id;
    }

    public Date getDate_added() {
        return date_added;
    }

    public void setDate_added(Date date_added) {
        this.date_added = date_added;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public BasicInformation getBasic_information() {
        return basic_information;
    }

    public void setBasic_information(BasicInformation basic_information) {
        this.basic_information = basic_information;
    }
}
