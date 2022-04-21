package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;

import java.time.Duration;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.function.Function;

/**
 * Parses <code>hour:minute:second</code> or <code>minute:second</code> notation to a total length in seconds
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class TrackDurationService {

    private static final String DEFAULT_DURATION_SEPARATOR = ":";

    private Stack<Function<Long, Duration>> createDurationSegmentParseStack() {
        Stack<Function<Long, Duration>> durationParsers = new Stack<>();
        durationParsers.push(Duration::ofHours);
        durationParsers.push(Duration::ofMinutes);
        durationParsers.push(Duration::ofSeconds);
        return durationParsers;
    }

    public Duration parseTrackLength(String trackDuration) {
        if (trackDuration.contains(DEFAULT_DURATION_SEPARATOR)) {
            String[] trackDurationParts = trackDuration.split(DEFAULT_DURATION_SEPARATOR);
            return toDuration(trackDurationParts);
        }
        throw new OpenMusicScrobblerException("Can't parse track duration: " + trackDuration);
    }

    private Duration toDuration(String[] trackDurationParts) {
        int partsCount = trackDurationParts.length;
        Duration duration = Duration.ZERO;
        Stack<Function<Long, Duration>> durationParsers = createDurationSegmentParseStack();
        // reverse order; so we start with seconds, which is the top item in our Stack
        for (int i = partsCount - 1; i >= 0; i--) {
            String currentPart = trackDurationParts[i];
            Long currentPartLength = parseNumber(currentPart);
            Function<Long, Duration> durationParserFunction = getNextDurationParser(durationParsers);
            duration = duration.plus(durationParserFunction.apply(currentPartLength));
        }
        return duration;
    }

    private Long parseNumber(String currentPart) {
        try {
            return Long.parseLong(currentPart);
        } catch (NumberFormatException ex) {
            throw new OpenMusicScrobblerException("Can't parse track duration part: " + currentPart, ex);
        }
    }

    private Function<Long, Duration> getNextDurationParser(Stack<Function<Long, Duration>> durationParsers) {
        try {
            return durationParsers.pop();
        } catch (EmptyStackException ex) {
            throw new OpenMusicScrobblerException("Unsupported track duration format", ex);
        }
    }
}
