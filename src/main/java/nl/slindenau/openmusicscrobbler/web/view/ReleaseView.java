package nl.slindenau.openmusicscrobbler.web.view;

import io.dropwizard.views.View;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ReleaseView extends View {

    private static final String RELEASE_VIEW_TEMPLATE = "release.mustache";

    private final MusicRelease release;

    public ReleaseView(MusicRelease release) {
        super(RELEASE_VIEW_TEMPLATE);
        this.release = release;
    }

    @SuppressWarnings("unused")
    public MusicRelease getRelease() {
        return release;
    }
}
