package nl.slindenau.openmusicscrobbler.cli;

import de.umass.util.StringUtilities;

/**
 * The last.fm client supports providing the password in MD5.<br/>
 * This class will securely store that password.
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class EncryptPasswordClient extends AbstractConsoleClient {

    @Override
    public void run() {
        String passwordInput = readConsolePasswordInput("Enter Last.fm password");
        String encryptedPassword = StringUtilities.md5(passwordInput);
        printLine("Encrypted password: " + encryptedPassword);
        // todo: properly encrypt the md5 hash
        readConsoleTextInput("Press [Enter] to exit", 0);
        closeConsoleClient();
    }
}
