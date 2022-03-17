package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Parses <code>hour:minute:second</code> or <code>minute:second</code> notation to a total length in seconds
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class TrackDurationService {

    private static final String DEFAULT_DURATION_SEPARATOR = ":";
    private static final int SECONDS_MULTIPLIER_TO_SECONDS = 1;
    private static final int MINUTE_MULTIPLIER_TO_SECONDS = 60 * SECONDS_MULTIPLIER_TO_SECONDS;
    private static final int HOUR_MULTIPLIER_TO_SECONDS = 60 * MINUTE_MULTIPLIER_TO_SECONDS;

    private Stack<Integer> createMultiplicationFactorsStack() {
        Stack<Integer> multiplicationFactors = new Stack<>();
        multiplicationFactors.push(HOUR_MULTIPLIER_TO_SECONDS);
        multiplicationFactors.push(MINUTE_MULTIPLIER_TO_SECONDS);
        multiplicationFactors.push(SECONDS_MULTIPLIER_TO_SECONDS);
        return multiplicationFactors;
    }

    public int parseTrackLength(String trackDuration) {
        createMultiplicationFactorsStack();
        if (trackDuration.contains(DEFAULT_DURATION_SEPARATOR)) {
            String[] trackDurationParts = trackDuration.split(DEFAULT_DURATION_SEPARATOR);
            return toSeconds(trackDurationParts);
        }
        throw new OpenMusicScrobblerException("Can't parse track duration: " + trackDuration);
    }

    private int toSeconds(String[] trackDurationParts) {
        int partsCount = trackDurationParts.length;
        int timeInSeconds = 0;
        Stack<Integer> multiplicationFactors = createMultiplicationFactorsStack();
        // reverse order; so we start with seconds, which is the top item in our Stack
        for (int i = partsCount - 1; i >= 0; i--) {
            String currentPart = trackDurationParts[i];
            Integer currentPartLength = parseInteger(currentPart);
            Integer multiplicationFactor = getNextMultiplicationFactor(multiplicationFactors);
            timeInSeconds += currentPartLength * multiplicationFactor;
        }
        return timeInSeconds;
    }

    private Integer parseInteger(String currentPart) {
        try {
            return Integer.parseInt(currentPart);
        } catch (NumberFormatException ex) {
            throw new OpenMusicScrobblerException("Can't parse track duration part: " + currentPart, ex);
        }
    }

    private Integer getNextMultiplicationFactor(Stack<Integer> multiplicationFactors) {
        try {
            return multiplicationFactors.pop();
        } catch (EmptyStackException ex) {
            throw new OpenMusicScrobblerException("Unsupported track duration format", ex);
        }
    }
}
