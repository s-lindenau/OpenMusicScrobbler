package nl.slindenau.openmusicscrobbler;

import nl.slindenau.openmusicscrobbler.cli.ConsoleClient;
import nl.slindenau.openmusicscrobbler.cli.EncryptPasswordClient;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class Application {

    private static final String CONSOLE_APPLICATION = "console";
    private static final String ENCRYPT_APPLICATION = "encrypt";
    private static final String DEFAULT_APPLICATION_TO_RUN = CONSOLE_APPLICATION;

    public static void main(String[] args) {
        String applicationToRun = getApplicationToRun(args);
        switch (applicationToRun) {
            case ENCRYPT_APPLICATION -> new EncryptPasswordClient().run();
            case CONSOLE_APPLICATION -> new ConsoleClient().run();
            default -> throw new IllegalArgumentException("Unknown command: " + applicationToRun);
        }
    }

    private static String getApplicationToRun(String[] args) {
        if (args == null || args.length == 0) {
            return DEFAULT_APPLICATION_TO_RUN;
        }
        return args[0];
    }
}
