package nl.slindenau.openmusicscrobbler.model;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A music release (Album, Single, Vinyl, Tape etc.)<br/>
 * Tracks are stored in (possibly multiple) {@link ReleasePart} objects.
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public record MusicRelease(MusicReleaseBasicInformation basicInformation, Collection<ReleasePart> releaseParts) {

    public Collection<Track> getAllTracks() {
        return releaseParts.stream()
                .map(ReleasePart::getAllTracks)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
