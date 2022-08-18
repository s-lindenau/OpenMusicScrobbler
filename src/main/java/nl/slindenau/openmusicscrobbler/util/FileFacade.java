package nl.slindenau.openmusicscrobbler.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Facade around the static java nio File/Path api
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class FileFacade {

    public BufferedReader newBufferedReader(Path file) throws IOException {
        return Files.newBufferedReader(file);
    }

    public BufferedWriter newBufferedWriter(Path file) throws IOException {
        return Files.newBufferedWriter(file);
    }

    public boolean exists(Path path) {
        return Files.exists(path);
    }

    public boolean isDirectory(Path path) {
        return Files.isDirectory(path);
    }

    public Path get(String path) {
        return Paths.get(path);
    }
}
