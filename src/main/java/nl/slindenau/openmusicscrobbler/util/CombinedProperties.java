package nl.slindenau.openmusicscrobbler.util;

import java.util.Optional;
import java.util.Properties;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class CombinedProperties {

    private final Properties baseProperties;
    private Properties overrideProperties;

    public CombinedProperties(Properties baseProperties) {
        this.baseProperties = baseProperties;
        this.overrideProperties = null;
    }

    public CombinedProperties withOverride(Properties overrideProperties) {
        this.overrideProperties = overrideProperties;
        return this;
    }

    public String getProperty(String key) {
        return getOverrideProperty(key).orElse(baseProperties.getProperty(key));
    }

    public String getProperty(String key, String defaultValue) {
        return getOverrideProperty(key).orElse(baseProperties.getProperty(key, defaultValue));
    }

    private Optional<String> getOverrideProperty(String key) {
        if (overrideProperties != null) {
            // no getProperty with default value on the override; check if the key is defined in the baseProperties in that case
            return OptionalString.ofNullableOrBlank(overrideProperties.getProperty(key));
        }
        return Optional.empty();
    }
}
