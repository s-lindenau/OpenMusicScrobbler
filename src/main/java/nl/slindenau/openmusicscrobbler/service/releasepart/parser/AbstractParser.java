package nl.slindenau.openmusicscrobbler.service.releasepart.parser;

import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.releasepart.ReleasePartParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public abstract class AbstractParser implements ReleasePartParser {

    protected static final int NOT_A_NUMBER = Integer.MIN_VALUE;
    protected static final int NO_MATCH_CONFIDENCE_PENALTY = -1000;
    protected static final int MATCH_CONFIDENCE = 1;

    protected int parseInteger(String trackPositionPart) {
        try {
            return Integer.parseInt(trackPositionPart.trim());
        } catch (NumberFormatException ignored) {
            return NOT_A_NUMBER;
        }
    }

    protected boolean isNumeric(int input) {
        return input != NOT_A_NUMBER;
    }

    protected Optional<ParsedTrack> getParsedTrack(Track track, String partName, int trackNumber) {
        return Optional.of(new ParsedTrack(track, partName, trackNumber));
    }

    protected Collection<ReleasePart> createPartsByName(Collection<Track> trackList, Function<Track, Optional<ParsedTrack>> parseTrack) {
        Map<String, List<ParsedTrack>> parts = trackList.stream()
                .map(parseTrack)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(ParsedTrack::partName, Collectors.toCollection(LinkedList::new)));
        return createReleaseParts(parts);
    }

    private Collection<ReleasePart> createReleaseParts(Map<String, List<ParsedTrack>> parts) {
        Collection<ReleasePart> releaseParts = new ArrayList<>(parts.size());
        for (Map.Entry<String, List<ParsedTrack>> part : parts.entrySet()) {
            releaseParts.add(createReleasePart(part.getKey(), part.getValue()));
        }
        return releaseParts;
    }

    private ReleasePart createReleasePart(String partNumber, List<ParsedTrack> parsedTracks) {
        Collection<Track> tracks = parsedTracks.stream().map(ParsedTrack::track).collect(Collectors.toCollection(LinkedList::new));
        return new ReleasePart(partNumber, Collections.emptyList(), tracks);
    }

    protected record ParsedTrack(Track track, String partName, int trackNumber) {

    }
}
