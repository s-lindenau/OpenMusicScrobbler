package nl.slindenau.openmusicscrobbler.discogs.model;

import nl.slindenau.openmusicscrobbler.discogs.model.release.Artist;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Combines the Artist(s) on a Release into a single Artist name.<br/>
 * <ul><li>Uses the specific ANV (Artist Name Variation) if available.</li>
 * <li>Removes the duplicate marker at the end of the Artist name.</li>
 * <li>Combines multiple Artists with the given join character(s).</li></ul>
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsArtistNameCollector implements Collector<Artist, String, String> {

    private static final String SPACE = " ";
    private final DiscogsArtistName discogsArtistName = new DiscogsArtistName();
    private final StringBuilder artistNameBuilder = new StringBuilder();

    @Override
    public Supplier<String> supplier() {
        return artistNameBuilder::toString;
    }

    @Override
    public BiConsumer<String, Artist> accumulator() {
        return (artistName, nextArtist) -> {
            String nextArtistName = getArtistName(nextArtist);
            String joinCharacter = nextArtist.getJoin();
            join(nextArtistName, joinCharacter);
        };
    }

    @Override
    public BinaryOperator<String> combiner() {
        return (s, s2) -> {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + "::combiner() not implemented");
        };
    }

    @Override
    public Function<String, String> finisher() {
        return artist -> artistNameBuilder.toString();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }

    private String getArtistName(Artist artist) {
        String artistNameVariation = artist.getArtistNameVariation();
        if (artistNameVariation == null || artistNameVariation.isBlank()) {
            return discogsArtistName.getArtistName(artist.getName());
        } else {
            return discogsArtistName.getArtistName(artistNameVariation);
        }
    }

    private void join(String nextArtist, String joinCharacter) {
        if (joinCharacter == null || joinCharacter.isBlank()) {
            this.artistNameBuilder.append(nextArtist);
        } else {
            this.artistNameBuilder.append(nextArtist);
            this.artistNameBuilder.append(SPACE);
            this.artistNameBuilder.append(joinCharacter);
            this.artistNameBuilder.append(SPACE);
        }
    }
}
