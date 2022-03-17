package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsArtistNameCollector;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Release;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Tracklist;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;

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

    public MusicRelease createRelease(MusicRelease musicRelease, Release discogsRelease) {
        String releaseArtist = discogsRelease.artists.stream().collect(new DiscogsArtistNameCollector());
        Collection<Track> trackList = new LinkedList<>();
        discogsRelease.tracklist.stream().map(track -> createTrack(track, releaseArtist)).forEach(trackList::add);
        Collection<ReleasePart> parts = createParts(trackList);
        return createMusicReleaseWithTrackList(musicRelease, releaseArtist, parts);
    }

    private Track createTrack(Tracklist tracklist, String releaseArtist) {
        String position = tracklist.position;
        String title = tracklist.title;
        String duration = tracklist.duration;
        int lengthInSeconds = trackDurationService.parseTrackLength(duration);
        // todo: correctly find track artist for Various Artists albums
        return new Track(position, releaseArtist, title, duration, lengthInSeconds);
    }

    private Collection<ReleasePart> createParts(Collection<Track> trackList) {
        // todo: split parts and give the correct identification per part
        return Collections.singletonList(new ReleasePart("A", Collections.emptyList(), trackList));
    }

    private MusicRelease createMusicReleaseWithTrackList(MusicRelease musicRelease, String releaseArtist, Collection<ReleasePart> releaseParts) {
        return new MusicRelease(musicRelease.id(), musicRelease.discogsId(), releaseArtist, musicRelease.title(), musicRelease.format(), releaseParts);
    }
}
