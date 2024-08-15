package nl.slindenau.openmusicscrobbler.service;

import de.umass.lastfm.scrobble.ScrobbleResult;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientFactory;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientSupplier;
import nl.slindenau.openmusicscrobbler.lastfm.validation.LastFmScrobbleDateConstraint;
import nl.slindenau.openmusicscrobbler.lastfm.model.LastFmScrobbleResult;
import nl.slindenau.openmusicscrobbler.lastfm.model.LastFmScrobbleResultHolder;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmService {

    private static final Duration LAST_FM_MAX_HISTORIC_SCROBBLE = LastFmScrobbleDateConstraint.MAX_HISTORIC_SCROBBLE;

    private final Logger logger = LoggerFactory.getLogger(LastFmService.class);
    private final ApplicationProperties applicationProperties;
    private final LastFmClientFactory lastFmClientFactory;

    public LastFmService() {
        this(new LastFmClientFactory(), new ApplicationProperties());
    }

    protected LastFmService(LastFmClientFactory lastFmClientFactory, ApplicationProperties applicationProperties) {
        this.lastFmClientFactory = lastFmClientFactory;
        this.applicationProperties = applicationProperties;
    }

    public LastFmClientSupplier getLastFmClient() {
        return new LastFmClientSupplier(lastFmClientFactory::getClient);
    }

    public LastFmScrobbleResultHolder scrobbleTracks(MusicRelease release, Instant firstTrackStartedAt, Collection<Track> tracks, LastFmClientSupplier client) {
        LastFmScrobbleResultHolder resultHolder = new LastFmScrobbleResultHolder();
        String releaseTitle = release.basicInformation().title();
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
            LastFmScrobbleResult result = scrobbleTrack(trackArtist.trim(), releaseTitle.trim(), trackName.trim(), trackScrobbleAt, client);
            resultHolder.addResult(result);
            logger.info(String.valueOf(result));
        }
        return resultHolder;
    }

    private void checkTotalPlayTimePossible(Instant firstTrackStartedAt, Instant scrobbleEnd) {
        if (scrobbleEnd.isAfter(Instant.now())) {
            String message = "Can't scrobble tracks at start date: %s, end date would be in the future: %s";
            throw new OpenMusicScrobblerException(String.format(message, Date.from(firstTrackStartedAt), Date.from(scrobbleEnd)));
        }
        Instant maximumHistoricScrobble = Instant.now().minus(LAST_FM_MAX_HISTORIC_SCROBBLE);
        if (firstTrackStartedAt.isBefore(maximumHistoricScrobble)) {
            String message = "Can't scrobble tracks at start date: %s, Last.fm only processes historic scrobbles from the past %s days";
            throw new OpenMusicScrobblerException(String.format(message, Date.from(firstTrackStartedAt), LAST_FM_MAX_HISTORIC_SCROBBLE.toDays()));
        }
    }

    private Instant getTrackScrobbleAt(Instant firstTrackScrobbleAt, long secondsSinceFirstTrack) {
        return firstTrackScrobbleAt.plusSeconds(secondsSinceFirstTrack);
    }

    private LastFmScrobbleResult scrobbleTrack(String artist, String album, String trackName, Instant scrobbleAtTime, LastFmClientSupplier clientSupplier) {
        if (applicationProperties.isDebugEnabled()) {
            String message = String.format("Scrobble track: [%s - %s] from Album: [%s] (on %s)", artist, trackName, album, Date.from(scrobbleAtTime));
            logger.info(message);
            return new LastFmScrobbleResult("Debug mode: scrobble not sent to Last.fm");
        }
        ScrobbleResult scrobbleResult = clientSupplier.getClient().scrobbleTrack(artist, album, trackName, scrobbleAtTime);
        return new LastFmScrobbleResult(scrobbleResult);
    }

    public long getTotalPlayTimeInSeconds(Collection<Track> tracks) {
        return tracks.stream()
                .map(Track::length)
                .mapToLong(Duration::toSeconds)
                .sum();
    }
}
