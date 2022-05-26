package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsArtistNameCollector;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Release;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Tracklist;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.model.TrackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class MusicReleaseService {

    private final TrackDurationService trackDurationService = new TrackDurationService();
    private final Logger logger = LoggerFactory.getLogger(MusicReleaseService.class);

    public MusicRelease createRelease(MusicRelease musicRelease, Release discogsRelease) {
        String releaseArtist = discogsRelease.artists.stream().collect(new DiscogsArtistNameCollector());
        Collection<Track> trackList = new LinkedList<>();
        processTracks(discogsRelease, releaseArtist, trackList);
        String releaseTitle = discogsRelease.title;
        Collection<ReleasePart> parts = createParts(trackList);
        return createMusicReleaseWithTrackList(musicRelease, releaseArtist, releaseTitle, parts);
    }

    private void processTracks(Release discogsRelease, String releaseArtist, Collection<Track> trackList) {
        discogsRelease.tracklist.stream()
                .filter(track -> TrackType.TRACK.isTrackType(track.type))
                .map(track -> createTrack(track, releaseArtist))
                .forEach(trackList::add);
    }

    private Track createTrack(Tracklist tracklist, String releaseArtist) {
        String position = tracklist.position;
        String title = tracklist.title;
        String duration = tracklist.duration;
        Duration length = getTrackLength(duration);
        String trackArtist = getTrackArtist(tracklist, releaseArtist);
        return new Track(position, trackArtist, title, duration, length);
    }

    private Duration getTrackLength(String duration) {
        try {
            return trackDurationService.parseTrackLength(duration);
        } catch(OpenMusicScrobblerException ex) {
            logger.debug("Using default track length", ex);
            return new ApplicationProperties().getDiscogsDefaultTrackLength();
        }
    }

    private String getTrackArtist(Tracklist tracklist, String releaseArtist) {
        if (tracklist.artists == null || tracklist.artists.isEmpty()) {
            return releaseArtist;
        } else {
            return tracklist.artists.stream().collect(new DiscogsArtistNameCollector());
        }
    }

    private Collection<ReleasePart> createParts(Collection<Track> trackList) {
        // todo: split parts and give the correct identification per part
        return Collections.singletonList(new ReleasePart("A", Collections.emptyList(), trackList));
    }

    private MusicRelease createMusicReleaseWithTrackList(MusicRelease musicRelease, String releaseArtist, String releaseTitle, Collection<ReleasePart> releaseParts) {
        return new MusicRelease(musicRelease.id(), musicRelease.discogsId(), releaseArtist, releaseTitle, musicRelease.format(), releaseParts);
    }
}
