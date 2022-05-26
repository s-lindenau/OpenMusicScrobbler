package nl.slindenau.openmusicscrobbler.discogs.client;

import com.adamdonegan.Discogs4J.client.DiscogsClient;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.config.UserAgentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsClientFactory {

    private final Logger logger = LoggerFactory.getLogger(DiscogsClientFactory.class);

    public DiscogsClientWrapper getClient() {
        String userAgent = new UserAgentFactory().getUserAgent();
        DiscogsClient discogsClient = new DiscogsClient(userAgent);
        ApplicationProperties applicationProperties = new ApplicationProperties();
        boolean isDebugEnabled = applicationProperties.isDebugEnabled();
        discogsClient.setDebugEnabled(isDebugEnabled);
        discogsClient.setConnectTimeout(applicationProperties.getDiscogsConnectionTimeout());
        discogsClient.setReadTimeout(applicationProperties.getDiscogsReadTimeout());
        logUserAgent(isDebugEnabled, userAgent);
        return new DiscogsClientWrapper(discogsClient);
    }

    private void logUserAgent(Boolean isDebugEnabled, String userAgent) {
        if (isDebugEnabled) {
            logger.info("User-Agent: " + userAgent);
        }
    }

}
