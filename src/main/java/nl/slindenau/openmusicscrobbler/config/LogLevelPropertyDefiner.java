package nl.slindenau.openmusicscrobbler.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.core.PropertyDefinerBase;
import org.slf4j.LoggerFactory;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LogLevelPropertyDefiner extends PropertyDefinerBase {

    private static final Level DEFAULT_LEVEL = Level.INFO;
    private static final Level DEBUG_LEVEL = Level.ALL;

    @Override
    public String getPropertyValue() {
        try {
            return getLogLevel().toString();
        } catch (Exception ex) {
            // logging during the initialization phase will be replayed
            LoggerFactory.getLogger(this.getClass()).warn(ex.getMessage(), ex);
            return DEFAULT_LEVEL.toString();
        }
    }

    private Level getLogLevel() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        boolean isDebug = applicationProperties.isDebugEnabled();
        if(isDebug) {
            return DEBUG_LEVEL;
        } else {
            String configuredLogLevel = applicationProperties.getLogLevel();
            return Level.toLevel(configuredLogLevel, DEFAULT_LEVEL);
        }
    }
}
