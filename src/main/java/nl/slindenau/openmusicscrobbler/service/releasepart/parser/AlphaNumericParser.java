package nl.slindenau.openmusicscrobbler.service.releasepart.parser;

import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;

import java.util.Collection;
import java.util.Optional;

/**
 * An incrementing number list prefixed by some character from the alphabet (A1 - An, B1 - Bn, etc.)<br/>
 * The numeric part is optional, in that case the entire part contains one track (A, B, etc.)
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class AlphaNumericParser extends AbstractParser {

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

        String partName = position.substring(0, 1);
        Optional<Integer> numericPart = getNumericPart(position);
        boolean numericPartIsValid = numericPart.map(this::isNumeric).orElse(true);

        if (isAlpha(partName) && numericPartIsValid) {
            int trackNumber = numericPart.orElse(1);
            return getParsedTrack(track, partName, trackNumber);
        }
        return Optional.empty();
    }

    private Optional<Integer> getNumericPart(String position) {
        if (position.length() > 1) {
            return Optional.of(parseInteger(position.substring(1)));
        }
        return Optional.empty();
    }

    private boolean isAlpha(String input) {
        return Character.isAlphabetic(input.charAt(0));
    }
}
