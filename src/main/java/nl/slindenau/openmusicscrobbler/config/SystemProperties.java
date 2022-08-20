package nl.slindenau.openmusicscrobbler.config;

import nl.slindenau.openmusicscrobbler.util.OptionalString;

import java.util.Optional;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class SystemProperties {

    private static final String CONFIGURATION_FOLDER_PROPERTY_KEY = "oms.config";

    public Optional<String> getConfigurationDirectory() {
        return OptionalString.ofNullableOrBlank(System.getProperty(CONFIGURATION_FOLDER_PROPERTY_KEY));
    }
}
