package nl.slindenau.openmusicscrobbler.discogs.client;

import com.adamdonegan.Discogs4J.client.DiscogsClient;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.config.UserAgentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsClientFactory {

    private final Logger logger = LoggerFactory.getLogger(DiscogsClientFactory.class);

    private final ApplicationProperties applicationProperties;
    private final UserAgentFactory userAgentFactory;

    public DiscogsClientFactory() {
        this(new ApplicationProperties(), new UserAgentFactory());
    }

    protected DiscogsClientFactory(ApplicationProperties applicationProperties, UserAgentFactory userAgentFactory) {
        this.applicationProperties = applicationProperties;
        this.userAgentFactory = userAgentFactory;
    }

    public DiscogsClientWrapper getClient() {
        String userAgent = userAgentFactory.getUserAgent();
        logUserAgent(userAgent);

        DiscogsClient discogsClient = getDiscogsClient(userAgent);
        discogsClient.setDebugEnabled(applicationProperties.isDebugEnabled());
        discogsClient.setConnectTimeout(applicationProperties.getDiscogsConnectionTimeout());
        discogsClient.setReadTimeout(applicationProperties.getDiscogsReadTimeout());

        return new DiscogsClientWrapper(discogsClient);
    }

    private DiscogsClient getDiscogsClient(String userAgent) {
        Optional<String> discogsPersonalAccessToken = applicationProperties.getDiscogsPersonalAccessToken();
        return discogsPersonalAccessToken
                .map(personalAccessToken -> new DiscogsClient(userAgent, personalAccessToken))
                .orElseGet(() -> new DiscogsClient(userAgent));
    }

    private void logUserAgent(String userAgent) {
        logger.info("User-Agent: " + userAgent);
    }

}
