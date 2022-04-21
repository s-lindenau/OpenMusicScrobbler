package nl.slindenau.openmusicscrobbler.discogs.client;

import com.adamdonegan.Discogs4J.client.DiscogsClient;
import nl.slindenau.openmusicscrobbler.config.SystemProperties;
import nl.slindenau.openmusicscrobbler.config.UserAgentFactory;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsClientFactory {

    public DiscogsClientWrapper getClient() {
        String userAgent = new UserAgentFactory().getUserAgent();
        DiscogsClient discogsClient = new DiscogsClient(userAgent);
        Boolean isDebugEnabled = new SystemProperties().isDebugEnabled();
        discogsClient.setDebugEnabled(isDebugEnabled);
        return new DiscogsClientWrapper(discogsClient);
    }

}
