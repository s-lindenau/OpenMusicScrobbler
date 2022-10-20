package nl.slindenau.openmusicscrobbler.cli;

import nl.slindenau.openmusicscrobbler.cli.model.ReleaseDecorator;
import nl.slindenau.openmusicscrobbler.cli.model.TrackDecorator;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.DateTimeService;
import nl.slindenau.openmusicscrobbler.service.DiscogsService;
import nl.slindenau.openmusicscrobbler.service.ScrobbleService;
import nl.slindenau.openmusicscrobbler.service.search.SearchService;
import nl.slindenau.openmusicscrobbler.util.OptionalString;

import java.time.Instant;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ConsoleClient extends AbstractConsoleClient {

    private static final String EXIT_COMMAND = "exit";
    private static final String CANCEL_COMMAND = "cancel";

    private final DiscogsService discogsService;
    private final ScrobbleService scrobbleService;
    private final SearchService searchService;

    public ConsoleClient() {
        this.discogsService = new DiscogsService();
        this.scrobbleService = new ScrobbleService();
        this.searchService = new SearchService();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            try {
                mainApplicationLoop();
            } catch (OpenMusicScrobblerException ex) {
                handleException(ex);
            } catch (Exception ex) {
                handleException(ex);
                getLogger().error(ex.getMessage(), ex);
            }
            exit = askCommand(EXIT_COMMAND);
        }
        closeConsoleClient();
    }

    private void mainApplicationLoop() {
        String discogsUsername = getDiscogsUsername();
        printLine("Fetching user collection: " + discogsUsername);
        ReleaseCollection userCollection = discogsService.getUserCollection(discogsUsername);
        printEmptyLine();
        printLine("-- Main Menu --");
        printLine("1: Browse collection");
        printLine("2: Search collection");
        Integer menuItem = readConsoleNumberInput("Select menu item to continue");
        printEmptyLine();
        switch (menuItem) {
            case 1 -> handleCollection(userCollection);
            case 2 -> searchCollection(userCollection);
        }
    }

    private void searchCollection(ReleaseCollection userCollection) {
        searchService.loadCollection(userCollection);
        ReleaseCollection matchingSearch = handleSearchInput();
        while (matchingSearch.releases().isEmpty()) {
            printLine("No results, please try again.");
            matchingSearch = handleSearchInput();
        }
        printEmptyLine();
        handleCollection(matchingSearch);
    }

    private ReleaseCollection handleSearchInput() {
        String searchQueryText = readConsoleTextInput("Enter search query to filter collection items");
        return searchService.findMatching(searchQueryText);
    }

    private void handleCollection(ReleaseCollection collectionReleases) {
        printReleases(collectionReleases);
        printEmptyLine();
        Integer releaseId = readConsoleNumberInput("Select release ID to continue");
        MusicRelease selectedRelease = discogsService.getRelease(collectionReleases, releaseId);
        handleRelease(selectedRelease);
    }

    private void handleRelease(MusicRelease release) {
        printEmptyLine();
        printLine("Selected release " + new ReleaseDecorator(release.basicInformation()));
        printLine("Tracklist");
        printTracks(release.getAllTracks());
        Collection<Track> tracks = handleReleaseSelection(release);
        while (tracks.isEmpty()) {
            tracks = handleReleaseSelection(release);
        }
        printEmptyLine();
        printLine("Selected tracks: ");
        printTracks(tracks);
        printEmptyLine();
        printLine("Scrobble tracks to Last.fm?");
        boolean cancel = askCommand(CANCEL_COMMAND);
        if (!cancel) {
            scrobbleTracks(release, tracks);
            printLine("Scrobble complete!");
        }
    }

    private void printTracks(Collection<Track> tracks) {
        tracks.stream().map(TrackDecorator::new).map(TrackDecorator::toString).forEach(this::printLine);
    }

    private Collection<Track> handleReleaseSelection(MusicRelease release) {
        String parts = release.releaseParts().stream().map(ReleasePart::partIdentification).collect(Collectors.joining(", "));
        if (isEmpty(parts)) {
            return release.getAllTracks();
        }
        printEmptyLine();
        printLine("Release parts: " + parts);
        String selectedParts = readConsoleOptionalTextInput("Select parts to scrobble, or leave empty by pressing [Enter] for all tracks");
        if (isEmpty(selectedParts)) {
            return release.getAllTracks();
        }
        return getSelectedTracks(release, selectedParts);
    }

    private Collection<Track> getSelectedTracks(MusicRelease release, String selectedPartsInput) {
        List<Track> selected = new LinkedList<>();
        String selectedParts = selectedPartsInput.toLowerCase().trim();
        for (ReleasePart releasePart : release.releaseParts()) {
            String partIdentification = releasePart.partIdentification().toLowerCase();
            if (selectedParts.contains(partIdentification)) {
                selected.addAll(releasePart.getAllTracks());
            }
        }
        return selected;
    }

    private void scrobbleTracks(MusicRelease release, Collection<Track> tracks) {
        Instant firstTrackScrobbleAt = getFirstTrackScrobbleDateFromUserInput(tracks);
        scrobbleService.scrobbleTracks(release, firstTrackScrobbleAt, tracks);
    }

    private Instant getFirstTrackScrobbleDateFromUserInput(Collection<Track> tracks) {
        printEmptyLine();
        String message = "When was the last track played? Leave empty by pressing [Enter] for 'just now', or enter when the last track finished (format: %s)";
        String input = readConsoleOptionalTextInput(String.format(message, DateTimeService.DATE_TIME_FORMAT));
        if (!isEmpty(input)) {
            return getFirstTrackScrobbleDateRelativeTo(tracks, new DateTimeService().parseInstant(input));
        }
        return getFirstTrackScrobbleDateFromCurrentTime(tracks);
    }

    private boolean isEmpty(String input) {
        return input == null || input.isEmpty() || input.isBlank();
    }

    private Instant getFirstTrackScrobbleDateFromCurrentTime(Collection<Track> tracks) {
        return getFirstTrackScrobbleDateRelativeTo(tracks, Instant.now());
    }

    private Instant getFirstTrackScrobbleDateRelativeTo(Collection<Track> tracks, Instant lastTrackEndedAt) {
        long totalPlayTime = scrobbleService.getTotalPlayTimeInSeconds(tracks);
        return lastTrackEndedAt.minusSeconds(totalPlayTime);
    }

    private void printReleases(ReleaseCollection collectionReleases) {
        collectionReleases.releases().stream().map(ReleaseDecorator::new).map(ReleaseDecorator::toString).forEach(this::printLine);
    }

    private String getDiscogsUsername() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        Optional<String> usernameProperty = OptionalString.ofNullableOrBlank(applicationProperties.getDiscogsUsername());
        return usernameProperty.orElseGet(() -> readConsoleTextInput("Discogs Username"));
    }

    private void handleException(Exception exception) {
        printLine("Error message: " + exception.getMessage());
    }

    private boolean askCommand(String command) {
        String message = String.format("Press [Enter] to continue or type [%s] to stop", command);
        String input = readConsoleOptionalTextInput(message);
        return command.equalsIgnoreCase(input);
    }
}
