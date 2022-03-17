package nl.slindenau.openmusicscrobbler.discogs.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Removes the duplication marker from the artist name if present:<br/>
 * Artist (<code>n</code>) -> Artist
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class DiscogsArtistName {

    private static final String ARTIST_NAME_DUPLICATION_REGEX = "(.*)( \\(\\d\\))";
    private static final int ARTIST_NAME_WITHOUT_DUPLICATION_MARKER_MATCH_GROUP = 1;

    public String getArtistName(String artistName) {
        Pattern pattern = Pattern.compile(ARTIST_NAME_DUPLICATION_REGEX);
        Matcher matcher = pattern.matcher(artistName);
        if (matcher.find()) {
            return matcher.group(ARTIST_NAME_WITHOUT_DUPLICATION_MARKER_MATCH_GROUP);
        } else {
            return artistName;
        }
    }

}
