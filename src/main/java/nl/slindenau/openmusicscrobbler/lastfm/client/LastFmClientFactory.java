package nl.slindenau.openmusicscrobbler.lastfm.client;

import de.umass.lastfm.Authenticator;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Session;
import nl.slindenau.openmusicscrobbler.Constants;
import nl.slindenau.openmusicscrobbler.SystemProperties;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmClientFactory {

    public LastFmClientWrapper getClient() {
        Caller.getInstance().setUserAgent(Constants.USER_AGENT);
        Caller.getInstance().setApiRootUrl(Constants.LAST_FM_SECURE_ENDPOINT);

        SystemProperties systemProperties = new SystemProperties();
        String key = systemProperties.getLastFmApiKey();
        String secret = systemProperties.getLastFmApiSecret();
        String user = systemProperties.getLastFmUsername();
        String password = systemProperties.getLastFmPassword();
        if (password == null) {
            throwMissingCredentialsException();
        }
        Session session = Authenticator.getMobileSession(user, password, key, secret);
        if (session == null) {
            throwMissingCredentialsException();
        }
        return new LastFmClientWrapper(session);
    }

    private void throwMissingCredentialsException() {
        throw new RuntimeException("Could not create Last.fm session. Check authentication details!");
    }
}
