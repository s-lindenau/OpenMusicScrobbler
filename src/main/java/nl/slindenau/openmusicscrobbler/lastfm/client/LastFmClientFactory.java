package nl.slindenau.openmusicscrobbler.lastfm.client;

import de.umass.lastfm.Caller;
import de.umass.lastfm.Session;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.config.UserAgentFactory;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.util.OptionalString;

import java.util.logging.Level;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmClientFactory {

    private static final String LAST_FM_SECURE_ENDPOINT = "https://ws.audioscrobbler.com/2.0/";

    private final LastFmClientFacade clientFacade;
    private final UserAgentFactory userAgentFactory;
    private final ApplicationProperties applicationProperties;

    public LastFmClientFactory() {
        this(new LastFmClientFacade(), new UserAgentFactory(), new ApplicationProperties());
    }

    protected LastFmClientFactory(LastFmClientFacade clientFacade, UserAgentFactory userAgentFactory, ApplicationProperties applicationProperties) {
        this.clientFacade = clientFacade;
        this.userAgentFactory = userAgentFactory;
        this.applicationProperties = applicationProperties;
    }

    public LastFmClientWrapper getClient() {
        String userAgent = userAgentFactory.getUserAgent();
        getCaller().setUserAgent(userAgent);
        getCaller().setApiRootUrl(LAST_FM_SECURE_ENDPOINT);

        String key = applicationProperties.getLastFmApiKey();
        String secret = applicationProperties.getLastFmApiSecret();
        String user = applicationProperties.getLastFmUsername();
        String password = applicationProperties.getLastFmPassword();
        if (isEmpty(password)) {
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

    private boolean isEmpty(String input) {
        return OptionalString.ofNullableOrBlank(input).isEmpty();
    }
}
