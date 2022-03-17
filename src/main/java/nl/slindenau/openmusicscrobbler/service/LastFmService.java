package nl.slindenau.openmusicscrobbler.service;

import de.umass.lastfm.scrobble.ScrobbleResult;
import nl.slindenau.openmusicscrobbler.SystemProperties;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientFactory;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientWrapper;
import nl.slindenau.openmusicscrobbler.model.LastFmScrobbleResult;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmService {
    private final LastFmClientFactory lastFmClientFactory;
    private final SystemProperties systemProperties = new SystemProperties();

    public LastFmService() {
        this.lastFmClientFactory = new LastFmClientFactory();
    }

    public LastFmService(LastFmClientFactory lastFmClientFactory) {
        this.lastFmClientFactory = lastFmClientFactory;
    }

    public void scrobbleTracks(MusicRelease release, Instant firstTrackStartedAt) {
        scrobbleTracks(release, firstTrackStartedAt, release.getAllTracks());
    }

    public void scrobbleTracks(MusicRelease release, Instant firstTrackStartedAt, ReleasePart... releaseParts) {
        for (ReleasePart releasePart : releaseParts) {
            scrobbleTracks(release, firstTrackStartedAt, releasePart.getAllTracks());
        }
    }

    public void scrobbleTracks(MusicRelease release, Instant firstTrackStartedAt, Track... tracks) {
        scrobbleTracks(release, firstTrackStartedAt, Arrays.asList(tracks));
    }

    private void scrobbleTracks(MusicRelease release, Instant firstTrackStartedAt, Collection<Track> tracks) {
        String releaseTitle = release.title();
        String releaseArtist = release.artist();
        int totalPlayTimeInSeconds = getTotalPlayTimeInSeconds(tracks);
        Instant scrobbleEnd = firstTrackStartedAt.plusSeconds(totalPlayTimeInSeconds);
        checkTotalPlayTimePossible(firstTrackStartedAt, scrobbleEnd);
        int secondsSinceFirstTrack = 0;
        for (Track track : tracks) {
            String trackName = track.title();
            // we scrobble once the track is completed, so add the track time before we scrobble
            secondsSinceFirstTrack += track.lengthInSeconds();
            Instant trackScrobbleAt = getTrackScrobbleAt(firstTrackStartedAt, secondsSinceFirstTrack);
            LastFmScrobbleResult result = scrobbleTrack(releaseArtist, releaseTitle.trim(), trackName.trim(), trackScrobbleAt);
            System.out.println(result);
        }
    }

    private void checkTotalPlayTimePossible(Instant firstTrackStartedAt, Instant scrobbleEnd) {
        if (scrobbleEnd.isAfter(Instant.now())) {
            throw new OpenMusicScrobblerException("Can't scrobble tracks at requested start date: " + Date.from(firstTrackStartedAt));
        }
    }

    private Instant getTrackScrobbleAt(Instant firstTrackScrobbleAt, int secondsSinceFirstTrack) {
        return firstTrackScrobbleAt.plusSeconds(secondsSinceFirstTrack);
    }

    public LastFmScrobbleResult scrobbleTrack(String artist, String album, String trackName, Instant scrobbleAtTime) {
        if (systemProperties.isDebugEnabled()) {
            System.out.printf("Scrobble track: %s - %s: %s (%s)%n", artist, album, trackName, Date.from(scrobbleAtTime));
            return new LastFmScrobbleResult("Debug mode: scrobble not sent to API");
        }
        ScrobbleResult scrobbleResult = getLastFmClient().scrobbleTrack(artist, album, trackName, scrobbleAtTime);
        return new LastFmScrobbleResult(scrobbleResult);
    }

    private LastFmClientWrapper getLastFmClient() {
        return lastFmClientFactory.getClient();
    }

    public int getTotalPlayTimeInSeconds(MusicRelease release) {
        return getTotalPlayTimeInSeconds(release.getAllTracks());
    }

    public int getTotalPlayTimeInSeconds(Collection<Track> tracks) {
        return tracks.stream().mapToInt(Track::lengthInSeconds).sum();
    }
}
