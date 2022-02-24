package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Parses hour:minute:second notation to a total length in seconds
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class TrackDurationService {

    public static final String DEFAULT_DURATION_SEPARATOR = ":";
    public static final int SECONDS_MULTIPLIER_TO_SECONDS = 1;
    public static final int MINUTE_MULTIPLIER_TO_SECONDS = 60 * SECONDS_MULTIPLIER_TO_SECONDS;
    public static final int HOUR_MULTIPLIER_TO_SECONDS = 60 * MINUTE_MULTIPLIER_TO_SECONDS;

    private Stack<Integer> multiplicationFactors = new Stack<>();

    private void createMultiplicationFactorsStack() {
        multiplicationFactors = new Stack<>();
        multiplicationFactors.push(HOUR_MULTIPLIER_TO_SECONDS);
        multiplicationFactors.push(MINUTE_MULTIPLIER_TO_SECONDS);
        multiplicationFactors.push(SECONDS_MULTIPLIER_TO_SECONDS);
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
        // reverse order; so we start with seconds, which is the top item in our Stack
        for (int i = partsCount - 1; i >= 0; i--) {
            String currentPart = trackDurationParts[i];
            Integer currentPartLength = parseInteger(currentPart);
            Integer multiplicationFactor = getNextMultiplicationFactor();
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

    private Integer getNextMultiplicationFactor() {
        try {
            return multiplicationFactors.pop();
        } catch (EmptyStackException ex) {
            throw new OpenMusicScrobblerException("Unsupported track duration format", ex);
        }
    }
}
