package nl.slindenau.openmusicscrobbler.service.search;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class SearchQueryParser {

    public SearchQuery parseSearchQuery(String searchQueryText) {
        SearchQuery searchQuery = new SearchQuery();
        // todo: implement parsing of categories
        searchQuery.addSearchTerm(SearchCategory.ANY, searchQueryText);
        return searchQuery;
    }
}
