package nl.slindenau.openmusicscrobbler.lastfm.client;

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

    private final LastFmClientFacade clientFacade;

    public LastFmClientFactory() {
        this(new LastFmClientFacade());
    }

    protected LastFmClientFactory(LastFmClientFacade clientFacade) {
        this.clientFacade = clientFacade;
    }

    public LastFmClientWrapper getClient() {
        String userAgent = new UserAgentFactory().getUserAgent();
        getCaller().setUserAgent(userAgent);
        getCaller().setApiRootUrl(LAST_FM_SECURE_ENDPOINT);

        ApplicationProperties applicationProperties = new ApplicationProperties();
        String key = applicationProperties.getLastFmApiKey();
        String secret = applicationProperties.getLastFmApiSecret();
        String user = applicationProperties.getLastFmUsername();
        String password = applicationProperties.getLastFmPassword();
        if (password == null || password.isBlank()) {
            throw newMissingCredentialsException();
        }
        Session session = clientFacade.getSession(user, password, key, secret);
        if (session == null) {
            throw newMissingCredentialsException();
        }
        boolean isDebugEnabled = applicationProperties.isDebugEnabled();
        getCaller().getLogger().setLevel(isDebugEnabled ? Level.ALL : Level.OFF);
        return new LastFmClientWrapper(clientFacade, session);
    }

    private Caller getCaller() {
        return clientFacade.getCaller();
    }

    private OpenMusicScrobblerException newMissingCredentialsException() {
        return new OpenMusicScrobblerException("Could not create Last.fm session. Check authentication details!");
    }
}
