package nl.slindenau.openmusicscrobbler.web.controller;

import com.codahale.metrics.annotation.Timed;
import de.umass.util.StringUtilities;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperty;
import nl.slindenau.openmusicscrobbler.config.ConfigurationProperties;
import nl.slindenau.openmusicscrobbler.util.OptionalString;
import nl.slindenau.openmusicscrobbler.web.view.ConfigureApplicationView;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@Path("/configuration")
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8", MediaType.TEXT_HTML + ";charset=UTF-8"})
public class ConfigurationResource {

    @GET
    @Timed
    public ConfigureApplicationView getConfigurationAsView() {
        return getConfigureApplicationView();
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public ConfigureApplicationView saveConfiguration(Form formData) {
        setAsSystemProperties(formData.asMap());
        savePropertiesFile();
        return getConfigureApplicationView();
    }

    private ConfigureApplicationView getConfigureApplicationView() {
        return new ConfigureApplicationView(new ApplicationProperties());
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
