package nl.slindenau.openmusicscrobbler.web;

import io.dropwizard.Configuration;
import io.dropwizard.logging.LoggingFactory;

/**
 * @author slindenau
 * @author davidvollmar
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DropWizardConfiguration extends Configuration {

    private LoggingFactory loggingFactory;

    @Override
    public synchronized LoggingFactory getLoggingFactory() {
        if (loggingFactory == null) {
            loggingFactory = new LogbackLoggingFactory();
        }
        return loggingFactory;
    }
}
