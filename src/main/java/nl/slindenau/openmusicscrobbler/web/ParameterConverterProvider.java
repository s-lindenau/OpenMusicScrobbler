package nl.slindenau.openmusicscrobbler.web;

import nl.slindenau.openmusicscrobbler.lastfm.validation.LastFmScrobbleDateConstraint;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Register custom parameter converters for specific types
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ParameterConverterProvider implements ParamConverterProvider {

    @SuppressWarnings("SpellCheckingInspection")
    public static final String FORM_INPUT_FIELD_LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> parameterType, Type genericType, Annotation[] annotations) {
        if (isOfType(parameterType, Instant.class) && isAnnotatedWith(annotations, LastFmScrobbleDateConstraint.class)) {
            //noinspection unchecked
            return (ParamConverter<T>)new LastFmScrobbleDateParameterConverter();
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

    private static class LastFmScrobbleDateParameterConverter implements ParamConverter<Instant> {

        private static final DateTimeFormatter FORM_INPUT_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(FORM_INPUT_FIELD_LOCAL_DATE_TIME_FORMAT);

        @Override
        public Instant fromString(String value) {
            if (value == null || value.isBlank()) {
                return null;
            }
            LocalDateTime localDateTime = LocalDateTime.parse(value, FORM_INPUT_DATE_TIME_FORMAT);
            ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
            return zonedDateTime.toInstant();
        }

        @Override
        public String toString(Instant value) {
            if (value == null) {
                return null;
            }
            return FORM_INPUT_DATE_TIME_FORMAT.format(value);
        }
    }
}
