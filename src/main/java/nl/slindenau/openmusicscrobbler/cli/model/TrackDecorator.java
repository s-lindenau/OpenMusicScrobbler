package nl.slindenau.openmusicscrobbler.cli.model;

import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.util.OptionalString;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public record TrackDecorator(Track track) {

    private static final String TRACK_FORMAT_WITHOUT_DURATION = "%2s: %s - %s";
    private static final String TRACK_FORMAT_WITH_DURATION = TRACK_FORMAT_WITHOUT_DURATION + " (%s)";

    @Override
    public String toString() {
        return decorateTrack(track);
    }

    private String decorateTrack(Track track) {
        String position = track.position();
        String artist = track.artist();
        String title = track.title();
        String duration = track.duration();
        if (isEmpty(duration)) {
            return String.format(TRACK_FORMAT_WITHOUT_DURATION, position, artist, title);
        } else {
            return String.format(TRACK_FORMAT_WITH_DURATION, position, artist, title, duration);
        }
    }

    private boolean isEmpty(String input) {
        return OptionalString.ofNullableOrBlank(input).isEmpty();
    }
}
