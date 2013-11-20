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

/**
 * @author hrathod
 */
public abstract class AbstractServletModule extends JerseyServletModule {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractServletModule.class);
	private static final String CAS_CALLBACK_PATH = "/proxyCallback";
	private static final String CAS_LOGIN_PATH = "/login";
	private static final String ROLES_SQL = "select a.name as role from roles a, user_role b, users c where a.id=b.role_id and b.user_id=c.id and c.email=?";
	private static final String ROLE_COLUMN = "role";
	private static final String TEMPLATES_PATH = "/WEB-INF/templates";
	private static final String WEB_CONTENT_REGEX = "(/(image|js|css)/?.*)|(/.*\\.jsp)|(/WEB-INF/.*\\.jsp)|(/WEB-INF/.*\\.jspf)|(/.*\\.html)|(/favicon\\.ico)|(/robots\\.txt)";
	private static final String SERVICES_JNDI_NAME = "java:comp/env/jdbc/EurekaService";
	private static final String BACKEND_JNDI_NAME = "java:comp/env/jdbc/EurekaBackend";

	protected AbstractServletModule() {
		super();
	}

	protected void printParams(Map<String, String> inParams) {
		for (Map.Entry<String, String> entry : inParams.entrySet()) {
			LOGGER.debug(entry.getKey() + " -> " + entry.getValue());
		}
	}

	private void setupAuthorizationFilter() {
		bind(RolesFilter.class).in(Singleton.class);
		Map<String, String> rolesFilterInitParams = new HashMap<>();
		rolesFilterInitParams.put("datasource", SERVICES_JNDI_NAME);
		rolesFilterInitParams.put("sql", ROLES_SQL);
		rolesFilterInitParams.put("rolecolumn", ROLE_COLUMN);
		filter("/*").through(RolesFilter.class, rolesFilterInitParams);
	}

	private void setupCasProxyFilter() {
		bind(Cas20ProxyReceivingTicketValidationFilter.class).in(
				Singleton.class);
		Map<String, String> params = new HashMap<>();
		params.put("acceptAnyProxy", "true");
		params.put("proxyCallbackUrl", this.getProxyCallbackUrl());
		params.put("proxyReceptorUrl", this.getProxyReceptorUrl());
		params.put("casServerUrlPrefix", this.getCasUrl());
		params.put("serverName", this.getServerName());
		params.put("redirectAfterValidation", "true");
		if (LOGGER.isDebugEnabled()) {
			this.printParams(params);
		}
		filter(this.getProxyReceptorUrl(), this.getContainerProtectedPath())
				.through(Cas20ProxyReceivingTicketValidationFilter.class,
						params);
	}

	private void setupCasAuthenticationFilter() {
		bind(AuthenticationFilter.class).in(Singleton.class);
		Map<String, String> params = new HashMap<>();
		params.put("casServerLoginUrl", this.getCasLoginUrl());
		params.put("serverName", this.getServerName());
		params.put("renew", "false");
		params.put("gateway", "false");
		if (LOGGER.isDebugEnabled()) {
			this.printParams(params);
		}
		filter(this.getContainerProtectedPath()).through(
				AuthenticationFilter.class, params);
	}

	private void setupServletRequestWrapperFilter() {
		bind(HttpServletRequestWrapperFilter.class).in(Singleton.class);
		Map<String, String> params = new HashMap<>();
		params.put("roleAttribute", "authorities");
		if (LOGGER.isDebugEnabled()) {
			this.printParams(params);
		}
		filter("/*").through(HttpServletRequestWrapperFilter.class, params);
	}

	private void setupCasThreadLocalAssertionFilter() {
		bind(AssertionThreadLocalFilter.class).in(Singleton.class);
		filter("/*").through(AssertionThreadLocalFilter.class);
	}

	private void setupContainer() {
		Map<String, String> params = new HashMap<>();
		params.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");
		params.put(PackagesResourceConfig.PROPERTY_PACKAGES,
				this.getPackageNames());
		params.put(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
				RolesAllowedResourceFilterFactory.class.getName());
		params.put(ServletContainer.JSP_TEMPLATES_BASE_PATH, TEMPLATES_PATH);
		params.put(ServletContainer.PROPERTY_WEB_PAGE_CONTENT_REGEX,
				WEB_CONTENT_REGEX);
		if (LOGGER.isDebugEnabled()) {
			this.printParams(params);
		}
		serve(this.getContainerPath()).with(GuiceContainer.class, params);
	}

	@Override
	protected void configureServlets() {
		super.configureServlets();
		this.setupCasProxyFilter();
		this.setupCasAuthenticationFilter();
		this.setupServletRequestWrapperFilter();
		this.setupCasThreadLocalAssertionFilter();
		this.setupAuthorizationFilter();
		this.setupContainer();
	}

	private String getProxyCallbackUrl() {
		return this.getApplicationUrl() + CAS_CALLBACK_PATH;
	}

	private String getProxyReceptorUrl() {
		return CAS_CALLBACK_PATH;
	}

	protected String getApplicationUrl() {
		return this.getServerName() + this.getContextPath();
	}

	protected String getCasLoginUrl() {
		return this.getCasUrl() + CAS_LOGIN_PATH;
	}

	protected abstract String getContextPath();

	protected abstract String getPackageNames();

	protected abstract String getServerName();

	protected abstract String getCasUrl();

	protected abstract String getContainerPath();

	protected abstract String getContainerProtectedPath();
}
