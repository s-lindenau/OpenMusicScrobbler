package nl.slindenau.openmusicscrobbler.web.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class WebApplicationResponse {
    private final boolean success;
    private final String message;

    public WebApplicationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public static class DetailResponse extends WebApplicationResponse {

        private final Collection<DetailMessage> details;

        public DetailResponse(boolean success, String message) {
            super(success, message);
            this.details = new LinkedList<>();
        }

        public void addDetail(WebApplicationResponse.DetailResponse.DetailMessage detailMessage) {
            this.details.add(detailMessage);
        }

        public Collection<DetailMessage> getDetails() {
            return Collections.unmodifiableCollection(details);
        }

        public record DetailMessage(boolean success, String element, String detailMessage) {
            // 'element' is the element in the DOM for which this detail message is relevant
        }
    }

}
