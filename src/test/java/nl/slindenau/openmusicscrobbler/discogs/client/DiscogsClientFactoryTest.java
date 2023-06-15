package nl.slindenau.openmusicscrobbler.discogs.client;

import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.config.UserAgentFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@ExtendWith(MockitoExtension.class)
class DiscogsClientFactoryTest {

    private DiscogsClientFactory discogsClientFactory;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private UserAgentFactory userAgentFactory;

    @BeforeEach
    void setUp() {
        discogsClientFactory = new DiscogsClientFactory(applicationProperties, userAgentFactory);
        setupUserAgentFactory();
    }

    private void setupUserAgentFactory() {
        when(userAgentFactory.getUserAgent()).thenReturn("test-user-agent");
    }

    @Test
    void getClientNoAuthentication() {
        when(applicationProperties.getDiscogsPersonalAccessToken()).thenReturn(Optional.empty());
        DiscogsClientWrapper client = discogsClientFactory.getClient();
        Assertions.assertFalse(client.isUsingAuthentication(), "Discogs client should be created without authentication");
    }

    @Test
    void getClientPersonalTokenAuthentication() {
        when(applicationProperties.getDiscogsPersonalAccessToken()).thenReturn(Optional.of("personal-token"));
        DiscogsClientWrapper client = discogsClientFactory.getClient();
        Assertions.assertTrue(client.isUsingAuthentication(), "Discogs client should be created with authentication");
    }
}