package nl.slindenau.openmusicscrobbler.service.search;

import nl.slindenau.openmusicscrobbler.model.MusicReleaseBasicInformation;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
class SearchServiceTest {

    private static final String SEARCH_TEXT = "1999";

    private SearchService searchService;

    @SuppressWarnings("FieldCanBeLocal")
    private MusicReleaseBasicInformation releaseNoHit;
    private MusicReleaseBasicInformation releaseYearHit;
    private MusicReleaseBasicInformation releaseTitleHit;

    @BeforeEach
    void setUp() {
        searchService = new SearchService();
        Collection<MusicReleaseBasicInformation> releases = setupReleases();
        ReleaseCollection releaseCollection = new ReleaseCollection(releases);
        searchService.loadCollection(releaseCollection);
    }

    private Collection<MusicReleaseBasicInformation> setupReleases() {
        Collection<MusicReleaseBasicInformation> releases = new ArrayList<>();
        releaseNoHit = new MusicReleaseBasicInformation(1, 1, "A", "T", "F", 0, "T");
        releaseYearHit = new MusicReleaseBasicInformation(2, 2, "Kadril", "Eva", "CD", 1999, "T");
        releaseTitleHit = new MusicReleaseBasicInformation(3, 3, "Vengaboys", "1999 (I Wanna Go Back)", "File", 2021, "T");
        releases.add(releaseNoHit);
        releases.add(releaseYearHit);
        releases.add(releaseTitleHit);
        return releases;
    }

    @Test
    void findByText() {
        ReleaseCollection searchResult = searchService.findMatching(SEARCH_TEXT);
        assertSearchResultMatches(searchResult, releaseTitleHit, releaseYearHit);
    }

    @Test
    void findByYear() {
        SearchQuery query = new SearchQuery();
        query.addSearchTerm(SearchCategory.YEAR, SEARCH_TEXT);
        ReleaseCollection searchResult = searchService.findMatching(query);
        assertSearchResultMatches(searchResult, releaseYearHit);
    }

    @Test
    void findByTitle() {
        SearchQuery query = new SearchQuery();
        query.addSearchTerm(SearchCategory.TITLE, SEARCH_TEXT);
        ReleaseCollection searchResult = searchService.findMatching(query);
        assertSearchResultMatches(searchResult, releaseTitleHit);
    }

    @Test
    void findByYearOrTitel() {
        SearchQuery query = new SearchQuery();
        query.addSearchTerm(SearchCategory.YEAR, SEARCH_TEXT);
        query.addSearchTerm(SearchCategory.TITLE, SEARCH_TEXT);
        ReleaseCollection searchResult = searchService.findMatching(query);
        assertSearchResultMatches(searchResult, releaseTitleHit, releaseYearHit);
    }

    private void assertSearchResultMatches(ReleaseCollection searchResult, MusicReleaseBasicInformation... expectedReleases) {
        Collection<MusicReleaseBasicInformation> actual = new ArrayList<>(searchResult.releases());
        Collection<MusicReleaseBasicInformation> expected = Arrays.asList(expectedReleases);
        // check all Actual are in Expected list
        actual.forEach(actualRelease -> assertReleaseIsExpected(actualRelease, expected, "Actual", "expected"));
        // check all Expected are in Actual list
        expected.forEach(expectedRelease -> assertReleaseIsExpected(expectedRelease, actual, "Expected", "actual"));
    }

    private void assertReleaseIsExpected(MusicReleaseBasicInformation toTest, Collection<MusicReleaseBasicInformation> collection, String source, String target) {
        boolean isReleaseFound = collection.contains(toTest);
        if(!isReleaseFound) {
            System.out.println(target + collection);
        }
        Assertions.assertTrue(isReleaseFound, () -> String.format("%s release not in %s list: %s", source, target, toTest));
    }
}