/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 *
 * @author hrathod
 */
public class ServletTestModule extends JerseyServletModule {

	@Override
	protected void configureServlets() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		params.put(PackagesResourceConfig.PROPERTY_PACKAGES,
				"edu.emory.cci.aiw.cvrg.eureka.etl.resource");
		params.put(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
				RolesAllowedResourceFilterFactory.class.getName());

		serve("/api/*").with(GuiceContainer.class, params);
	}
}
