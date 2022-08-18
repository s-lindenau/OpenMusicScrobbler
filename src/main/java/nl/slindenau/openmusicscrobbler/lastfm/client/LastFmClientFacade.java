package nl.slindenau.openmusicscrobbler.lastfm.client;

import de.umass.lastfm.Authenticator;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Session;
import de.umass.lastfm.Track;
import de.umass.lastfm.scrobble.ScrobbleData;
import de.umass.lastfm.scrobble.ScrobbleResult;

/**
 * Facade around the static lastfm api client library.
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmClientFacade {

    public Caller getCaller() {
        return Caller.getInstance();
    }

    public Session getSession(String user, String password, String key, String secret) {
        return Authenticator.getMobileSession(user, password, key, secret);
    }

    public ScrobbleResult scrobbleTrack(ScrobbleData scrobbleData, Session session) {
        return Track.scrobble(scrobbleData, session);
    }

}
