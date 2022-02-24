package nl.slindenau.openmusicscrobbler.discogs.client;

import com.adamdonegan.Discogs4J.client.DiscogsClient;
import nl.slindenau.openmusicscrobbler.Constants;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsClientFactory {

    public DiscogsClientWrapper getClient() {
        DiscogsClient discogsClient = new DiscogsClient(Constants.USER_AGENT);
        // todo: configuration property
        discogsClient.setDebugEnabled(true);
        return new DiscogsClientWrapper(discogsClient);
    }

}
