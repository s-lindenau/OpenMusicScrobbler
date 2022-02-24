package nl.slindenau.openmusicscrobbler.discogs.client;

import com.adamdonegan.Discogs4J.client.DiscogsClient;
import nl.slindenau.openmusicscrobbler.Constants;
import nl.slindenau.openmusicscrobbler.SystemProperties;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsClientFactory {

    public DiscogsClientWrapper getClient() {
        DiscogsClient discogsClient = new DiscogsClient(Constants.USER_AGENT);
        Boolean isDebugEnabled = new SystemProperties().isDebugEnabled();
        discogsClient.setDebugEnabled(isDebugEnabled);
        return new DiscogsClientWrapper(discogsClient);
    }

}
