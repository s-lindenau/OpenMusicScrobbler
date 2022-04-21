package nl.slindenau.openmusicscrobbler.cli;

import nl.slindenau.openmusicscrobbler.config.SystemProperties;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.DateTimeService;
import nl.slindenau.openmusicscrobbler.service.DiscogsService;
import nl.slindenau.openmusicscrobbler.service.LastFmService;

import java.time.Instant;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ConsoleClient extends AbstractConsoleClient {

    private static final String EXIT_COMMAND = "exit";
    private static final String CANCEL_COMMAND = "cancel";

    private final DiscogsService discogsService;
    private final LastFmService lastFmService;

    public ConsoleClient() {
        this.discogsService = new DiscogsService();
        this.lastFmService = new LastFmService();
    }

    // todo: configure logging to file

    public void run() {
        boolean exit = false;
        while (!exit) {
            try {
                mainApplicationLoop();
            } catch (OpenMusicScrobblerException ex) {
                handleException(ex);
            } catch (Exception ex) {
                ex.printStackTrace();
                handleException(ex);
            }
            exit = askCommand(EXIT_COMMAND);
        }
        closeConsoleClient();
    }

    private void mainApplicationLoop() {
        String discogsUsername = getDiscogsUsername();
        printLine("Fetching user collection: " + discogsUsername);
        ReleaseCollection userCollection = discogsService.getUserCollection(discogsUsername);
        handleCollection(userCollection);
    }

    private void handleCollection(ReleaseCollection userCollection) {
        printReleases(userCollection);
        printEmptyLine();
        Integer releaseId = readConsoleNumberInput("Select release ID to continue");
        MusicRelease selectedRelease = discogsService.getRelease(userCollection, releaseId);
        handleRelease(selectedRelease);
    }

    private void handleRelease(MusicRelease release) {
        printEmptyLine();
        printLine("Selected release " + decorateRelease(release));
        printLine("Tracklist");
        release.getAllTracks().stream().map(this::decorateTrack).forEach(this::printLine);
        printEmptyLine();
        printLine("Scrobble release to Last.fm?");
        boolean cancel = askCommand(CANCEL_COMMAND);
        if (!cancel) {
            scrobbleTracks(release);
            printLine("Scrobble complete!");
        }
    }

    private void scrobbleTracks(MusicRelease release) {
        // todo: replace 'release' with selected part(s) to scrobble
        Instant firstTrackScrobbleAt = getFirstTrackScrobbleDateFromUserInput(release);
        lastFmService.scrobbleTracks(release, firstTrackScrobbleAt);
    }

    private Instant getFirstTrackScrobbleDateFromUserInput(MusicRelease release) {
        printEmptyLine();
        String message = "When was the last track played? Leave empty by pressing [Enter] for 'just now', or enter when the last track finished (format: %s)";
        String input = readConsoleTextInput(String.format(message, DateTimeService.DATE_TIME_FORMAT), 0);
        if (!isEmpty(input)) {
            return getFirstTrackScrobbleDateRelativeTo(release, new DateTimeService().parseInstant(input));
        }
        return getFirstTrackScrobbleDateFromCurrentTime(release);
    }

    private boolean isEmpty(String input) {
        return input == null || input.isEmpty();
    }

    private Instant getFirstTrackScrobbleDateFromCurrentTime(MusicRelease release) {
        return getFirstTrackScrobbleDateRelativeTo(release, Instant.now());
    }

    private Instant getFirstTrackScrobbleDateRelativeTo(MusicRelease release, Instant lastTrackEndedAt) {
        // todo: replace with length of selected part(s) to scrobble
        long totalPlayTime = lastFmService.getTotalPlayTimeInSeconds(release);
        return lastTrackEndedAt.minusSeconds(totalPlayTime);
    }

    private void printReleases(ReleaseCollection collectionReleases) {
        collectionReleases.releases().stream().map(this::decorateRelease).forEach(this::printLine);
    }

    private String decorateRelease(MusicRelease release) {
        int releaseId = release.id();
        String title = release.title();
        String format = release.format();
        String artist = release.artist();
        return String.format("%02d: %s - %s (%s)", releaseId, artist, title, format);
    }

    private String decorateTrack(Track track) {
        String position = track.position();
        String artist = track.artist();
        String title = track.title();
        String duration = track.duration();
        return String.format("%2s: %s - %s (%s)", position, artist, title, duration);
    }

    private String getDiscogsUsername() {
        SystemProperties systemProperties = new SystemProperties();
        String usernameProperty = systemProperties.getDiscogsUsername();
        if (usernameProperty != null) {
            return usernameProperty;
        }
        return readConsoleTextInput("Discogs Username");
    }

    private void handleException(Exception exception) {
        printLine("Error message: " + exception.getMessage());
    }

    private boolean askCommand(String command) {
        String message = String.format("Press [Enter] to continue or type [%s] to stop", command);
        String input = readConsoleTextInput(message, 0);
        return command.equalsIgnoreCase(input);
    }
}
