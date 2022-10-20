package nl.slindenau.openmusicscrobbler.service.search;

import nl.slindenau.openmusicscrobbler.model.MusicReleaseBasicInformation;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class SearchService {

    private Collection<Searchable<MusicReleaseBasicInformation>> musicReleaseSearchable = new ArrayList<>();

    public void loadCollection(ReleaseCollection userCollection) {
        this.musicReleaseSearchable = new SearchableFactory().createSearchInfo(userCollection.releases());
    }

    public ReleaseCollection findMatching(String searchQueryText) {
        SearchQuery searchQuery = new SearchQueryParser().parseSearchQuery(searchQueryText);
        return findMatching(searchQuery);
    }

    public ReleaseCollection findMatching(SearchQuery searchQuery) {
        Collection<MusicReleaseBasicInformation> searchResults = musicReleaseSearchable.stream()
                .filter(searchable -> searchableMatchesQuery(searchable, searchQuery))
                .map(Searchable::target)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(MusicReleaseBasicInformation::id))));
        return new ReleaseCollection(searchResults);
    }

    private boolean searchableMatchesQuery(Searchable<MusicReleaseBasicInformation> searchable, SearchQuery searchQuery) {
        return searchQuery.getQueryParts().entrySet().stream()
                .anyMatch(entry -> searchQueryPartMatches(searchable, entry.getKey(), entry.getValue()));
    }

    private boolean searchQueryPartMatches(Searchable<MusicReleaseBasicInformation> searchable, SearchCategory searchCategory, String searchValue) {
        if (SearchCategory.ANY == searchCategory || searchable.category() == searchCategory) {
            return searchable.value().toLowerCase().contains(searchValue.toLowerCase());
        }
        return false;
    }
}
