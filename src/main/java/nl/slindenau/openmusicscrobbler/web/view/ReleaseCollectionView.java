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

    private final ReleaseCollection releaseCollection;

    public ReleaseCollectionView(ReleaseCollection releaseCollection) {
        super(RELEASE_COLLECTION_VIEW_TEMPLATE);
        this.releaseCollection = releaseCollection;
    }

    @SuppressWarnings("unused")
    public ReleaseCollection getReleaseCollection() {
        return releaseCollection;
    }
}
