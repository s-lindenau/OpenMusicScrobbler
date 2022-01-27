package nl.slindenau.openmusicscrobbler.discogs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsApiResponse {

    @JsonProperty("message")
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isError() {
        return errorMessage != null;
    }
}
