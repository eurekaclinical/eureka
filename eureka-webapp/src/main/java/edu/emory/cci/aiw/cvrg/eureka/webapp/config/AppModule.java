package edu.emory.cci.aiw.cvrg.eureka.webapp.config;

/*
 * #%L
 * Eureka WebApp
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

import com.google.inject.AbstractModule;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.webapp.provider.ServicesClientProvider;

/**
 *
 * @author hrathod
 */
class AppModule extends AbstractModule {
	private final WebappProperties webappProperties;

	AppModule(WebappProperties webappProperties) {
		assert webappProperties != null : "webappProperties cannot be null";
		this.webappProperties = webappProperties;
	}
	
	@Override
	protected void configure() {
		bind(WebappProperties.class).toInstance(this.webappProperties);
		bind(ServicesClient.class).toProvider(ServicesClientProvider.class);
	}
	
}
