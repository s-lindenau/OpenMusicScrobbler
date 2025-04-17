package nl.slindenau.openmusicscrobbler.web.model;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ScrobbleResult extends WebApplicationResponse.DetailResponse {

    public ScrobbleResult(boolean success, String message) {
        super(success, message);
    }
}
