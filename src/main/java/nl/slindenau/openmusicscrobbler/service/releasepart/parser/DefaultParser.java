package nl.slindenau.openmusicscrobbler.service.releasepart.parser;

import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;
import nl.slindenau.openmusicscrobbler.service.releasepart.ReleasePartParser;

import java.util.Collection;
import java.util.Collections;

/**
 * The default parser can be used as a fallback; it will return all tracks in a single unnamed part.
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DefaultParser implements ReleasePartParser {

    private static final String PART_NAME = "";

    @Override
    public int getConfidence(Collection<Track> trackList) {
        return Integer.MIN_VALUE;
    }

    @Override
    public Collection<ReleasePart> createParts(Collection<Track> trackList) {
        return Collections.singletonList(new ReleasePart(PART_NAME, Collections.emptyList(), trackList));
    }
}
