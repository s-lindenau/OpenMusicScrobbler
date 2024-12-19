package nl.slindenau.openmusicscrobbler.discogs.model.collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;

import java.util.Date;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@SuppressWarnings("unused")
public class CollectionRelease extends DiscogsApiResponse {
    public int id;
    @JsonProperty("instance_id")
    public int instanceId;
    @JsonProperty("date_added")
    public Date dateAdded;
    public int rating;
    @JsonProperty("basic_information")
    public BasicInformation basicInformation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public BasicInformation getBasicInformation() {
        return basicInformation;
    }

    public void setBasicInformation(BasicInformation basicInformation) {
        this.basicInformation = basicInformation;
    }
}
