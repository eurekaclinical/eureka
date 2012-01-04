package edu.emory.cci.aiw.cvrg.sample.config;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.persist.PersistFilter;
import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Configure Guice for testing.
 * 
 * @author hrathod
 * 
 */
public class ServletTestModule extends JerseyServletModule {

    @Override
    protected void configureServlets() {

        filter("/api/*").through(PersistFilter.class);

        Map<String, String> params = new HashMap<String, String>();
        params.put(PackagesResourceConfig.PROPERTY_PACKAGES,
                "edu.emory.cci.aiw.cvrg.sample.resource");
        params.put(PackagesResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
                RolesAllowedResourceFilterFactory.class.getName());
        serve("/api/*").with(GuiceContainer.class, params);
    }

}
