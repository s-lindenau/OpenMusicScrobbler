package nl.slindenau.openmusicscrobbler.service.search;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public record Searchable<T>(T target, SearchCategory category, String value) {
}
