package nl.slindenau.openmusicscrobbler.web.service;

import de.umass.util.StringUtilities;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperty;
import nl.slindenau.openmusicscrobbler.config.ConfigurationProperties;
import nl.slindenau.openmusicscrobbler.util.OptionalString;
import nl.slindenau.openmusicscrobbler.web.view.ConfigureApplicationView;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ConfigurationService {

    public void saveConfiguration(Form formData) {
        setAsSystemProperties(formData.asMap());
        savePropertiesFile();
    }

    private void setAsSystemProperties(MultivaluedMap<String, String> properties) {
        for (ApplicationProperty supportedProperty : ApplicationProperty.values()) {
            String propertyKey = supportedProperty.getKey();
            String propertyNewValue = properties.getFirst(propertyKey);
            setAsSystemProperty(supportedProperty, propertyNewValue);
        }
    }

    private void setAsSystemProperty(ApplicationProperty property, String propertyValueInput) {
        if (ConfigureApplicationView.SENSITIVE_INPUT_MARKER.equalsIgnoreCase(propertyValueInput)) {
            // sensitive property is masked; when the value is still this mask, it is unchanged
            String currentPropertyValue = new ApplicationProperties().getProperty(property);
            System.setProperty(property.getKey(), currentPropertyValue);
        } else {
            System.setProperty(property.getKey(), getPropertyValue(property, propertyValueInput));
        }
    }

    private String getPropertyValue(ApplicationProperty property, String propertyValueInput) {
        if (ApplicationProperty.LAST_FM_PASSWORD.equals(property)) {
            return OptionalString.ofNullableOrBlank(propertyValueInput)
                    // todo: properly encrypt the md5 hash
                    .map(StringUtilities::md5)
                    .orElse(ConfigureApplicationView.EMPTY_STRING);
        }
        return propertyValueInput;
    }

    private void savePropertiesFile() {
        ConfigurationProperties configurationProperties = new ConfigurationProperties();
        java.nio.file.Path output = configurationProperties.getConfigurationPropertiesDirectory();
        // this resource sets all properties on the System environment, use only those when persisting
        configurationProperties.writeConfigurationProperties(output, ApplicationProperties.fromSystemOnly());
    }
}
