package nl.slindenau.openmusicscrobbler;

import nl.slindenau.openmusicscrobbler.exception.LoggingUncaughtExceptionHandler;
import nl.slindenau.openmusicscrobbler.web.WebApplication;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class Application {

    public static void main(String[] commandLineArguments) {
        configureApplication();
        new WebApplication().run(commandLineArguments);
    }

    private static void configureApplication() {
        configureExceptionHandling();
        configureLogging();
    }

    private static void configureExceptionHandling() {
       Thread.setDefaultUncaughtExceptionHandler(new LoggingUncaughtExceptionHandler());
    }

    private static void configureLogging() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}
