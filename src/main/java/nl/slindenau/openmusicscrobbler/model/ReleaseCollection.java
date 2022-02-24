package nl.slindenau.openmusicscrobbler.model;

import java.util.Collection;

/**
 * A collection of releases
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public record ReleaseCollection(Collection<MusicRelease> releases) {
}
