package nl.slindenau.openmusicscrobbler.lastfm.client;

import de.umass.lastfm.Authenticator;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Session;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.config.UserAgentFactory;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;

import java.util.logging.Level;

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

        ApplicationProperties applicationProperties = new ApplicationProperties();
        String key = applicationProperties.getLastFmApiKey();
        String secret = applicationProperties.getLastFmApiSecret();
        String user = applicationProperties.getLastFmUsername();
        String password = applicationProperties.getLastFmPassword();
        if (password == null) {
            throwMissingCredentialsException();
        }
        Session session = Authenticator.getMobileSession(user, password, key, secret);
        if (session == null) {
            throwMissingCredentialsException();
        }
        boolean isDebugEnabled = applicationProperties.isDebugEnabled();
        Caller.getInstance().getLogger().setLevel(isDebugEnabled ? Level.ALL : Level.OFF);
        return new LastFmClientWrapper(session);
    }

    private void throwMissingCredentialsException() {
        throw new OpenMusicScrobblerException("Could not create Last.fm session. Check authentication details!");
    }
}
