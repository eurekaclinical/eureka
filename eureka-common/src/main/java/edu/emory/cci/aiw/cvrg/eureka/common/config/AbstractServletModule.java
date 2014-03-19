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

import java.util.Map;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

import edu.emory.cci.aiw.cvrg.eureka.common.filter.RolesFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.props.AbstractProperties;

/**
 * @author hrathod
 */
public abstract class AbstractServletModule extends ServletModule {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractServletModule.class);
	private static final String CAS_CALLBACK_PATH = "/proxyCallback";
	private final AbstractProperties properties;
	private final String protectedPath;
	private final ServletModuleSupport servletModuleSupport;

	protected AbstractServletModule(AbstractProperties inProperties,
			String inContainerPath, String inProtectedPath) {
		this.servletModuleSupport = new ServletModuleSupport(
		this.getServletContext().getContextPath(), inProperties);
		this.properties = inProperties;
		this.protectedPath = inProtectedPath;
	}

	protected void printParams(Map<String, String> inParams) {
		for (Map.Entry<String, String> entry : inParams.entrySet()) {
			LOGGER.debug(entry.getKey() + " -> " + entry.getValue());
		}
	}

	private void setupAuthorizationFilter() {
		bind(RolesFilter.class).in(Singleton.class);
		Map<String, String> rolesFilterInitParams = 
				this.servletModuleSupport.getRolesFilterInitParams();
		filter("/*").through(RolesFilter.class, rolesFilterInitParams);
	}

	private void setupCasProxyFilter() {
		bind(Cas20ProxyReceivingTicketValidationFilter.class).in(
				Singleton.class);
		Map<String, String> params = 
				this.servletModuleSupport.getCasProxyFilterInitParams();
		filter(CAS_CALLBACK_PATH, this.protectedPath).through(
				Cas20ProxyReceivingTicketValidationFilter.class, params);
	}

	private void setupCasAuthenticationFilter() {
		bind(AuthenticationFilter.class).in(Singleton.class);
		Map<String, String> params = 
				this.servletModuleSupport.getCasAuthenticationFilterInitParams();
		filter(this.protectedPath).through(AuthenticationFilter.class, params);
	}

	private void setupServletRequestWrapperFilter() {
		bind(HttpServletRequestWrapperFilter.class).in(Singleton.class);
		Map<String, String> params = 
				this.servletModuleSupport.getServletRequestWrapperFilterInitParams();
		filter("/*").through(HttpServletRequestWrapperFilter.class, params);
	}

	private void setupCasThreadLocalAssertionFilter() {
		bind(AssertionThreadLocalFilter.class).in(Singleton.class);
		filter("/*").through(AssertionThreadLocalFilter.class);
	}
	
	protected abstract void setupServlets();
	
	protected void setupFilters() {
		
	}

	@Override
	protected void configureServlets() {
		super.configureServlets();
		this.setupCasProxyFilter();
		this.setupCasAuthenticationFilter();
		this.setupServletRequestWrapperFilter();
		this.setupCasThreadLocalAssertionFilter();
		this.setupAuthorizationFilter();
		this.setupFilters();
		this.setupServlets();
	}

}
