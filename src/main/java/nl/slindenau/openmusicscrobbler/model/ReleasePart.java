package nl.slindenau.openmusicscrobbler.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A confined part of a release, for example:<br/>
 * - Tape side A or B<br/>
 * - CD 1 in a 3 CD box set<br/>
 * - Vinyl 1 in a 9 Vinyl box set<br/>
 * - Vinyl side A/B or one/two<br/>
 * <br/>
 * A release part can consist of multiple sub parts, and each part contains <code>1..*</code> {@link Track Tracks}
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public record ReleasePart(String partIdentification, Collection<ReleasePart> subParts, Collection<Track> tracks) {

    public Collection<Track> getAllTracks() {
        Collection<ReleasePart> allParts = new ArrayList<>(subParts);
        allParts.add(this);
        return allParts.stream()
                .map(ReleasePart::tracks)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
