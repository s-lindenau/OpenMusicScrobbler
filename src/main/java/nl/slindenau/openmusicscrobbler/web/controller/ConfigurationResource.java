package nl.slindenau.openmusicscrobbler.web.controller;

import com.codahale.metrics.annotation.Timed;
import nl.slindenau.openmusicscrobbler.config.ApplicationProperties;
import nl.slindenau.openmusicscrobbler.web.service.ConfigurationService;
import nl.slindenau.openmusicscrobbler.web.view.ConfigureApplicationView;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
@Path("/configuration")
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8", MediaType.TEXT_HTML + ";charset=UTF-8"})
public class ConfigurationResource {

    //todo: add dependency injection
    private final ConfigurationService configurationService = new ConfigurationService();

    @GET
    @Timed
    public ConfigureApplicationView getConfigurationAsView() {
        return getConfigureApplicationView();
    }

    private ConfigureApplicationView getConfigureApplicationView() {
        return new ConfigureApplicationView(new ApplicationProperties());
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public ConfigureApplicationView saveConfiguration(Form formData) {
        configurationService.saveConfiguration(formData);
        return getConfigureApplicationView();
    }

}
