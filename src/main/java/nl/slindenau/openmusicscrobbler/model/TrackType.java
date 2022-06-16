package nl.slindenau.openmusicscrobbler.model;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public enum TrackType {
    TRACK,
    HEADING,
    INDEX,

    ;

    public boolean isTrackType(String type) {
        return this.name().equalsIgnoreCase(type);
    }
}
