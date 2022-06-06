package nl.slindenau.openmusicscrobbler;

import nl.slindenau.openmusicscrobbler.cli.ConsoleClient;
import nl.slindenau.openmusicscrobbler.cli.EncryptPasswordClient;
import nl.slindenau.openmusicscrobbler.web.WebApplication;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class Application {

    private static final String CONSOLE_APPLICATION = "console";
    private static final String ENCRYPT_APPLICATION = "encrypt";
    private static final String SERVER_APPLICATION = "server";
    private static final String DEFAULT_APPLICATION_TO_RUN = CONSOLE_APPLICATION;

    public static void main(String[] commandLineArguments) {
        String applicationToRun = getApplicationToRun(commandLineArguments);
        switch (applicationToRun) {
            case ENCRYPT_APPLICATION -> new EncryptPasswordClient().run();
            case CONSOLE_APPLICATION -> new ConsoleClient().run();
            case SERVER_APPLICATION -> new WebApplication().run(commandLineArguments);
            default -> throw new IllegalArgumentException("Unknown command: " + applicationToRun);
        }
    }

    private static String getApplicationToRun(String[] commandLineArguments) {
        if (commandLineArguments == null || commandLineArguments.length == 0) {
            return DEFAULT_APPLICATION_TO_RUN;
        }
        return commandLineArguments[0];
    }
}
