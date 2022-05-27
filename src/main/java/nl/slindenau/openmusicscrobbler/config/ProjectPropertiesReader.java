package nl.slindenau.openmusicscrobbler.config;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.util.ResourceFileReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ProjectPropertiesReader {

    private static final String PROJECT_PROPERTIES_FILE_NAME = "project.properties";
    // properties in pom.xml, stored in resource file by maven plugin
    private static final String APPLICATION_NAME_KEY = "app.name";
    private static final String APPLICATION_VERSION_KEY = "app.version";
    private static final String APPLICATION_URL_KEY = "app.url";

    private Properties properties;

    public String getApplicationName() {
        return getProperties().getProperty(APPLICATION_NAME_KEY);
    }

    public String getApplicationVersion() {
        return getProperties().getProperty(APPLICATION_VERSION_KEY);
    }

    public String getApplicationHomepage() {
        return getProperties().getProperty(APPLICATION_URL_KEY);
    }

    private synchronized Properties getProperties() {
        if (this.properties == null) {
            this.properties = readProjectProperties();
        }
        return this.properties;
    }

    private Properties readProjectProperties() {
        try {
            return loadProjectProperties();
        } catch (Exception e) {
            throw new OpenMusicScrobblerException("Error loading " + PROJECT_PROPERTIES_FILE_NAME, e);
        }
    }

    private Properties loadProjectProperties() throws IOException {
        String fileContents = new ResourceFileReader().readFileContents(ProjectPropertiesReader.class, PROJECT_PROPERTIES_FILE_NAME);
        Properties properties = new Properties();
        try (StringReader reader = new StringReader(fileContents)) {
            properties.load(reader);
        }
        return properties;
    }

}
