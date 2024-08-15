package nl.slindenau.openmusicscrobbler.lastfm.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

/**
 * Validates if the requested scrobble date is definitely out-of-bounds for Last.fm.
 * <br/><br/>
 * Note that based on the selected tracks to scrobble, each individual track could still end up out-of-bounds,
 * even when the requested scrobble date is marked as valid, so one should not solely rely on this validator.
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmScrobbleDateConstraintValidator implements ConstraintValidator<LastFmScrobbleDateConstraint, Instant> {

    @Override
    public boolean isValid(Instant scrobbleDate, ConstraintValidatorContext context) {
        if (scrobbleDate == null) {
            // this can only validate non-null values, combine with other constraints if needed
            return true;
        }

        Clock clock = context.getClockProvider().getClock();
        Duration maxHistoricScrobbleDuration = LastFmScrobbleDateConstraint.MAX_HISTORIC_SCROBBLE;
        Instant maximumHistoricScrobble = Instant.now(clock).minus(maxHistoricScrobbleDuration);
        boolean isBeforeMaximum = scrobbleDate.isBefore(maximumHistoricScrobble);

        if (!isBeforeMaximum) {
            return true;
        }

        // invalid
        String constraintViolationMessageTemplate = context.getDefaultConstraintMessageTemplate();
        // replace %s in the message with the maximum number of days in the past
        String message = String.format(constraintViolationMessageTemplate, maxHistoricScrobbleDuration.toDays());
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}
