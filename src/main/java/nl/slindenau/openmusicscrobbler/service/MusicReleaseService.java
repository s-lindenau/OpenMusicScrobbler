package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsArtistNameCollector;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Release;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Tracklist;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.MusicReleaseBasicInformation;
import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.model.TrackType;
import nl.slindenau.openmusicscrobbler.service.releasepart.ReleasePartParserStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class MusicReleaseService {

    private static final String TRACK_WITH_INDEX_TITLE_FORMAT = "%s: %s";

    private final TrackDurationService trackDurationService = new TrackDurationService();
    private final Logger logger = LoggerFactory.getLogger(MusicReleaseService.class);
    private final ApplicationProperties applicationProperties;

    public MusicReleaseService() {
        this(new ApplicationProperties());
    }

    public MusicReleaseService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public MusicRelease createRelease(MusicReleaseBasicInformation musicRelease, Release discogsRelease) {
        String releaseArtist = discogsRelease.artists.stream().collect(new DiscogsArtistNameCollector());
        Collection<Track> trackList = new LinkedList<>();
        processTracks(discogsRelease, releaseArtist, trackList);
        String releaseTitle = discogsRelease.title;
        Collection<ReleasePart> parts = createParts(trackList);
        return createMusicReleaseWithTrackList(musicRelease, releaseArtist, releaseTitle, parts);
    }

    private void processTracks(Release discogsRelease, String releaseArtist, Collection<Track> trackList) {
        discogsRelease.tracklist.stream()
                .map(tracklist -> createTracks(tracklist, releaseArtist))
                .flatMap(Collection::stream)
                .forEach(trackList::add);
    }

    private List<Track> createTracks(Tracklist tracklist, String releaseArtist) {
        if (isTrack(tracklist)) {
            return Collections.singletonList(createTrack(tracklist, releaseArtist));
        } else if (isTrackIndex(tracklist)) {
            return createTracksFromIndex(tracklist, releaseArtist);
        }
        // other track types are not supported (headings for example)
        return Collections.emptyList();
    }

    private boolean isTrack(Tracklist tracklist) {
        return isTrackOfType(TrackType.TRACK, tracklist);
    }

    private boolean isTrackIndex(Tracklist tracklist) {
        return isTrackOfType(TrackType.INDEX, tracklist);
    }

    private boolean isTrackOfType(TrackType track, Tracklist tracklist) {
        return track.isTrackType(tracklist.type);
    }

    private List<Track> createTracksFromIndex(Tracklist tracklist, String releaseArtist) {
        Objects.requireNonNull(tracklist.subTracks);
        List<Track> tracksInIndex = new ArrayList<>(tracklist.subTracks.size());
        String indexTitle = tracklist.title;
        processSubTracks(tracklist.subTracks, releaseArtist, indexTitle, tracksInIndex);
        return tracksInIndex;
    }

    private void processSubTracks(List<Tracklist> subTracks, String releaseArtist, String indexTitle, List<Track> tracksInIndex) {
        subTracks.stream()
                .map(subTrack -> createTrack(subTrack, releaseArtist, indexTitle))
                .forEach(tracksInIndex::add);
    }

    private Track createTrack(Tracklist tracklist, String releaseArtist) {
        return createTrack(tracklist, releaseArtist, null);
    }

    private Track createTrack(Tracklist tracklist, String releaseArtist, String indexTitle) {
        String position = tracklist.position;
        String title = getTrackTitle(tracklist, indexTitle);
        String duration = tracklist.duration;
        Duration length = getTrackLength(duration);
        String trackArtist = getTrackArtist(tracklist, releaseArtist);
        return new Track(position, trackArtist, title, duration, length);
    }

    private String getTrackTitle(Tracklist tracklist, String indexTitle) {
        if (indexTitle == null || indexTitle.isBlank()) {
            return tracklist.title;
        } else {
            return String.format(TRACK_WITH_INDEX_TITLE_FORMAT, indexTitle, tracklist.title);
        }
    }

    private Duration getTrackLength(String duration) {
        try {
            return trackDurationService.parseTrackLength(duration);
        } catch (OpenMusicScrobblerException ex) {
            logger.debug("Using default track length", ex);
            return this.applicationProperties.getDiscogsDefaultTrackLength();
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
        return new ReleasePartParserStrategy().getParseStrategy(trackList).createParts(trackList);
    }

    private MusicRelease createMusicReleaseWithTrackList(MusicReleaseBasicInformation musicRelease, String releaseArtist, String releaseTitle, Collection<ReleasePart> releaseParts) {
        MusicReleaseBasicInformation updatedBasicInformation = getUpdatedBasicInformation(musicRelease, releaseArtist, releaseTitle);
        return new MusicRelease(updatedBasicInformation, releaseParts);
    }

    private MusicReleaseBasicInformation getUpdatedBasicInformation(MusicReleaseBasicInformation musicRelease, String releaseArtist, String releaseTitle) {
        return new MusicReleaseBasicInformation(musicRelease.id(), musicRelease.discogsId(), releaseArtist, releaseTitle, musicRelease.format(), musicRelease.year());
    }
}
