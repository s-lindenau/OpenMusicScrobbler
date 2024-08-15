package nl.slindenau.openmusicscrobbler.lastfm.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.Duration;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = LastFmScrobbleDateConstraintValidator.class)
public @interface LastFmScrobbleDateConstraint {

    Duration MAX_HISTORIC_SCROBBLE = Duration.ofDays(14);

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
