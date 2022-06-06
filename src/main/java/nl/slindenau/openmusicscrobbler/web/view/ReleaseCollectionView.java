package nl.slindenau.openmusicscrobbler.web.view;

import io.dropwizard.views.View;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;

public class ReleaseCollectionView extends View {

    private final ReleaseCollection releaseCollection;

    public ReleaseCollectionView(ReleaseCollection releaseCollection) {
        super("releaseCollection.mustache");
        this.releaseCollection = releaseCollection;
    }

    public ReleaseCollection getReleaseCollection() {
        return releaseCollection;
    }
}
