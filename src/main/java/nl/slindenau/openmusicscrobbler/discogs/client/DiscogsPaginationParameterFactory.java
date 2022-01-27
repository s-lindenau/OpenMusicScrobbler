package nl.slindenau.openmusicscrobbler.discogs.client;

import nl.slindenau.openmusicscrobbler.Constants;
import nl.slindenau.openmusicscrobbler.discogs.model.Pagination;

import java.util.HashMap;
import java.util.Map;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsPaginationParameterFactory {

    private static final String INVALID_PAGE_NUMBER_ERROR_MESSAGE = "Invalid page number %s, number of pages are: %s";

    public Map<String, String> getPageParameters(Pagination pagination, int pageNumber) {
        int totalPages = pagination.getPages();
        if (pageNumber < 1 || pageNumber > totalPages) {
            throw new RuntimeException(String.format(INVALID_PAGE_NUMBER_ERROR_MESSAGE, pageNumber, totalPages));
        }
        Map<String, String> parameters = new HashMap<>();
        parameters.put(Constants.DISCOGS_PARAMETER_PAGE, String.valueOf(pageNumber));
        parameters.put(Constants.DISCOGS_PARAMETER_PAGE_SIZE, String.valueOf(pagination.getPer_page()));
        return parameters;
    }
}
