package nl.slindenau.openmusicscrobbler.config;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.util.OptionalString;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class UserAgentFactory {

    private static final String USER_AGENT_FORMAT = "%s/%s +%s";

    private final Package packageInfo;
    private final ProjectPropertiesReader propertiesReader;

    public UserAgentFactory() {
        this(UserAgentFactory.class.getPackage(), new ProjectPropertiesReader());
    }

    protected UserAgentFactory(Package packageInfo, ProjectPropertiesReader propertiesReader) {
        this.packageInfo = packageInfo;
        this.propertiesReader = propertiesReader;
    }

    public String getUserAgent() {
        String applicationName = optionalString(getApplicationName()).orElseGet(propertiesReader::getApplicationName);
        String applicationVersion = optionalString(getApplicationVersion()).orElseGet(propertiesReader::getApplicationVersion);
        String applicationHomepage = optionalString(getApplicationHomepage()).orElseGet(propertiesReader::getApplicationHomepage);
        if (applicationName != null && applicationVersion != null && applicationHomepage != null) {
            return String.format(USER_AGENT_FORMAT, applicationName, applicationVersion, applicationHomepage);
        } else {
            String message = "Can't determine User Agent from values: " + Arrays.asList(applicationName, applicationVersion, applicationHomepage);
            throw new OpenMusicScrobblerException(message);
        }
    }

    private String getApplicationName() {
        return getPackage().getImplementationTitle();
    }

    private String getApplicationVersion() {
        return getPackage().getImplementationVersion();
    }

    private String getApplicationHomepage() {
        return getPackage().getImplementationVendor();
    }

    private Optional<String> optionalString(String input) {
        return OptionalString.ofNullableOrBlank(input);
    }

    private Package getPackage() {
        return this.packageInfo;
    }
}
