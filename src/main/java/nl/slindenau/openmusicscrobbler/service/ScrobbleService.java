package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientSupplier;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;

import java.time.Instant;
import java.util.Collection;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ScrobbleService {

    private final LastFmService lastFmService;

    public ScrobbleService() {
        this(new LastFmService());
    }

    protected ScrobbleService(LastFmService lastFmService) {
        this.lastFmService = lastFmService;
    }

    public void scrobbleTracks(MusicRelease release, Instant firstTrackStartedAt) {
        lastFmService.scrobbleTracks(release, firstTrackStartedAt, release.getAllTracks(), getClient());
    }

    public void scrobbleTracks(MusicRelease release, Instant firstTrackStartedAt, ReleasePart... releaseParts) {
        LastFmClientSupplier client = getClient();
        for (ReleasePart releasePart : releaseParts) {
            lastFmService.scrobbleTracks(release, firstTrackStartedAt, releasePart.getAllTracks(), client);
        }
    }

    public void scrobbleTracks(MusicRelease release, Instant firstTrackStartedAt, Collection<Track> tracks) {
        lastFmService.scrobbleTracks(release, firstTrackStartedAt, tracks, getClient());
    }

    public long getTotalPlayTimeInSeconds(Collection<Track> tracks) {
        return lastFmService.getTotalPlayTimeInSeconds(tracks);
    }

    private LastFmClientSupplier getClient() {
        return lastFmService.getLastFmClient();
    }
}
