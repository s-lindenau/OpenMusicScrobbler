package nl.slindenau.openmusicscrobbler.config;

import ch.qos.logback.core.PropertyDefinerBase;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LogLevelPropertyDefiner extends PropertyDefinerBase {
    @Override
    public String getPropertyValue() {
        return "info";
    }
}
