package nl.slindenau.openmusicscrobbler.discogs.model.release;

import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;

import java.util.List;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class Format extends DiscogsApiResponse {
    public String name;
    public String qty;
    public String text;
    public List<String> descriptions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }
}
