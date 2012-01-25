package edu.emory.cci.aiw.cvrg.eureka.services.config;

import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
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

		// See the Bootstrap class for the persistence service startup.
		// The was required because the persistence service needs to be
		// started early in order to preload data for tests.
		// install(new JpaPersistModule("sample-jpa-unit"));
		// filter("/api/*").through(PersistFilter.class);

		Map<String, String> params = new HashMap<String, String>();
		params.put(PackagesResourceConfig.PROPERTY_PACKAGES,
				"edu.emory.cci.aiw.cvrg.eureka.services.resource");
		params.put(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
				RolesAllowedResourceFilterFactory.class.getName());
		serve("/api/*").with(GuiceContainer.class, params);
	}

}
