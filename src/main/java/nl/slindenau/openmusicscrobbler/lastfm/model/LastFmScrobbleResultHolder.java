package nl.slindenau.openmusicscrobbler.lastfm.model;

import de.umass.lastfm.scrobble.ScrobbleResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Holds results per each individual Track that is sent to Last.fm
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmScrobbleResultHolder {

    private final Collection<LastFmScrobbleResult> results;

    public LastFmScrobbleResultHolder() {
        this.results = new LinkedList<>();
    }

    public void addResult(LastFmScrobbleResult result) {
        this.results.add(result);
    }

    public void addAll(LastFmScrobbleResultHolder other) {
        this.results.addAll(other.getResults());
    }

    public Collection<LastFmScrobbleResult> getResults() {
        return Collections.unmodifiableCollection(this.results);
    }

    public boolean isSuccess() {
        // todo: implement
        return true;
    }

    public String getMessage() {
        return results.stream()
                .map(LastFmScrobbleResult::getMessage)
                .filter(Objects::nonNull)
                .findAny()
                .orElseGet(this::getScrobbleResults);
    }

    private String getScrobbleResults() {
        // todo: update to readable result per scrobble
        return results.stream()
                .map(LastFmScrobbleResult::getApiScrobbleResult)
                .map(ScrobbleResult::toString)
                .collect(Collectors.joining("<br><br>"));
    }
}
