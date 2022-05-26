package nl.slindenau.openmusicscrobbler.test;

import nl.slindenau.openmusicscrobbler.config.ApplicationProperty;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientFactory;
import nl.slindenau.openmusicscrobbler.discogs.client.DiscogsClientWrapper;

import java.util.Arrays;

/**
 * Test utility to print the JSON data of a given Discogs release by id.
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsClientReleaseDownloader {
    public static void main(String[] commandLineArguments) {
        String releaseId = getReleaseId(commandLineArguments);
        // enable debug to log the JSON response
        System.setProperty(ApplicationProperty.DEBUG.getKey(), Boolean.TRUE.toString());
        DiscogsClientWrapper client = new DiscogsClientFactory().getClient();
        client.getRelease(releaseId);
    }

    private static String getReleaseId(String[] commandLineArguments) {
        try {
            String releaseIdArgument = commandLineArguments[0];
            Integer.parseInt(releaseIdArgument);
            return releaseIdArgument;
        } catch (Exception ex) {
            String message = "Invalid release id on command line argument index 0: ";
            System.out.println(message + Arrays.asList(commandLineArguments == null ? new String[]{} : commandLineArguments));
            throw ex;
        }
    }
}
