package nl.slindenau.openmusicscrobbler.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A collection of releases
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public final class ReleaseCollection {

    private final Map<Integer, MusicReleaseBasicInformation> releaseById = new HashMap<>();
    private final Map<Integer, MusicReleaseBasicInformation> releaseByDiscogsId = new HashMap<>();

    private final Collection<MusicReleaseBasicInformation> releases;

    public ReleaseCollection(Collection<MusicReleaseBasicInformation> releases) {
        this.releases = releases;
        this.releases.forEach(this::mapRelease);
    }

    private void mapRelease(MusicReleaseBasicInformation release) {
        releaseById.put(release.id(), release);
        releaseByDiscogsId.put(release.discogsId(), release);
    }

    public Collection<MusicReleaseBasicInformation> releases() {
        return releases;
    }

    public Optional<MusicReleaseBasicInformation> findById(int id) {
        return Optional.ofNullable(releaseById.get(id));
    }

    public Optional<MusicReleaseBasicInformation> findByDiscogsId(int discogsId) {
        return Optional.ofNullable(releaseByDiscogsId.get(discogsId));
    }
}
