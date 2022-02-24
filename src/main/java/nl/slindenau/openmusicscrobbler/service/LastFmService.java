package nl.slindenau.openmusicscrobbler.service;

import nl.slindenau.openmusicscrobbler.lastfm.client.LastFmClientFactory;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class LastFmService {
    private LastFmClientFactory lastFmClientFactory;

    public LastFmService() {
        this.lastFmClientFactory = new LastFmClientFactory();
    }

    public LastFmService(LastFmClientFactory lastFmClientFactory) {
        this.lastFmClientFactory = lastFmClientFactory;
    }
}
