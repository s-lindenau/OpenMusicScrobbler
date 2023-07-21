package nl.slindenau.openmusicscrobbler.service.releasepart.parser;

import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Multiple numeric lists separated by some character. 1-1 to 1-N, 2-1 to 2-N etc.
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class MultipleNumericParser extends AbstractParser {

    private static final SplitPattern[] SEPARATORS = {
            new SplitPattern("-"),
            new SplitPattern("."),
            new SplitPattern(","),
            new SplitPattern("_"),
            new SplitPattern(" ")
    };

    @Override
    public int getConfidence(Collection<Track> trackList) {
        return trackList.stream()
                .mapToInt(this::getTrackConfidence)
                .sum();
    }

    private int getTrackConfidence(Track track) {
        Optional<ParsedTrack> parsedTrack = parseTrack(track);
        return parsedTrack.map(onMatch -> MATCH_CONFIDENCE).orElse(NO_MATCH_CONFIDENCE_PENALTY);
    }

    @Override
    public Collection<ReleasePart> createParts(Collection<Track> trackList) {
        return createPartsByName(trackList, this::parseTrack);
    }

    private Optional<ParsedTrack> parseTrack(Track track) {
        String position = track.position();
        for (SplitPattern separator : SEPARATORS) {
            if (position.contains(separator.getPattern())) {
                String[] trackPositionParts = position.split(separator.getEscapedPattern());
                if (trackPositionParts.length == 2) {
                    return parseTrackParts(trackPositionParts, track);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<ParsedTrack> parseTrackParts(String[] trackPositionParts, Track track) {
        int partNumber = parseInteger(trackPositionParts[0]);
        int trackNumber = parseInteger(trackPositionParts[1]);
        if (isNumeric(partNumber) && isNumeric(trackNumber)) {
            return getParsedTrack(track, Integer.toString(partNumber), trackNumber);
        }
        return Optional.empty();
    }

    private static class SplitPattern {
        private final String pattern;
        private final String escapedPattern;

        public SplitPattern(String pattern) {
            this.pattern = pattern;
            this.escapedPattern = Pattern.quote(pattern);
        }

        public String getPattern() {
            return pattern;
        }

        public String getEscapedPattern() {
            return escapedPattern;
        }
    }
}
