package nl.slindenau.openmusicscrobbler.cli;

import de.umass.lastfm.scrobble.ScrobbleResult;
import nl.slindenau.openmusicscrobbler.Constants;
import nl.slindenau.openmusicscrobbler.SystemProperties;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientFactory;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientWrapper;
import nl.slindenau.openmusicscrobbler.discogs.model.DiscogsApiResponse;
import nl.slindenau.openmusicscrobbler.discogs.model.Pagination;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.BasicInformation;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.CollectionFolder;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.CollectionRelease;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.CollectionReleases;
import nl.slindenau.openmusicscrobbler.discogs.model.collection.UserCollection;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Artist;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Format;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Release;
import nl.slindenau.openmusicscrobbler.discogs.model.release.Tracklist;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientFactory;
import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientWrapper;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.Date;
import java.util.Optional;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ConsoleClient {

    private static final String EXIT_COMMAND = "exit";
    private static final String CANCEL_COMMAND = "cancel";

    public static void main(String[] args) {
        new ConsoleClient().run();
    }

    // todo: clean up class
    // todo: configure logging to file

    public void run() {
        TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal<?> terminal = textIO.getTextTerminal();
        boolean exit = false;
        while (!exit) {
            try {
                mainApplicationLoop(textIO, terminal);
            } catch (Exception ex) {
                ex.printStackTrace();
                handleException(ex, terminal);
            }
            exit = askCommand(textIO, EXIT_COMMAND);
        }
        textIO.dispose();
    }

    private void mainApplicationLoop(TextIO textIO, TextTerminal<?> terminal) {
        String discogsUsername = getDiscogsUsername(textIO);
        DiscogsClientWrapper discogsClient = new DiscogsClientFactory().getClient();
        UserCollection userCollection = discogsClient.getUserCollection(discogsUsername);

        if (userCollection.isError()) {
            handleError(userCollection, terminal);
        } else {
            handleCollection(textIO, terminal, discogsUsername, discogsClient);
        }
    }

    private void handleCollection(TextIO textIO, TextTerminal<?> terminal, String discogsUsername, DiscogsClientWrapper discogsClient) {
        terminal.println("Fetching user collection: " + discogsUsername);
        String folderId = Constants.DISCOGS_USER_COLLECTION_PUBLIC_FOLDER_ID;
        CollectionFolder folder = discogsClient.getFolder(discogsUsername, folderId);
        if (folder.isError()) {
            handleError(folder, terminal);
        } else {
            CollectionReleases collectionReleases = discogsClient.getReleases(discogsUsername, folderId);
            printReleases(terminal, collectionReleases);
            Pagination pagination = collectionReleases.getPagination();
            for (int nextPage = 2; nextPage <= pagination.getPages(); nextPage++) {
                CollectionReleases collectionReleasesNextPage = discogsClient.getReleases(discogsUsername, folderId, pagination, nextPage);
                terminal.println();
                printReleases(terminal, collectionReleasesNextPage);
            }

            terminal.println();
            Integer releaseId = textIO.newIntInputReader().read("Select release ID to continue");
            Release release = discogsClient.getRelease(String.valueOf(releaseId));
            String releaseArtist = release.artists.stream().findFirst().map(Artist::getName).orElse("unknown artist");
            release.tracklist.stream().map(track -> decorateTrack(track, releaseArtist)).forEach(terminal::println);

            terminal.println();
            terminal.println("Scrobble release to Last.fm?");
            boolean cancel = askCommand(textIO, CANCEL_COMMAND);
            if (!cancel) {
                LastFmClientWrapper lastFmClient = new LastFmClientFactory().getClient();
                scrobbleTracks(lastFmClient, release, releaseArtist);
                terminal.println("Scrobble complete!");
            }
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

    private void printReleases(TextTerminal<?> terminal, CollectionReleases collectionReleases) {
        Pagination pagination = collectionReleases.getPagination();
        terminal.println(String.format("Collection releases on page %s/%s", pagination.getPage(), pagination.getPages()));
        terminal.println("-------------------------------");
        collectionReleases.getReleases().stream().map(this::decorateRelease).forEach(terminal::println);
    }

    private String decorateRelease(CollectionRelease release) {
        int releaseId = release.getId();
        BasicInformation basicInformation = release.getBasic_information();
        String title = basicInformation.getTitle();
        String format = basicInformation.getFormats().stream().findFirst().map(Format::getName).orElse("unknown format");
        String artist = basicInformation.getArtists().stream().findFirst().map(Artist::getName).orElse("unknown artist");
        return String.format("%s: %s - %s (%s)", releaseId, artist, title, format);
    }

    private String decorateTrack(Tracklist track, String artist) {
        String position = track.position;
        String title = track.title;
        String duration = track.duration;
        return String.format("%s: %s - %s (%s)", position, artist, title, duration);
    }

    private String getDiscogsUsername(TextIO textIO) {
        SystemProperties systemProperties = new SystemProperties();
        String usernameProperty = systemProperties.getDiscogsUsername();
        if (usernameProperty != null) {
            return usernameProperty;
        }
        return textIO.newStringInputReader().read("Discogs Username");
    }

    private void handleError(DiscogsApiResponse apiResponse, TextTerminal<?> terminal) {
        terminal.println("Error message: " + apiResponse.getErrorMessage());
    }

    private void handleException(Exception exception, TextTerminal<?> terminal) {
        terminal.println("Error message: " + exception.getMessage());
    }

    private boolean askCommand(TextIO textIO, String command) {
        String message = String.format("Press [Enter] to continue or type [%s] to stop", command);
        String input = textIO.newStringInputReader().withMinLength(0).read(message);
        return command.equalsIgnoreCase(input);
    }
}
