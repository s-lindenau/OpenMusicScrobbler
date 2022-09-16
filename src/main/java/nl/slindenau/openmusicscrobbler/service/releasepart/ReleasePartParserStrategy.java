package nl.slindenau.openmusicscrobbler.service.releasepart;

import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.releasepart.parser.AlphaNumericParser;
import nl.slindenau.openmusicscrobbler.service.releasepart.parser.DefaultParser;
import nl.slindenau.openmusicscrobbler.service.releasepart.parser.MultipleNumericParser;
import nl.slindenau.openmusicscrobbler.service.releasepart.parser.NumericParser;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class ReleasePartParserStrategy {

    private final List<Supplier<ReleasePartParser>> parsers = List.of(
            NumericParser::new,
            MultipleNumericParser::new,
            AlphaNumericParser::new
    );

    public ReleasePartParser getParseStrategy(Collection<Track> trackList) {
        return parsers.stream()
                .map(parser -> new ReleasePartParseResult(parser.get(), trackList))
                .filter(this::hasMinimumRequiredParseConfidence)
                .max(Comparator.comparingInt(ReleasePartParseResult::getConfidence))
                .map(ReleasePartParseResult::getParser)
                .orElseGet(DefaultParser::new);
    }

    private boolean hasMinimumRequiredParseConfidence(ReleasePartParseResult parseResult) {
        return parseResult.getConfidence() > 0;
    }

    private static class ReleasePartParseResult {

        private final ReleasePartParser parser;
        private final int confidence;

        public ReleasePartParseResult(ReleasePartParser parser, Collection<Track> trackList) {
            this.parser = parser;
            this.confidence = parser.getConfidence(trackList);
        }

        public ReleasePartParser getParser() {
            return parser;
        }

        public int getConfidence() {
            return confidence;
        }
    }
}
