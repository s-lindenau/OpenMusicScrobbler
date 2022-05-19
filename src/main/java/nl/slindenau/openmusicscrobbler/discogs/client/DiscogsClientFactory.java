package nl.slindenau.openmusicscrobbler.discogs.client;

import com.adamdonegan.Discogs4J.client.DiscogsClient;
import nl.slindenau.openmusicscrobbler.config.SystemProperties;
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
        SystemProperties systemProperties = new SystemProperties();
        Boolean isDebugEnabled = systemProperties.isDebugEnabled();
        discogsClient.setDebugEnabled(isDebugEnabled);
        discogsClient.setConnectTimeout(systemProperties.getDiscogsConnectionTimeout());
        discogsClient.setReadTimeout(systemProperties.getDiscogsReadTimeout());
        logUserAgent(isDebugEnabled, userAgent);
        return new DiscogsClientWrapper(discogsClient);
    }

    private void logUserAgent(Boolean isDebugEnabled, String userAgent) {
        if (isDebugEnabled) {
            logger.info("User-Agent: " + userAgent);
        }
    }

}
