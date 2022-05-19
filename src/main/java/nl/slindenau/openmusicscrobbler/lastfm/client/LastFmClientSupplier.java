package nl.slindenau.openmusicscrobbler.lastfm.client;

import java.util.function.Supplier;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmClientSupplier {

    private final Supplier<LastFmClientWrapper> supplier;
    private LastFmClientWrapper cached;

    public LastFmClientSupplier(Supplier<LastFmClientWrapper> supplier) {
        this.supplier = supplier;
    }

    public LastFmClientWrapper getClient() {
        if(this.cached == null) {
            this.cached = supplier.get();
        }
        return this.cached;
    }
}
