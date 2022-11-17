package nl.slindenau.openmusicscrobbler.lastfm.client;

import de.umass.lastfm.Caller;
import de.umass.lastfm.Session;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.config.UserAgentFactory;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@ExtendWith(MockitoExtension.class)
class LastFmClientFactoryTest {

    private static final String LAST_FM_SECURE_ENDPOINT = "https://ws.audioscrobbler.com/2.0/";
    private static final String USER_AGENT = "user agent";

    private LastFmClientFactory lastFmClientFactory;

    @Mock
    private Caller caller;

    @Mock
    private LastFmClientFacade lastFmClientFacade;

    @Mock
    private UserAgentFactory userAgentFactory;

    @Mock
    private ApplicationProperties applicationProperties;

    @BeforeEach
    void setUp() {
        lastFmClientFactory = new LastFmClientFactory(lastFmClientFacade, userAgentFactory, applicationProperties);
        setupUserAgent();
        setupCaller();
    }

    private void setupUserAgent() {
        when(userAgentFactory.getUserAgent()).thenReturn(USER_AGENT);
    }

    private void setupCaller() {
        when(lastFmClientFacade.getCaller()).thenReturn(caller);
    }

    private void setupCallerLogger() {
        when(caller.getLogger()).thenReturn(mock(Logger.class));
    }

    private void setupApplicationProperties(String password) {
        when(applicationProperties.getLastFmPassword()).thenReturn(password);
    }

    private void setupSession(Session session) {
        when(lastFmClientFacade.getSession(any(), any(), any(), any())).thenReturn(session);
    }

    @Test
    void testClient() {
        setupCallerLogger();
        setupSession(mock(Session.class));
        setupApplicationProperties("correct password");
        LastFmClientWrapper client = lastFmClientFactory.getClient();
        Assertions.assertNotNull(client, "Client should not be null");
        verify(caller).setApiRootUrl(LAST_FM_SECURE_ENDPOINT);
        verify(caller).setUserAgent(USER_AGENT);
    }

    @Test
    void testMissingCredentials() {
        setupApplicationProperties(null);
        Assertions.assertThrows(OpenMusicScrobblerException.class, lastFmClientFactory::getClient, "Expected exception on missing credentials");
    }

    @Test
    void testInvalidCredentials() {
        setupSession(null);
        setupApplicationProperties("wrong password");
        Assertions.assertThrows(OpenMusicScrobblerException.class, lastFmClientFactory::getClient, "Expected exception on invalid credentials");
    }
}