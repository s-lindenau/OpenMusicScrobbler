package nl.slindenau.openmusicscrobbler.config;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.util.FileFacade;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ConfigurationProperties {

    private static final String CONFIGURATION_COMMENTS = "Configure application properties in this file, or override values via Java System Properties";
    private static final String CONFIGURATION_PROPERTIES_FILE_NAME = "config.properties";
    private static final String CURRENT_WORKING_DIRECTORY = ".";

    private final FileFacade fileFacade;
    private final SystemProperties systemProperties;

    public ConfigurationProperties() {
        this(new FileFacade(), new SystemProperties());
    }

    protected ConfigurationProperties(FileFacade fileFacade, SystemProperties systemProperties) {
        this.fileFacade = fileFacade;
        this.systemProperties = systemProperties;
    }

    /**
     * Read the values from the default configuration properties file in the current working directory
     *
     * @return the parsed configuration properties
     */
    public Properties readConfigurationProperties() {
        Path currentDirectory = getConfigurationPropertiesDirectory();
        Path propertiesFile = getConfigurationPropertiesFile(currentDirectory);
        return readConfigurationProperties(propertiesFile);
    }

    public Path getConfigurationPropertiesDirectory() {
        String configurationDirectory = getConfigurationDirectory();
        return fileFacade.get(configurationDirectory);
    }

    private String getConfigurationDirectory() {
        return systemProperties.getConfigurationDirectory().orElse(CURRENT_WORKING_DIRECTORY);
    }

    /**
     * Read the values from the given configuration properties file
     *
     * @param propertiesFile the properties file
     * @return the parsed configuration properties
     */
    public Properties readConfigurationProperties(Path propertiesFile) {
        Properties properties = new Properties();
        if (fileFacade.exists(propertiesFile)) {
            loadProperties(propertiesFile, properties);
        }
        return properties;
    }

    private void loadProperties(Path propertiesFile, Properties properties) {
        try (BufferedReader fileReader = fileFacade.newBufferedReader(propertiesFile)) {
            properties.load(fileReader);
        } catch (IOException ex) {
            throw new OpenMusicScrobblerException(ex);
        }
    }

    /**
     * Write the current application properties to a file.
     * Called by <code>generateConfigurationProperties.bsh</code>
     *
     * @param directory the directory to create the configuration properties file in
     * @return the created file
     */
    public Path writeConfigurationProperties(Path directory) {
        return writeConfigurationProperties(directory, new ApplicationProperties());
    }

    public Path writeConfigurationProperties(Path directory, ApplicationProperties applicationPropertiesProvider) {
        Path configurationPropertiesFile = getConfigurationPropertiesFile(directory);
        Properties applicationProperties = applicationPropertiesProvider.getAsProperties();
        storeProperties(configurationPropertiesFile, applicationProperties);
        return configurationPropertiesFile;
    }

    private Path getConfigurationPropertiesFile(Path directory) {
        if(fileFacade.exists(directory) && fileFacade.isDirectory(directory)) {
            return directory.resolve(CONFIGURATION_PROPERTIES_FILE_NAME);
        }
        throw new OpenMusicScrobblerException("Expected existing directory as input, got: " + directory);
    }

    private void storeProperties(Path propertiesFile, Properties applicationProperties) {
        try (BufferedWriter fileWriter = fileFacade.newBufferedWriter(propertiesFile)) {
            applicationProperties.store(fileWriter, CONFIGURATION_COMMENTS);
        } catch (IOException ex) {
            throw new OpenMusicScrobblerException(ex);
        }
    }
}
