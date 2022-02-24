package nl.slindenau.openmusicscrobbler.model;

/**
 * A music recording
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public record Track(ReleasePart releasePart, String artist, String title, int lengthInSeconds) {
}
