package nl.slindenau.openmusicscrobbler.web;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.web.api.CollectionResource;
import nl.slindenau.openmusicscrobbler.web.assets.WebApplicationAssets;

/**
 * @author slindenau
 * @author davidvollmar
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class WebApplication extends Application<DropWizardConfiguration> {

    private static final String JERSEY_APPLICATION_ROOT_URL_PATTERN = "/app/*";

    @Override
    public void run(String... arguments) {
        try {
            super.run(arguments);
        } catch (Exception e) {
            throw new OpenMusicScrobblerException(e);
        }
    }

    @Override
    public void run(DropWizardConfiguration configuration, Environment environment) {
        CollectionResource collectionResource = new CollectionResource();
        environment.jersey().setUrlPattern(JERSEY_APPLICATION_ROOT_URL_PATTERN);
        environment.jersey().register(collectionResource);
    }

    @Override
    public void initialize(Bootstrap<DropWizardConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle<>());
        bootstrap.addBundle(new WebApplicationAssets().getAssetsBundle());
    }
}
