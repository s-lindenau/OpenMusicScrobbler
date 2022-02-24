package nl.slindenau.openmusicscrobbler.model;

/**
 * A music recording
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public record Track(String position, String artist, String title, String duration, int lengthInSeconds) {
}
