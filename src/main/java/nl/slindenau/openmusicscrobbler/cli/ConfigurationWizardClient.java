package nl.slindenau.openmusicscrobbler.cli;

import de.umass.util.StringUtilities;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperty;
import nl.slindenau.openmusicscrobbler.config.ConfigurationProperties;
import nl.slindenau.openmusicscrobbler.util.OptionalString;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Helps the end user setup their current configuration properties.<br/>
 * Will store the output in the <code>config.properties</code> file.<br/>
 * The last.fm client supports providing the password in MD5.<br/>
 * This class will securely store that password.
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ConfigurationWizardClient extends AbstractConsoleClient {

    private static final String PASSWORD_STRING = "***";
    private static final String EMPTY_STRING = "";

    private final ApplicationProperties applicationProperties = new ApplicationProperties();

    @Override
    public void run() {
        printLine("The configuration wizard will help to configure all application properties.");
        printLine("Leave the new value empty (blank) to keep the current value.");
        printEmptyLine();

        Stream.of(ApplicationProperty.values()).forEach(this::configureProperty);

        boolean isConfirmed = askConfirm();
        if (isConfirmed) {
            saveProperties();
        }
        closeConsoleClient();
    }

    private boolean askConfirm() {
        String command = readConsoleOptionalTextInput("Press [Enter] to save or type [cancel] to revert changes");
        return OptionalString.ofNullableOrBlank(command).isEmpty();
    }

    private void configureProperty(ApplicationProperty applicationProperty) {
        if (ApplicationProperty.LAST_FM_PASSWORD.equals(applicationProperty)) {
            configurePropertyValue(applicationProperty, this::readLastFmPasswordInput, this::printPasswordProperty);
        } else {
            configurePropertyValue(applicationProperty, this::readConsoleOptionalTextInput, this::printPlainProperty);
        }
    }

    private void configurePropertyValue(ApplicationProperty applicationProperty, Function<String, String> propertyValueReader, Function<ApplicationProperty, String> propertyValuePrinter) {
        String propertyKey = applicationProperty.getKey();
        printLine("Property: " + propertyKey);
        printLine("Description: " + applicationProperty.getDescription());
        printLine("Current value: " + propertyValuePrinter.apply(applicationProperty));
        String propertyInput = propertyValueReader.apply("Enter new value");
        Optional<String> propertyValue = OptionalString.ofNullableOrBlank(propertyInput);
        propertyValue.ifPresent(newPropertyValue -> System.setProperty(propertyKey, newPropertyValue));
        printEmptyLine();
    }

    private String readLastFmPasswordInput(String propertyName) {
        String passwordInput = readConsolePasswordInput(propertyName);
        Optional<String> password = OptionalString.ofNullableOrBlank(passwordInput);
        if(password.isPresent()) {
            // todo: properly encrypt the md5 hash
            return StringUtilities.md5(passwordInput);
        } else {
            return null;
        }
    }

    private String printPlainProperty(ApplicationProperty applicationProperty) {
        Optional<String> propertyValue = getProperty(applicationProperty);
        return propertyValue.orElse(EMPTY_STRING);
    }

    private String printPasswordProperty(ApplicationProperty applicationProperty) {
        Optional<String> propertyValue = getProperty(applicationProperty);
        if (propertyValue.isPresent()) {
            return PASSWORD_STRING;
        } else {
            return EMPTY_STRING;
        }
    }

    private Optional<String> getProperty(ApplicationProperty applicationProperty) {
        String propertyValue = applicationProperties.getProperty(applicationProperty);
        return OptionalString.ofNullableOrBlank(propertyValue);
    }

    private void saveProperties() {
        ConfigurationProperties configurationProperties = new ConfigurationProperties();
        Path output = configurationProperties.getConfigurationPropertiesDirectory();
        configurationProperties.writeConfigurationProperties(output);
    }
}
