/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.config;

import com.google.inject.persist.PersistFilter;

import edu.emory.cci.aiw.cvrg.eureka.common.config.AbstractServletModule;

/**
 * Configure web related items for Guice and Jersey.
 * 
 * @author hrathod
 * 
 */
class ServletModule extends AbstractServletModule {

	private static final String CONTAINER_PATH = "/api/*";
	private static final String CONTAINER_PROTECTED_PATH = "/api/protected/*";
	private static final String PACKAGE_NAMES = "edu.emory.cci.aiw.cvrg.eureka.services.resource;edu.emory.cci.aiw.cvrg.eureka.common.json";
	private final String contextPath;
	private final ServiceProperties serviceProperties;

	public ServletModule (ServiceProperties inProperties) {
		super();
		this.contextPath = this.getServletContext().getContextPath();
		this.serviceProperties = inProperties;
	}
	@Override
	protected void configureServlets() {
		super.configureServlets();
		filter(this.getContainerPath()).through(PersistFilter.class);
	}

	@Override
	protected String getContextPath() {
		return this.contextPath;
	}

	@Override
	protected String getPackageNames() {
		return PACKAGE_NAMES;
	}

	@Override
	protected String getServerName() {
		return this.serviceProperties.getServerName();
	}

	@Override
	protected String getCasUrl() {
		return this.serviceProperties.getCasUrl();
	}

	@Override
	protected String getContainerPath() {
		return CONTAINER_PATH;
	}

	@Override
	protected String getContainerProtectedPath() {
		return CONTAINER_PROTECTED_PATH;
	}
}
