package nl.slindenau.openmusicscrobbler.exception;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class OpenMusicScrobblerException extends RuntimeException {

    public OpenMusicScrobblerException(Throwable cause) {
        super(cause);
    }

    public OpenMusicScrobblerException(String message) {
        super(message);
    }

    public OpenMusicScrobblerException(String message, Throwable cause) {
        super(message, cause);
    }
}
