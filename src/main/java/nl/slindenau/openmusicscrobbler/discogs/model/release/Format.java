package nl.slindenau.openmusicscrobbler.discogs.model.release;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;

import java.util.List;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class Format extends DiscogsApiResponse {
    public String name;
    @JsonProperty("qty")
    public String quantity;
    public String text;
    public List<String> descriptions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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
