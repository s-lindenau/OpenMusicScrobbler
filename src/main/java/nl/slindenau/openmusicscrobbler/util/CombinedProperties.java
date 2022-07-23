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
        // no getProperty(key, default); blank values are seen as non-null and don't return the defaultValue
        return getOverrideProperty(key).orElse(getBaseProperty(key).orElse(defaultValue));
    }

    private Optional<String> getBaseProperty(String key) {
        return OptionalString.ofNullableOrBlank(baseProperties.getProperty(key));
    }

    private Optional<String> getOverrideProperty(String key) {
        if (overrideProperties != null) {
            return OptionalString.ofNullableOrBlank(overrideProperties.getProperty(key));
        }
        return Optional.empty();
    }
}
