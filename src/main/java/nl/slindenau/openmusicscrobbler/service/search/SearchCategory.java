package nl.slindenau.openmusicscrobbler.service.search;

/**
 * The category to search on, or {@link SearchCategory#ANY} for matching <i>anything</i>
 *
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public enum SearchCategory {
    ANY,
    ARTIST,
    TITLE,
    FORMAT,
    YEAR,
}
