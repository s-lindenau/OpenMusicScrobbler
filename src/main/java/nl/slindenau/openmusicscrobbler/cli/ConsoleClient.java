package nl.slindenau.openmusicscrobbler.cli;

import de.umass.lastfm.scrobble.ScrobbleResult;
import nl.slindenau.openmusicscrobbler.SystemProperties;
import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Artist;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Release;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Tracklist;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientFactory;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientWrapper;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import nl.slindenau.openmusicscrobbler.service.DiscogsService;
import nl.slindenau.openmusicscrobbler.service.LastFmService;

import java.util.Date;
import java.util.Optional;

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

    // todo: clean up class
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
        Release selectedRelease = discogsService.getRelease(releaseId);
        if (selectedRelease.isError()) {
            handleError(selectedRelease);
        } else {
            handleRelease(selectedRelease);
        }
    }

    private void handleRelease(Release release) {
        String releaseArtist = release.artists.stream().findFirst().map(Artist::getName).orElse("unknown artist");
        release.tracklist.stream().map(track -> decorateTrack(track, releaseArtist)).forEach(this::printLine);

        printEmptyLine();
        printLine("Scrobble release to Last.fm?");
        boolean cancel = askCommand(CANCEL_COMMAND);
        if (!cancel) {
            LastFmClientWrapper lastFmClient = new LastFmClientFactory().getClient();
            scrobbleTracks(lastFmClient, release, releaseArtist);
            printLine("Scrobble complete!");
        }
    }

    private void scrobbleTracks(LastFmClientWrapper lastFmClient, Release release, String releaseArtist) {
        String releaseTitle = release.title;
        Optional<Tracklist> firstTrack = release.tracklist.stream().findFirst();
        if (firstTrack.isPresent()) {
            Tracklist track = firstTrack.get();
            String trackName = track.title;
            // todo: scrobble entire release on correct date per track
            ScrobbleResult result = lastFmClient.scrobbleTrack(releaseArtist, releaseTitle.trim(), trackName.trim(), new Date());
            System.out.println(result);
        }
    }

    private void printReleases(ReleaseCollection collectionReleases) {
        collectionReleases.releases().stream().map(this::decorateRelease).forEach(this::printLine);
    }

    private String decorateRelease(MusicRelease release) {
        int releaseId = release.id();
        String title = release.title();
        String format = release.format();
        String artist = release.artist();
        return String.format("%s: %s - %s (%s)", releaseId, artist, title, format);
    }

    private String decorateTrack(Tracklist track, String artist) {
        String position = track.position;
        String title = track.title;
        String duration = track.duration;
        return String.format("%s: %s - %s (%s)", position, artist, title, duration);
    }

    private String getDiscogsUsername() {
        SystemProperties systemProperties = new SystemProperties();
        String usernameProperty = systemProperties.getDiscogsUsername();
        if (usernameProperty != null) {
            return usernameProperty;
        }
        return readConsoleTextInput("Discogs Username");
    }

    private void handleError(DiscogsApiResponse apiResponse) {
        printLine("Error message: " + apiResponse.getErrorMessage());
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
