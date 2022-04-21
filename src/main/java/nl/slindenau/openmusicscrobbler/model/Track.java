package nl.slindenau.openmusicscrobbler.model;

import java.time.Duration;

/**
 * A music recording
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public record Track(String position, String artist, String title, String duration, Duration length) {
}
