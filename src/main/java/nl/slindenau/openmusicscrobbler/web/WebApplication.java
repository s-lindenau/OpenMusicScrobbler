package nl.slindenau.openmusicscrobbler.web;

import io.dropwizard.Application;
import io.dropwizard.bundles.webjars.WebJarBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import nl.slindenau.openmusicscrobbler.exception.OpenMusicScrobblerException;
import nl.slindenau.openmusicscrobbler.web.controller.CollectionResource;
import nl.slindenau.openmusicscrobbler.web.controller.ConfigurationResource;
import nl.slindenau.openmusicscrobbler.web.assets.WebApplicationAssets;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;

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
        environment.lifecycle().addServerLifecycleListener(this::printOnServerStarted);
        environment.jersey().setUrlPattern(JERSEY_APPLICATION_ROOT_URL_PATTERN);
        environment.jersey().register(new LoggingExceptionMapper.ThrowableExceptionMapper());
        environment.jersey().register(new LoggingExceptionMapper.WebApplicationExceptionMapper());
        environment.jersey().register(new LoggingExceptionMapper.ConstraintViolationExceptionMapper());
        environment.jersey().register(new ParameterConverterProvider());
        environment.jersey().register(new CollectionResource());
        environment.jersey().register(new ConfigurationResource());
    }

    @Override
    public void initialize(Bootstrap<DropWizardConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle<>());
        bootstrap.addBundle(new WebApplicationAssets().getAssetsBundle());
        bootstrap.addBundle(new WebJarBundle());
    }

    private void printOnServerStarted(Server server) {
        System.out.println("Status: " + server);
        for (Connector connector : server.getConnectors()) {
            System.out.println("Connector: " + connector);
        }
        System.out.println("Server startup completed!");
    }
}
