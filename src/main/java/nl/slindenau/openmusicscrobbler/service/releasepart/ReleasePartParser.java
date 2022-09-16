package nl.slindenau.openmusicscrobbler.service.releasepart;

import nl.slindenau.openmusicscrobbler.model.ReleasePart;
import nl.slindenau.openmusicscrobbler.model.Track;

import java.util.Collection;

/**
 * Parses a collection of {@link Track Tracks} into separate {@link ReleasePart ReleaseParts}
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public interface ReleasePartParser {

    /**
     * @param trackList the list of Tracks
     * @return with how much confidence can this parser process the given {@link Track tracks}. >0 for yes, <=0 for no.
     */
    int getConfidence(Collection<Track> trackList);

    /**
     * @param trackList the list of Tracks
     * @return the list of {@link Track Tracks} split into one or more {@link ReleasePart ReleaseParts}
     */
    Collection<ReleasePart> createParts(Collection<Track> trackList);
}
