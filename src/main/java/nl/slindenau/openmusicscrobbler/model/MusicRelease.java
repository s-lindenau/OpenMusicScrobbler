package nl.slindenau.openmusicscrobbler.model;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A music release (Album, Single, Vinyl, Tape etc.)
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public record MusicRelease(int id, int discogsId, String artist, String title, String format, Collection<ReleasePart> releaseParts) {

    public Collection<Track> getAllTracks() {
        return releaseParts.stream()
                .map(ReleasePart::tracks)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
