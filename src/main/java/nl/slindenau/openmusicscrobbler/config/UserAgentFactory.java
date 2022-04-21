package nl.slindenau.openmusicscrobbler.config;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class UserAgentFactory {

    private static final String APPLICATION_NAME = "Open Music Scrobbler";
    private static final String USER_AGENT_FORMAT = "%s/%s +%s";

    public String getUserAgent() {
        String applicationName = getApplicationName();
        String applicationVersion = getApplicationVersion();
        String applicationHomepage = getApplicationHomepage();
        if (applicationName != null && applicationVersion != null && applicationHomepage != null) {
            return String.format(USER_AGENT_FORMAT, applicationName, applicationVersion, applicationHomepage);
        }
        return APPLICATION_NAME;
    }

    private String getApplicationName() {
        return this.getClass().getPackage().getImplementationTitle();
    }

    private String getApplicationVersion() {
        return this.getClass().getPackage().getImplementationVersion();
    }

    private String getApplicationHomepage() {
        return this.getClass().getPackage().getImplementationVendor();
    }
}
