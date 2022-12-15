package nl.slindenau.openmusicscrobbler.web.assets;

import io.dropwizard.assets.AssetsBundle;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class WebApplicationAssets {

    private static final String URI_PATH = "/";
    private static final String INDEX_FILE = "index.html";

    public AssetsBundle getAssetsBundle() {
        return new AssetsBundle(getAssetsResourcePath(), URI_PATH, INDEX_FILE);
    }

    private String getAssetsResourcePath() {
        return "/" + WebApplicationAssets.class.getPackageName().replace(".", "/");
    }

}
