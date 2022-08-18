package nl.slindenau.openmusicscrobbler.cli.model;

import nl.slindenau.openmusicscrobbler.model.MusicReleaseBasicInformation;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public record ReleaseDecorator(MusicReleaseBasicInformation musicRelease) {

    private static final String RELEASE_FORMAT_BASE = "%02d: %s - %s";
    private static final String RELEASE_FORMAT_WITH_YEAR = RELEASE_FORMAT_BASE + " (%s, %s)";
    private static final String RELEASE_FORMAT_WITHOUT_YEAR = RELEASE_FORMAT_BASE + " (%s)";

    @Override
    public String toString() {
        return decorateRelease(musicRelease);
    }

    private String decorateRelease(MusicReleaseBasicInformation release) {
        int releaseId = release.id();
        String title = release.title();
        String format = release.format();
        String artist = release.artist();
        Integer year = release.year();
        if (year == null || year == 0) {
            return String.format(RELEASE_FORMAT_WITHOUT_YEAR, releaseId, artist, title, format);
        } else {
            return String.format(RELEASE_FORMAT_WITH_YEAR, releaseId, artist, title, format, year);
        }
    }

}
