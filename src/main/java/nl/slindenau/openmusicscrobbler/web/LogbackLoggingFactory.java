package nl.slindenau.openmusicscrobbler.web;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AsyncAppenderBase;
import ch.qos.logback.core.joran.spi.JoranException;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dropwizard.logback.AsyncAppenderBaseProxy;
import io.dropwizard.logging.LoggingFactory;
import io.dropwizard.logging.LoggingUtil;

import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * To use the <code>logback.xml</code> configuration file.<br/>
 * https://gist.github.com/fedotxxl/0b3cc5e5e4eaeffdcde1f9834796edc6
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LogbackLoggingFactory implements LoggingFactory {

    private static final ReentrantLock CHANGE_LOGGER_CONTEXT_LOCK = new ReentrantLock();
    private static final int SLEEP_WAIT_TIME_MILLIS_ON_FLUSH = 100;

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
        CHANGE_LOGGER_CONTEXT_LOCK.lock();
        try {
            loggerContext.reset();
            contextInitializer.autoConfig();
        } catch (JoranException e) {
            throw new RuntimeException(e);
        } finally {
            CHANGE_LOGGER_CONTEXT_LOCK.unlock();
        }
    }

    @Override
    public void stop() {
        // Copied from io.dropwizard.logging.DefaultLoggingFactory
        CHANGE_LOGGER_CONTEXT_LOCK.lock();
        try {
            final Logger logger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
            final Iterator<Appender<ILoggingEvent>> appenderIterator = logger.iteratorForAppenders();
            while (appenderIterator.hasNext()) {
                final Appender<ILoggingEvent> appender = appenderIterator.next();
                if (appender instanceof AsyncAppenderBase) {
                    flushAppender((AsyncAppenderBase<?>)appender);
                } else if (appender instanceof AsyncAppenderBaseProxy) {
                    flushAppender(((AsyncAppenderBaseProxy<?>)appender).getAppender());
                }
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        } finally {
            CHANGE_LOGGER_CONTEXT_LOCK.unlock();
        }
    }

    @Override
    public void reset() {
        CHANGE_LOGGER_CONTEXT_LOCK.lock();
        try {
            loggerContext.reset();
        } finally {
            CHANGE_LOGGER_CONTEXT_LOCK.unlock();
        }
    }

    @SuppressWarnings("BusyWait")
    private void flushAppender(AsyncAppenderBase<?> appender) throws InterruptedException {
        int timeWaiting = 0;
        while (timeWaiting < appender.getMaxFlushTime() && appender.getNumberOfElementsInQueue() > 0) {
            Thread.sleep(SLEEP_WAIT_TIME_MILLIS_ON_FLUSH);
            timeWaiting += SLEEP_WAIT_TIME_MILLIS_ON_FLUSH;
        }

        if (appender.getNumberOfElementsInQueue() > 0) {
            appender.addWarn(appender.getNumberOfElementsInQueue() + " events may be discarded");
        }
    }
}