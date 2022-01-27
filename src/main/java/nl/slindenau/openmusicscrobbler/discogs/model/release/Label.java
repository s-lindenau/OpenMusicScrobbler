package nl.slindenau.openmusicscrobbler.discogs.model.release;

import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class Label extends DiscogsApiResponse {
    public String name;
    public String catno;
    public String entity_type;
    public String entity_type_name;
    public int id;
    public String resource_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatno() {
        return catno;
    }

    public void setCatno(String catno) {
        this.catno = catno;
    }

    public String getEntity_type() {
        return entity_type;
    }

    public void setEntity_type(String entity_type) {
        this.entity_type = entity_type;
    }

    public String getEntity_type_name() {
        return entity_type_name;
    }

    public void setEntity_type_name(String entity_type_name) {
        this.entity_type_name = entity_type_name;
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
