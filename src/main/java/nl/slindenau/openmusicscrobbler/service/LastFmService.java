package nl.slindenau.openmusicscrobbler.service;

import de.umass.lastfm.scrobble.ScrobbleResult;
import nl.slindenau.openmusicscrobbler.config.SystemProperties;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientFactory;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientWrapper;
import nl.slindenau.openmusicscrobbler.model.LastFmScrobbleResult;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmService {

    private static final int LAST_FM_MAX_HISTORIC_SCROBBLE_WEEKS = 2;

    private final Logger logger = LoggerFactory.getLogger(LastFmService.class);
    private final SystemProperties systemProperties = new SystemProperties();
    private final LastFmClientFactory lastFmClientFactory;

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
        long totalPlayTimeInSeconds = getTotalPlayTimeInSeconds(tracks);
        Instant scrobbleEnd = firstTrackStartedAt.plusSeconds(totalPlayTimeInSeconds);
        checkTotalPlayTimePossible(firstTrackStartedAt, scrobbleEnd);
        long secondsSinceFirstTrack = 0;
        for (Track track : tracks) {
            String trackArtist = track.artist();
            String trackName = track.title();
            // we scrobble once the track is completed, so add the track time before we scrobble
            secondsSinceFirstTrack += track.length().toSeconds();
            Instant trackScrobbleAt = getTrackScrobbleAt(firstTrackStartedAt, secondsSinceFirstTrack);
            LastFmScrobbleResult result = scrobbleTrack(trackArtist.trim(), releaseTitle.trim(), trackName.trim(), trackScrobbleAt);
            // todo: handle result
            logger.info(String.valueOf(result));
        }
    }

    private void checkTotalPlayTimePossible(Instant firstTrackStartedAt, Instant scrobbleEnd) {
        if (scrobbleEnd.isAfter(Instant.now())) {
            String message = "Can't scrobble tracks at start date: %s, end date would be in the future: %s";
            throw new OpenMusicScrobblerException(String.format(message, Date.from(firstTrackStartedAt), Date.from(scrobbleEnd)));
        }
        Instant maximumHistoricScrobble = Instant.now().minus(LAST_FM_MAX_HISTORIC_SCROBBLE_WEEKS * 7, ChronoUnit.DAYS);
        if (firstTrackStartedAt.isBefore(maximumHistoricScrobble)) {
            String message = "Can't scrobble tracks at start date: %s, Last.fm only processes historic scrobbles from the past %s weeks";
            throw new OpenMusicScrobblerException(String.format(message, Date.from(firstTrackStartedAt), LAST_FM_MAX_HISTORIC_SCROBBLE_WEEKS));
        }
    }

    private Instant getTrackScrobbleAt(Instant firstTrackScrobbleAt, long secondsSinceFirstTrack) {
        return firstTrackScrobbleAt.plusSeconds(secondsSinceFirstTrack);
    }

    public LastFmScrobbleResult scrobbleTrack(String artist, String album, String trackName, Instant scrobbleAtTime) {
        if (systemProperties.isDebugEnabled()) {
            String message = String.format("Scrobble track: [%s - %s] from Album: [%s] (on %s)", artist, trackName, album, Date.from(scrobbleAtTime));
            logger.info(message);
            return new LastFmScrobbleResult("Debug mode: scrobble not sent to API");
        }
        ScrobbleResult scrobbleResult = getLastFmClient().scrobbleTrack(artist, album, trackName, scrobbleAtTime);
        return new LastFmScrobbleResult(scrobbleResult);
    }

    private LastFmClientWrapper getLastFmClient() {
        return lastFmClientFactory.getClient();
    }

    public long getTotalPlayTimeInSeconds(MusicRelease release) {
        return getTotalPlayTimeInSeconds(release.getAllTracks());
    }

    public long getTotalPlayTimeInSeconds(Collection<Track> tracks) {
        return tracks.stream().map(Track::length).mapToLong(Duration::toSeconds).sum();
    }
}
