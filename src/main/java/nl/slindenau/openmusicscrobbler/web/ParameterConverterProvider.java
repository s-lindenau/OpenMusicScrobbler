package nl.slindenau.openmusicscrobbler.web;

import nl.slindenau.openmusicscrobbler.lastfm.validation.LastFmScrobbleDateConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Register custom parameter converters for specific types
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ParameterConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> parameterType, Type genericType, Annotation[] annotations) {
        if (isOfType(parameterType, Instant.class) && isAnnotatedWith(annotations, LastFmScrobbleDateConstraint.class)) {
            return getLastFmScrobbleDateParameterConverter();
        }
        return null;
    }

    @SuppressWarnings("SameParameterValue")
    private <T> boolean isOfType(Class<T> parameterType, Class<?> targetType) {
        return parameterType == targetType;
    }

    @SuppressWarnings("SameParameterValue")
    private boolean isAnnotatedWith(Annotation[] annotations, Class<? extends Annotation> targetAnnotation) {
        return Arrays.stream(annotations)
                .map(Annotation::annotationType)
                .anyMatch(annotationType -> annotationType.isAssignableFrom(targetAnnotation));
    }

    @SuppressWarnings("unchecked")
    private <T> ParamConverter<T> getLastFmScrobbleDateParameterConverter() {
        return (ParamConverter<T>)new LastFmScrobbleDateParameterConverter();
    }

    private static class LastFmScrobbleDateParameterConverter implements ParamConverter<Instant> {

        private static final DateTimeFormatter SCROBBLE_INPUT_DATE_TIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME;
        private final Logger logger = LoggerFactory.getLogger(LastFmScrobbleDateParameterConverter.class);

        @Override
        public Instant fromString(String value) {
            if (value == null || value.isBlank()) {
                return null;
            }
            debugLog(() -> "Input: " + value);
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(value, SCROBBLE_INPUT_DATE_TIME_FORMAT);
            Instant instant = zonedDateTime.toInstant();
            debugLog(() -> "Output: " + instant);
            return instant;
        }

        @Override
        public String toString(Instant value) {
            if (value == null) {
                return null;
            }
            return SCROBBLE_INPUT_DATE_TIME_FORMAT.format(value);
        }

        private void debugLog(Supplier<String> message) {
            if (logger.isDebugEnabled()) {
                logger.debug(message.get());
            }
        }
    }
}
