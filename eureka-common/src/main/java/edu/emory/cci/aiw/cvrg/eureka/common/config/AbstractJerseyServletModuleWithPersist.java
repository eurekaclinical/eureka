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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.persist.PersistFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.props.AbstractProperties;

/**
 * Extend to setup Eureka RESTful web services. This abstract class sets up 
 * Guice and Jersey and binds the authentication and authorization filters that 
 * every Eureka web service should have.
 * 
 * @author hrathod
 */
public abstract class AbstractJerseyServletModuleWithPersist extends AbstractJerseyServletModule {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractJerseyServletModuleWithPersist.class);
	private static final String CONTAINER_PATH = "/api/*";

	protected AbstractJerseyServletModuleWithPersist(AbstractProperties inProperties,
			String inPackageNames) {
		super(inProperties, inPackageNames);
	}
	
	@Override
	protected void configureServlets() {
		super.configureServlets();
		
		filter(CONTAINER_PATH).through(PersistFilter.class);
	}

}
