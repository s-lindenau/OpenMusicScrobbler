package nl.slindenau.openmusicscrobbler.web.view;

import io.dropwizard.views.View;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ConfigureApplicationView extends View {

    private static final String CONFIGURE_SETTINGS_VIEW_TEMPLATE = "configure.mustache";
    public static final String SENSITIVE_INPUT_MARKER = "**************";
    public static final String EMPTY_STRING = "";

    private final ApplicationProperties applicationProperties;

    public ConfigureApplicationView(ApplicationProperties applicationProperties) {
        super(CONFIGURE_SETTINGS_VIEW_TEMPLATE);
        this.applicationProperties = applicationProperties;
    }

    @SuppressWarnings("unused")
    public String getSensitiveInputMarker() {
        return SENSITIVE_INPUT_MARKER;
    }

    @SuppressWarnings("unused")
    public List<ApplicationPropertyValue> getApplicationPropertyValues() {
        return Arrays.stream(ApplicationProperty.values())
                .map(this::createApplicationPropertyValue)
                .toList();
    }

    private ApplicationPropertyValue createApplicationPropertyValue(ApplicationProperty property) {
        String propertyValue = applicationProperties.getProperty(property);
        return new ApplicationPropertyValue(property, Objects.requireNonNullElse(propertyValue, EMPTY_STRING));
    }

    public record ApplicationPropertyValue(ApplicationProperty property, String currentValue) {

    }
}
