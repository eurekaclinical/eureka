package edu.emory.cci.aiw.cvrg.eureka.common.config;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2015 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Map;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

import edu.emory.cci.aiw.cvrg.eureka.common.filter.RolesFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.props.AbstractProperties;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;

/**
 * Extend to setup Eureka web applications. This abstract class sets up Guice
 * and binds the authentication and authorization filters that every Eureka
 * web application should have.
 * 
 * @author hrathod
 */
public abstract class AbstractServletModule extends ServletModule {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractServletModule.class);
	private static final String CAS_CALLBACK_PATH = "/proxyCallback";
	private final String protectedPath;
	private final ServletModuleSupport servletModuleSupport;
	private final String logoutPath;

	protected AbstractServletModule(AbstractProperties inProperties,
			String inContainerPath, String inProtectedPath,
			String inLogoutPath) {
		if (inProtectedPath == null) {
			throw new IllegalArgumentException("inProtectedPath cannot be null");
		}
		if (inLogoutPath == null) {
			throw new IllegalArgumentException("inLogoutPath cannot be null");
		}
		this.servletModuleSupport = new ServletModuleSupport(
		this.getServletContext().getContextPath(), inProperties);
		this.protectedPath = inProtectedPath;
		this.logoutPath = inLogoutPath;
	}

	protected void printParams(Map<String, String> inParams) {
		for (Map.Entry<String, String> entry : inParams.entrySet()) {
			LOGGER.debug(entry.getKey() + " -> " + entry.getValue());
		}
	}
	
	private void setupCasSingleSignOutFilter() {
		bind(SingleSignOutFilter.class).in(Singleton.class);
		filter(this.logoutPath).through(SingleSignOutFilter.class);
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
				this.servletModuleSupport.getCasProxyFilterInitParamsForWebApp();
		filter(CAS_CALLBACK_PATH, this.protectedPath).through(
				Cas20ProxyReceivingTicketValidationFilter.class, params);
	}
	

	private void setupCasAuthenticationFilter() {
		bind(AuthenticationFilter.class).in(Singleton.class);
		Map<String, String> params = 
				this.servletModuleSupport.getCasAuthenticationFilterInitParams();
		filter(this.protectedPath).through(AuthenticationFilter.class, params);
	}

	private void setupCasServletRequestWrapperFilter() {
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
	
	/**
	 * Override to setup additional filters. The default implementation does 
	 * nothing.
	 */
	protected void setupFilters() {
	}

	@Override
	protected void configureServlets() {
		super.configureServlets();
		/*
		 * CAS filters must go before other filters.
		 */
		this.setupCasFilters();
		this.setupAuthorizationFilter();
		this.setupFilters();
		this.setupServlets();
	}
	
	/*
	 * Sets up CAS filters. The filter order is specified in
	 * https://wiki.jasig.org/display/CASC/Configuring+Single+Sign+Out
	 * and
	 * https://wiki.jasig.org/display/CASC/CAS+Client+for+Java+3.1
	 */
	private void setupCasFilters() {
		this.setupCasSingleSignOutFilter();
		this.setupCasAuthenticationFilter();
		this.setupCasProxyFilter();
		this.setupCasServletRequestWrapperFilter();
		this.setupCasThreadLocalAssertionFilter();
	}

}
