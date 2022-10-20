package nl.slindenau.openmusicscrobbler.service.search;

import java.util.HashMap;
import java.util.Map;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class SearchQuery {

    private final Map<SearchCategory, String> queryParts = new HashMap<>();

    public void addSearchTerm(SearchCategory category, String searchValue) {
        queryParts.put(category, searchValue);
    }

    public Map<SearchCategory, String> getQueryParts() {
        return queryParts;
    }
}
