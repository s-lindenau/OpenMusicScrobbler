package nl.slindenau.openmusicscrobbler.lastfm.client;

import de.umass.lastfm.Authenticator;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Session;
import nl.slindenau.openmusicscrobbler.config.SystemProperties;
import nl.slindenau.openmusicscrobbler.config.UserAgentFactory;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmClientFactory {

    private static final String LAST_FM_SECURE_ENDPOINT = "https://ws.audioscrobbler.com/2.0/";

    public LastFmClientWrapper getClient() {
        String userAgent = new UserAgentFactory().getUserAgent();
        Caller.getInstance().setUserAgent(userAgent);
        Caller.getInstance().setApiRootUrl(LAST_FM_SECURE_ENDPOINT);

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
        throw new OpenMusicScrobblerException("Could not create Last.fm session. Check authentication details!");
    }
}
