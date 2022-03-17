package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientFactory;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientMock;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientWrapper;
import nl.slindenau.openmusicscrobbler.model.MusicRelease;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@ExtendWith(MockitoExtension.class)
class DiscogsServiceTest {

    // The example user collection on discogs contains very happy music :)
    private static final List<Release> EXPECTED_RELEASES_IN_COLLECTION = Arrays.asList(
            new Release("Stres D.A. / Depresija / III. Kategorija", "Stres D.A. / Depresija / III. Kategorija"),
            new Release("Problemi", "Nije Po Jus-u"),
            new Release("Distress / Odpadki Civilizacije", "Od Danas Do Sutra / Zaklenjena Vrata"));

    private static final String DISCOGS_USERNAME = "example";
    private static final String USER_COLLECTION_FILE_NAME = "example-user-collection.json";
    private static final String USER_COLLECTION_FOLDER_FILE_NAME = "example-user-collection-folder.json";
    private static final String USER_COLLECTION_RELEASES_FILE_NAME = "example-user-collection-folder-releases.json";

    private DiscogsService service;

    @Mock
    private DiscogsClientFactory discogsClientFactory;
    @Mock
    private MusicReleaseService musicReleaseService;

    @BeforeEach
    void setUp() {
        setupDiscogsClientFactory();
        service = new DiscogsService(discogsClientFactory, musicReleaseService);
    }

    private void setupDiscogsClientFactory() {
        DiscogsClientMock clientMock = new DiscogsClientMock();
        clientMock.setUserCollectionFileName(USER_COLLECTION_FILE_NAME);
        clientMock.setUserCollectionFolderFileName(USER_COLLECTION_FOLDER_FILE_NAME);
        clientMock.setUserCollectionFolderReleasesFileName(USER_COLLECTION_RELEASES_FILE_NAME);
        DiscogsClientWrapper clientWrapper = new DiscogsClientWrapper(clientMock.getClient());
        when(discogsClientFactory.getClient()).thenReturn(clientWrapper);
    }

    @Test
    void getUserCollection() {
        ReleaseCollection userCollection = service.getUserCollection(DISCOGS_USERNAME);
        // check that all returned releases are in our expected list
        userCollection.releases().forEach(this::assertReleaseIsExpected);
        // check that all our expected release are in the actual list
        EXPECTED_RELEASES_IN_COLLECTION.forEach(expectedRelease -> assertReleaseIsInActual(expectedRelease, userCollection));
    }

    private void assertReleaseIsInActual(Release expectedRelease, ReleaseCollection userCollection) {
        Optional<MusicRelease> expectedInActual = userCollection.releases().stream()
                .filter(musicRelease -> musicRelease.artist().equalsIgnoreCase(expectedRelease.artist))
                .filter(musicRelease -> musicRelease.title().equalsIgnoreCase(expectedRelease.title))
                .findAny();
        Assertions.assertTrue(expectedInActual.isPresent(), "Expected release not found: " + expectedRelease);
    }

    private void assertReleaseIsExpected(MusicRelease musicRelease) {
        String actualArtist = musicRelease.artist();
        String actualTitle = musicRelease.title();
        Release actualRelease = new Release(actualArtist, actualTitle);
        Assertions.assertTrue(EXPECTED_RELEASES_IN_COLLECTION.contains(actualRelease), "Release not expected: " + actualRelease);
    }

    private record Release(String artist, String title) {

        @Override
        public String toString() {
            return String.format("Artist=[%s], Title=[%s]", artist, title);
        }
    }
}