/*
 * #%L
 * Eureka Protempa ETL
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
package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import com.google.inject.persist.PersistFilter;

import edu.emory.cci.aiw.cvrg.eureka.common.config.AbstractServletModule;

/**
 * A Guice configuration module for setting up the web infrastructure and
 * binding appropriate implementations to interfaces.
 * 
 * @author hrathod
 * 
 */
public class ETLServletModule extends AbstractServletModule {

	private static final String CONTAINER_PATH = "/api/*";
	private static final String CONTAINER_PROTECTED_PATH = "/api/protected/*";
	private static final String PACKAGE_NAMES = "edu.emory.cci.aiw.cvrg.eureka.etl.resource;edu.emory.cci.aiw.cvrg.eureka.common.json";
	private final String contextPath;
	private final EtlProperties etlProperties;

	public ETLServletModule(EtlProperties inProperties) {
		super(inProperties, PACKAGE_NAMES, CONTAINER_PATH,
				CONTAINER_PROTECTED_PATH);
		this.contextPath = this.getServletContext().getContextPath();
		this.etlProperties = inProperties;
	}

	@Override
	protected void configureServlets() {
		super.configureServlets();
		filter(CONTAINER_PATH).through(PersistFilter.class);
	}
}
