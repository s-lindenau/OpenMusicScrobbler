package nl.slindenau.openmusicscrobbler.web;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dropwizard.logging.LoggingFactory;
import io.dropwizard.logging.LoggingUtil;

/**
 * To use the <code>logback.xml</code> configuration file.<br/>
 * https://gist.github.com/fedotxxl/0b3cc5e5e4eaeffdcde1f9834796edc6
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LogbackLoggingFactory implements LoggingFactory {

    @JsonIgnore
    private final LoggerContext loggerContext;
    @JsonIgnore
    private final ContextInitializer contextInitializer;

    public LogbackLoggingFactory() {
        this.loggerContext = LoggingUtil.getLoggerContext();
        this.contextInitializer = new ContextInitializer(loggerContext);
    }

    @Override
    public void configure(MetricRegistry metricRegistry, String name) {
        try {
            loggerContext.reset();
            contextInitializer.autoConfig();
        } catch (JoranException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        loggerContext.stop();
    }

    @Override
    public void reset() {
        loggerContext.reset();
    }
}