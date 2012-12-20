/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.clients;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;

/**
 * @author hrathod
 */
public class I2b2ClientProvider implements Provider<I2b2Client> {
	
	private final ServiceProperties serviceProperties;
	
	@Inject
	public I2b2ClientProvider(ServiceProperties inProperties) {
		this.serviceProperties = inProperties;
	}
	
	@Override
	public I2b2Client get() {
		return new I2b2RestClient(this.serviceProperties);
	}
}
