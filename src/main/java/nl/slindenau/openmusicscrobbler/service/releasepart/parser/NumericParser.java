package nl.slindenau.openmusicscrobbler.service.releasepart.parser;

import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;

import java.util.Collection;
import java.util.Collections;

/**
 * A single incrementing numbering list from 1 to N
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class NumericParser extends AbstractParser {

    private static final String PART_NAME = "All";

    @Override
    public int getConfidence(Collection<Track> trackList) {
        return trackList.stream().mapToInt(this::getTrackConfidence).sum();
    }

    private int getTrackConfidence(Track track) {
        int trackNumber = getTrackNumber(track);
        if (trackNumber == NOT_A_NUMBER) {
            return NO_MATCH_CONFIDENCE_PENALTY;
        }
        return MATCH_CONFIDENCE;
    }

    @Override
    public Collection<ReleasePart> createParts(Collection<Track> trackList) {
        return Collections.singletonList(new ReleasePart(PART_NAME, Collections.emptyList(), trackList));
    }

    private int getTrackNumber(Track track) {
        return parseInteger(track.position());
    }
}
