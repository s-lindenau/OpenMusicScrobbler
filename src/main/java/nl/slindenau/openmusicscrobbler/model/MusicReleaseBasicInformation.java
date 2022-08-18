package nl.slindenau.openmusicscrobbler.model;

/**
 * Basic information about a music release.<br/>
 * For a release with tracks see {@link MusicRelease}
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public record MusicReleaseBasicInformation(int id, int discogsId, String artist, String title, String format, Integer year) {

}
