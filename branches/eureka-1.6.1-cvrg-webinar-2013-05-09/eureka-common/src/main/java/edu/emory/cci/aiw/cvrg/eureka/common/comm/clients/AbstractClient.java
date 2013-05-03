/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.common.comm.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import edu.emory.cci.aiw.cvrg.eureka.common.json.ObjectMapperProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hrathod
 */
public abstract class AbstractClient {
	
	private static final Logger LOGGER = LoggerFactory
	        .getLogger(AbstractClient.class);

	protected AbstractClient() {
	}

	protected Client getRestClient() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(
				JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		clientConfig.getClasses().add(ObjectMapperProvider.class);
		return Client.create(clientConfig);
	}

	protected WebResource getResource() {
		return this.getRestClient().resource(this.getResourceUrl());
	}

	protected abstract String getResourceUrl();
	
	protected void errorIfStatusEqualTo(ClientResponse response, 
			ClientResponse.Status status) throws ClientException {
		errorIf(response, status, true);
	}
	
	protected void errorIfStatusNotEqualTo(ClientResponse response, 
			ClientResponse.Status status) throws ClientException {
		errorIf(response, status, false);
	}
	
	private void errorIf(ClientResponse response, 
			ClientResponse.Status status, boolean bool) 
			throws ClientException {
		ClientResponse.Status clientResponseStatus = 
				response.getClientResponseStatus();
		if (clientResponseStatus.equals(status) == bool) {
			String message = response.getEntity(String.class);
			LOGGER.error(message);
			throw new ClientException(clientResponseStatus, message);
		}
	}
}
