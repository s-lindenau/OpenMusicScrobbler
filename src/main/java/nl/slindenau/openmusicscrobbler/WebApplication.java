package nl.slindenau.openmusicscrobbler;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import nl.slindenau.openmusicscrobbler.web.CollectionResource;

public class WebApplication extends io.dropwizard.Application<DropWizardConfiguration>{

    public static void main(String[] args) throws Exception {
        new WebApplication().run(args);
    }

    @Override
    public void run(DropWizardConfiguration configuration, Environment environment) {
        CollectionResource collectionResource = new CollectionResource();
        environment.jersey().register(collectionResource);
    }

    @Override
    public void initialize(Bootstrap<DropWizardConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle<>());
    }
}
