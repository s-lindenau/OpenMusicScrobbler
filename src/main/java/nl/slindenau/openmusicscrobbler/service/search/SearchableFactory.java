package nl.slindenau.openmusicscrobbler.service.search;

import nl.slindenau.openmusicscrobbler.model.MusicReleaseBasicInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class SearchableFactory {

    public Collection<Searchable<MusicReleaseBasicInformation>> createSearchInfo(Collection<MusicReleaseBasicInformation> musicReleases) {
        return musicReleases.stream()
                .map(this::createSearchableMusicRelease)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private Collection<Searchable<MusicReleaseBasicInformation>> createSearchableMusicRelease(MusicReleaseBasicInformation musicReleaseBasicInformation) {
        Collection<Searchable<MusicReleaseBasicInformation>> searchableCollection = new ArrayList<>();
        searchableCollection.add(createSearchable(musicReleaseBasicInformation, SearchCategory.ARTIST, musicReleaseBasicInformation.artist()));
        searchableCollection.add(createSearchable(musicReleaseBasicInformation, SearchCategory.TITLE, musicReleaseBasicInformation.title()));
        searchableCollection.add(createSearchable(musicReleaseBasicInformation, SearchCategory.FORMAT, musicReleaseBasicInformation.format()));
        searchableCollection.add(createSearchable(musicReleaseBasicInformation, SearchCategory.YEAR, musicReleaseBasicInformation.year()));
        return searchableCollection;
    }

    private Searchable<MusicReleaseBasicInformation> createSearchable(MusicReleaseBasicInformation musicReleaseBasicInformation, SearchCategory category, Object searchableValue) {
        String value = searchableValue == null ? "" : String.valueOf(searchableValue);
        return new Searchable<>(musicReleaseBasicInformation, category, value);
    }
}
