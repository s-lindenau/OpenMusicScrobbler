package nl.slindenau.openmusicscrobbler.web.view;

import io.dropwizard.views.View;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.web.ParameterConverterProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ReleaseView extends View {

    private static final String RELEASE_VIEW_TEMPLATE = "release.mustache";
    private static final String FORM_INPUT_DATE_TIME_FORMAT = ParameterConverterProvider.FORM_INPUT_FIELD_LOCAL_DATE_TIME_FORMAT;

    private final MusicRelease release;

    public ReleaseView(MusicRelease release) {
        super(RELEASE_VIEW_TEMPLATE);
        this.release = release;
    }

    @SuppressWarnings("unused")
    public MusicRelease getRelease() {
        return release;
    }

    @SuppressWarnings("unused")
    public String currentLocalDateTime() {
        return DateTimeFormatter.ofPattern(FORM_INPUT_DATE_TIME_FORMAT).format(LocalDateTime.now());
    }
}
