package nl.slindenau.openmusicscrobbler.config;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@ExtendWith(MockitoExtension.class)
class UserAgentFactoryTest {

    private static final String USER_AGENT_FORMAT_REGEX = ".+/.+ \\+.+";

    @Mock
    private Package mockPackage;

    @Mock
    private ProjectPropertiesReader mockPropertiesReader;

    @Test
    @DisplayName("Test with the real class Package and Project Properties reader")
    void getUserAgentDefault() {
        UserAgentFactory userAgentFactory = new UserAgentFactory();
        assertUserAgentMatches(userAgentFactory);
    }

    @Test
    @DisplayName("Test that when no values are available we get an Exception")
    void getUserAgentMissingAllValues() {
        UserAgentFactory userAgentFactory = new UserAgentFactory(mockPackage, mockPropertiesReader);
        assertThrows(userAgentFactory::getUserAgent);
    }

    @Test
    @DisplayName("Test that when some values are unavailable we get an Exception")
    void getUserAgentMissingSomeValues() {
        setupPackage("", "Not missing", "");
        UserAgentFactory userAgentFactory = new UserAgentFactory(mockPackage, mockPropertiesReader);
        assertThrows(userAgentFactory::getUserAgent);
    }

    @Test
    @DisplayName("Test that when values from the Package are available, we never try to read the properties file")
    void getUserAgentFromPackage() {
        setupPackage("Title", "Vendor", "Version");
        UserAgentFactory userAgentFactory = new UserAgentFactory(mockPackage, mockPropertiesReader);
        assertUserAgentMatches(userAgentFactory);
        verify(mockPropertiesReader, never()).getApplicationName();
        verify(mockPropertiesReader, never()).getApplicationVersion();
        verify(mockPropertiesReader, never()).getApplicationHomepage();
    }

    private void setupPackage(String title, String vendor, String version) {
        when(mockPackage.getImplementationTitle()).thenReturn(title);
        when(mockPackage.getImplementationVendor()).thenReturn(vendor);
        when(mockPackage.getImplementationVersion()).thenReturn(version);
    }

    @Test
    @DisplayName("Test that when values from the Package are not available, we read the properties file")
    void getUserAgentFromProjectProperties() {
        setupPropertiesReader();
        UserAgentFactory userAgentFactory = new UserAgentFactory(mockPackage, mockPropertiesReader);
        assertUserAgentMatches(userAgentFactory);
    }

    private void setupPropertiesReader() {
        when(mockPropertiesReader.getApplicationName()).thenReturn("Name");
        when(mockPropertiesReader.getApplicationVersion()).thenReturn("Version");
        when(mockPropertiesReader.getApplicationHomepage()).thenReturn("Homepage");
    }

    private void assertUserAgentMatches(UserAgentFactory userAgentFactory) {
        String userAgent = userAgentFactory.getUserAgent();
        String message = "User agent [%s] does not match expected format regex %s";
        Assertions.assertTrue(userAgent.matches(USER_AGENT_FORMAT_REGEX), String.format(message, userAgent, USER_AGENT_FORMAT_REGEX));
    }

    private void assertThrows(Executable executable) {
        Assertions.assertThrows(OpenMusicScrobblerException.class, executable, "Expected exception for invalid User Agent was not thrown");
    }
}