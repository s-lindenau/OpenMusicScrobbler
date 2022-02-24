package nl.slindenau.openmusicscrobbler.model;

import java.util.Collection;

/**
 * A music release (Album, Single, Vinyl, Tape etc.)
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public record MusicRelease(int id, String artist, String title, String format, Collection<ReleasePart> releaseParts) {
}
