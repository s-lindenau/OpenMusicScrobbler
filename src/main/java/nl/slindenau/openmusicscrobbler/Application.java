package nl.slindenau.openmusicscrobbler;

import nl.slindenau.openmusicscrobbler.cli.ConfigurationWizardClient;
import nl.slindenau.openmusicscrobbler.cli.ConsoleClient;
import nl.slindenau.openmusicscrobbler.web.WebApplication;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class Application {

    private static final String CONSOLE_APPLICATION = "console";
    private static final String CONFIG_APPLICATION = "config";
    private static final String SERVER_APPLICATION = "server";
    private static final String DEFAULT_APPLICATION_TO_RUN = CONSOLE_APPLICATION;

    public static void main(String[] commandLineArguments) {
        configureLogging();
        String applicationToRun = getApplicationToRun(commandLineArguments);
        switch (applicationToRun) {
            case CONSOLE_APPLICATION -> new ConsoleClient().run();
            case CONFIG_APPLICATION -> new ConfigurationWizardClient().run();
            case SERVER_APPLICATION -> new WebApplication().run(commandLineArguments);
            default -> throw new IllegalArgumentException("Unknown command: " + applicationToRun);
        }
    }

    private static void configureLogging() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private static String getApplicationToRun(String[] commandLineArguments) {
        if (commandLineArguments == null || commandLineArguments.length == 0) {
            return DEFAULT_APPLICATION_TO_RUN;
        }
        return commandLineArguments[0];
    }
}
