package nl.slindenau.openmusicscrobbler.util;

import java.util.function.Supplier;

/**
 * A {@link Supplier} that caches the supplied value
 *
 * @param <Type> the type of results supplied by this supplier
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class CachingSupplier<Type> implements Supplier<Type> {

    private final Supplier<Type> supplier;
    private Type cachedValue;

    public CachingSupplier(Supplier<Type> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Type get() {
        synchronized (supplier) {
            if (this.cachedValue == null) {
                this.cachedValue = supplier.get();
            }
        }
        return this.cachedValue;
    }
}
