package nl.slindenau.openmusicscrobbler.lastfm.client;

import nl.slindenau.openmusicscrobbler.util.CachingSupplier;

import java.util.function.Supplier;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmClientSupplier {

    private final Supplier<LastFmClientWrapper> supplier;

    public LastFmClientSupplier(Supplier<LastFmClientWrapper> supplier) {
        this.supplier = new CachingSupplier<>(supplier);
    }

    public LastFmClientWrapper getClient() {
        return supplier.get();
    }
}
