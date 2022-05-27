package nl.slindenau.openmusicscrobbler.util;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class OptionalString {

    /**
     * @param input the String input value
     * @return an {@link Optional} with the provided input {@link String}, filtered on <b>not</b> {@link String#isBlank()}
     */
    public static Optional<String> ofNullableOrBlank(String input) {
        return Optional.ofNullable(input).filter(Predicate.not(String::isBlank));
    }

}
