package nl.slindenau.openmusicscrobbler.web.view;

import io.dropwizard.views.View;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;

/**
 * @author davidvollmar
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ReleaseCollectionView extends View {

    private static final String RELEASE_COLLECTION_VIEW_TEMPLATE = "releaseCollection.mustache";
    private static final String NO_SEARCH = null;
    public static final String EMPTY_SEARCH = "";

    private final ReleaseCollection releaseCollection;
    private final String searchQuery;
    private final boolean isSearch;

    public ReleaseCollectionView(ReleaseCollection releaseCollection) {
        this(releaseCollection, NO_SEARCH);
    }

    public ReleaseCollectionView(ReleaseCollection releaseCollection, String searchQuery) {
        super(RELEASE_COLLECTION_VIEW_TEMPLATE);
        this.releaseCollection = releaseCollection;
        this.searchQuery = searchQuery;
        this.isSearch = searchQuery != null;
    }

    @SuppressWarnings("unused")
    public ReleaseCollection getReleaseCollection() {
        return releaseCollection;
    }

    @SuppressWarnings("unused")
    public String getSearchQuery() {
        return searchQuery;
    }

    @SuppressWarnings("unused")
    public boolean isSearch() {
        return isSearch;
    }
}
