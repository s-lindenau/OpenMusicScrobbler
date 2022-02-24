package nl.slindenau.openmusicscrobbler.model;

import de.umass.lastfm.scrobble.ScrobbleResult;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmScrobbleResult {
    private ScrobbleResult apiScrobbleResult;
    private String message;

    public LastFmScrobbleResult(ScrobbleResult apiScrobbleResult) {
        this.apiScrobbleResult = apiScrobbleResult;
    }

    public LastFmScrobbleResult(String message) {
        this.message = message;
    }

    public ScrobbleResult getApiScrobbleResult() {
        return apiScrobbleResult;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        if (apiScrobbleResult != null) {
            return apiScrobbleResult.toString();
        }
        return message;
    }
}
