package nl.slindenau.openmusicscrobbler.discogs.client;

import nl.slindenau.openmusicscrobbler.Constants;
import nl.slindenau.openmusicscrobbler.discogs.model.Pagination;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsPaginationParameterFactory {

    private static final String INVALID_PAGE_NUMBER_ERROR_MESSAGE = "Invalid page number %s, number of pages are: %s";
    private static final String DISCOGS_PARAMETER_PAGE = "page";
    private static final String DISCOGS_PARAMETER_PAGE_SIZE = "per_page";

    public Map<String, String> getPageParameters(Pagination pagination, int pageNumber) {
        int totalPages = pagination.getPages();
        if (pageNumber < 1 || pageNumber > totalPages) {
            throw new OpenMusicScrobblerException(String.format(INVALID_PAGE_NUMBER_ERROR_MESSAGE, pageNumber, totalPages));
        }
        Map<String, String> parameters = new HashMap<>();
        parameters.put(DISCOGS_PARAMETER_PAGE, String.valueOf(pageNumber));
        parameters.put(DISCOGS_PARAMETER_PAGE_SIZE, String.valueOf(pagination.getItemsPerPage()));
        return parameters;
    }
}
