package edu.emory.cci.aiw.cvrg.eureka.common.config;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import edu.emory.cci.aiw.cvrg.eureka.common.filter.RolesFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.props.AbstractProperties;

/**
 * Extend to setup Eureka RESTful web services. This abstract class sets up 
 * Guice and Jersey and binds the authentication and authorization filters that 
 * every Eureka web service should have.
 * 
 * @author hrathod
 */
public abstract class AbstractJerseyServletModule extends JerseyServletModule {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractJerseyServletModule.class);
	private static final String CONTAINER_PATH = "/api/*";
	private static final String CONTAINER_PROTECTED_PATH = "/api/protected/*";
	private static final String CAS_CALLBACK_PATH = "/proxyCallback";
	private static final String TEMPLATES_PATH = "/WEB-INF/templates";
	private static final String WEB_CONTENT_REGEX = "(/(image|js|css)/?.*)|(/.*\\.jsp)|(/WEB-INF/.*\\.jsp)|(/WEB-INF/.*\\.jspf)|(/.*\\.html)|(/favicon\\.ico)|(/robots\\.txt)";
	private final String packageNames;
	private final ServletModuleSupport servletModuleSupport;

	protected AbstractJerseyServletModule(AbstractProperties inProperties,
			String inPackageNames) {
		this.servletModuleSupport = new ServletModuleSupport(this
				.getServletContext().getContextPath(), inProperties);
		this.packageNames = inPackageNames;
	}

	protected void printParams(Map<String, String> inParams) {
		for (Map.Entry<String, String> entry : inParams.entrySet()) {
			LOGGER.debug(entry.getKey() + " -> " + entry.getValue());
		}
	}

	private void setupAuthorizationFilter() {
		bind(RolesFilter.class).in(Singleton.class);
		Map<String, String> rolesFilterInitParams = this.servletModuleSupport
				.getRolesFilterInitParams();
		filter("/*").through(RolesFilter.class, rolesFilterInitParams);
	}

	private void setupCasProxyFilter() {
		bind(Cas20ProxyReceivingTicketValidationFilter.class).in(
				Singleton.class);
		Map<String, String> params = this.servletModuleSupport
				.getCasProxyFilterInitParamsForWebService();
		filter(CAS_CALLBACK_PATH, CONTAINER_PROTECTED_PATH).through(
				Cas20ProxyReceivingTicketValidationFilter.class, params);
	}

	private void setupCasAuthenticationFilter() {
		bind(AuthenticationFilter.class).in(Singleton.class);
		Map<String, String> params = this.servletModuleSupport
				.getCasAuthenticationFilterInitParams();
		filter(CONTAINER_PROTECTED_PATH).through(AuthenticationFilter.class,
				params);
	}

	private void setupCasServletRequestWrapperFilter() {
		bind(HttpServletRequestWrapperFilter.class).in(Singleton.class);
		Map<String, String> params = this.servletModuleSupport
				.getServletRequestWrapperFilterInitParams();
		filter("/*").through(HttpServletRequestWrapperFilter.class, params);
	}

	private void setupCasThreadLocalAssertionFilter() {
		bind(AssertionThreadLocalFilter.class).in(Singleton.class);
		filter("/*").through(AssertionThreadLocalFilter.class);
	}

	private void setupContainer() {
		Map<String, String> params = new HashMap<>();
		params.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");
		params.put(PackagesResourceConfig.PROPERTY_PACKAGES, this.packageNames);
		params.put(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
				RolesAllowedResourceFilterFactory.class.getName());
		params.put(ServletContainer.JSP_TEMPLATES_BASE_PATH, TEMPLATES_PATH);
		params.put(ServletContainer.PROPERTY_WEB_PAGE_CONTENT_REGEX,
				WEB_CONTENT_REGEX);
		if (LOGGER.isDebugEnabled()) {
			this.printParams(params);
		}
		serve(CONTAINER_PATH).with(GuiceContainer.class, params);
	}
	
	@Override
	protected void configureServlets() {
		super.configureServlets();
		/*
		 * CAS filters must go before other filters.
		 */
		this.setupCasFilters();
		this.setupAuthorizationFilter();
		this.setupContainer();
	}

	/**
	 * Sets up CAS filters. The filter order is specified in
	 * https://wiki.jasig.org/display/CASC/CAS+Client+for+Java+3.1
	 */
	private void setupCasFilters() {
		this.setupCasAuthenticationFilter();
		this.setupCasProxyFilter();
		this.setupCasServletRequestWrapperFilter();
		this.setupCasThreadLocalAssertionFilter();
	}

}
