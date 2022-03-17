package nl.slindenau.openmusicscrobbler.lastfm.client;

import de.umass.lastfm.Session;
import de.umass.lastfm.Track;
import de.umass.lastfm.scrobble.ScrobbleData;
import de.umass.lastfm.scrobble.ScrobbleResult;

import java.time.Instant;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmClientWrapper {

    private final Session session;

    public LastFmClientWrapper(Session session) {
        this.session = session;
    }

    public ScrobbleResult scrobbleTrack(String artist, String album, String track, Instant scrobbleAtTime) {
        ScrobbleData scrobbleData = new ScrobbleData();
        scrobbleData.setArtist(artist);
        scrobbleData.setAlbum(album);
        scrobbleData.setTrack(track);
        scrobbleData.setTimestamp(getTimestamp(scrobbleAtTime));
        return Track.scrobble(scrobbleData, this.session);
    }

    private int getTimestamp(Instant scrobbleAt) {
        return (int)scrobbleAt.getEpochSecond();
    }
}
