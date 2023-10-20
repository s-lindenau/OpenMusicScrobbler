package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientSupplier;
import nl.slindenau.openmusicscrobbler.lastfm.model.LastFmScrobbleResultHolder;
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

    public LastFmScrobbleResultHolder scrobbleTracks(MusicRelease release, Instant firstTrackStartedAt) {
        return lastFmService.scrobbleTracks(release, firstTrackStartedAt, release.getAllTracks(), getClient());
    }

    public LastFmScrobbleResultHolder scrobbleTracks(MusicRelease release, Instant firstTrackStartedAt, ReleasePart... releaseParts) {
        LastFmClientSupplier client = getClient();
        LastFmScrobbleResultHolder resultHolder = new LastFmScrobbleResultHolder();
        for (ReleasePart releasePart : releaseParts) {
            LastFmScrobbleResultHolder resultForPart = lastFmService.scrobbleTracks(release, firstTrackStartedAt, releasePart.getAllTracks(), client);
            resultHolder.addAll(resultForPart);
        }
        return resultHolder;
    }

    public LastFmScrobbleResultHolder scrobbleTracks(MusicRelease release, Instant firstTrackStartedAt, Collection<Track> tracks) {
        return lastFmService.scrobbleTracks(release, firstTrackStartedAt, tracks, getClient());
    }

    public Instant getFirstTrackScrobbleDateRelativeTo(Collection<Track> tracks, Instant lastTrackEndedAt) {
        long totalPlayTime = getTotalPlayTimeInSeconds(tracks);
        return lastTrackEndedAt.minusSeconds(totalPlayTime);
    }

    private long getTotalPlayTimeInSeconds(Collection<Track> tracks) {
        return lastFmService.getTotalPlayTimeInSeconds(tracks);
    }

    private LastFmClientSupplier getClient() {
        return lastFmService.getLastFmClient();
    }
}
