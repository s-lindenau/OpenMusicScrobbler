package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.model.MusicReleaseBasicInformation;
import nl.slindenau.openmusicscrobbler.model.ReleaseCollection;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
class DiscogsServiceCollectionTest extends DiscogsServiceTest {

    // The example user collection on discogs contains very happy music :)
    private static final List<Release> EXPECTED_RELEASES_IN_COLLECTION = Arrays.asList(
            new Release("Stres D.A. / Depresija / III. Kategorija", "Stres D.A. / Depresija / III. Kategorija"),
            new Release("Problemi", "Nije Po Jus-u"),
            new Release("Distress / Odpadki Civilizacije", "Od Danas Do Sutra / Zaklenjena Vrata"));

    private static final String DISCOGS_USERNAME = "example";
    private static final String USER_COLLECTION_FILE_NAME = "example-user-collection.json";
    private static final String USER_COLLECTION_FOLDER_FILE_NAME = "example-user-collection-folder.json";
    private static final String USER_COLLECTION_RELEASES_FILE_NAME = "example-user-collection-folder-releases.json";

    @Override
    protected String getUserCollectionFolderReleasesFileName() {
        return USER_COLLECTION_RELEASES_FILE_NAME;
    }

    @Override
    protected String getUserCollectionFolderFileName() {
        return USER_COLLECTION_FOLDER_FILE_NAME;
    }

    @Override
    protected String getUserCollectionFileName() {
        return USER_COLLECTION_FILE_NAME;
    }

    @Override
    protected String getReleaseFileName() {
        // not used; release detail level is not part of the discogs user collection
        return null;
    }

    @Override
    protected void runDiscogsServiceTest() {
        ReleaseCollection userCollection = getService().getUserCollection(DISCOGS_USERNAME);
        // check that all returned releases are in our expected list
        userCollection.releases().forEach(this::assertReleaseIsExpected);
        // check that all our expected releases are in the actual list
        EXPECTED_RELEASES_IN_COLLECTION.forEach(expectedRelease -> assertReleaseIsInActual(expectedRelease, userCollection));
    }

    private void assertReleaseIsInActual(Release expectedRelease, ReleaseCollection userCollection) {
        Optional<MusicReleaseBasicInformation> expectedInActual = userCollection.releases().stream()
                .filter(musicRelease -> musicRelease.artist().equalsIgnoreCase(expectedRelease.artist))
                .filter(musicRelease -> musicRelease.title().equalsIgnoreCase(expectedRelease.title))
                .findAny();
        Assertions.assertTrue(expectedInActual.isPresent(), "Expected release not found: " + expectedRelease);
    }

    private void assertReleaseIsExpected(MusicReleaseBasicInformation musicRelease) {
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