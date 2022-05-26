package nl.slindenau.openmusicscrobbler.util;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ResourceFileReader {

    public String readFileContents(Class<?> resourceClass, String filename) {
        try {
            return doReadFileContents(resourceClass, filename);
        } catch (URISyntaxException | IOException e) {
            throw new OpenMusicScrobblerException(e);
        }
    }

    private String doReadFileContents(Class<?> resourceClass, String filename) throws URISyntaxException, IOException {
        URL resource = resourceClass.getResource(filename);
        if (resource == null) {
            throw new IllegalArgumentException(filename + " is not a resource file in path " + resourceClass.getPackage().getName());
        }
        Path path = Paths.get(resource.toURI());
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
