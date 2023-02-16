package nl.slindenau.openmusicscrobbler.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LoggingUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    public LoggingUncaughtExceptionHandler() {
        this.defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        logger.error(throwable.getMessage(), throwable);
        if (hasOtherPreviousUncaughtExceptionHandler()) {
            // if there was a previous default handler, call that to terminate the application
            this.defaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
        } else {
            // no previous default handler, terminate the application
            throwable.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    private boolean hasOtherPreviousUncaughtExceptionHandler() {
        return defaultUncaughtExceptionHandler != null && !isLoggingHandler();
    }

    private boolean isLoggingHandler() {
        return defaultUncaughtExceptionHandler instanceof LoggingUncaughtExceptionHandler;
    }
}
