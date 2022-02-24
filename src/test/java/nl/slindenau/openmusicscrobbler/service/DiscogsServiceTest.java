package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientFactory;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientWrapperMock;
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

    private static final List<String> EXPECTED_RELEASES_IN_COLLECTION = Arrays.asList(
            "Stres D.A. / Depresija / III. Kategorija",
            "Nije Po Jus-u",
            "Od Danas Do Sutra / Zaklenjena Vrata");

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
        DiscogsClientWrapperMock client = new DiscogsClientWrapperMock();
        client.setUserCollectionFileName(USER_COLLECTION_FILE_NAME);
        client.setUserCollectionFolderFileName(USER_COLLECTION_FOLDER_FILE_NAME);
        client.setUserCollectionFolderReleasesFileName(USER_COLLECTION_RELEASES_FILE_NAME);
        when(discogsClientFactory.getClient()).thenReturn(client);
    }

    @Test
    void getUserCollection() {
        ReleaseCollection userCollection = service.getUserCollection(DISCOGS_USERNAME);
        // check that all returned releases are in our expected list
        userCollection.releases().forEach(this::assertReleaseIsExpected);
        // check that all our expected release are in the actual list
        EXPECTED_RELEASES_IN_COLLECTION.forEach(expectedRelease -> assertReleaseIsInActual(expectedRelease, userCollection));
    }

    private void assertReleaseIsInActual(String expectedRelease, ReleaseCollection userCollection) {
        Optional<MusicRelease> expectedInActual = userCollection.releases().stream()
                .filter(musicRelease -> musicRelease.title().equalsIgnoreCase(expectedRelease))
                .findAny();
        Assertions.assertTrue(expectedInActual.isPresent(), "Expected release title not found: " + expectedRelease);
    }

    private void assertReleaseIsExpected(MusicRelease musicRelease) {
        String actualTitle = musicRelease.title();
        Assertions.assertTrue(EXPECTED_RELEASES_IN_COLLECTION.contains(actualTitle), "Release title not expected: " + actualTitle);
    }
}