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

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CategoricalElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

/**
 * @author hrathod
 */
public class ServicesClient extends AbstractClient {

	private static final GenericType<User> UserType = new GenericType<User>() {
	};
	private final String servicesUrl;

	public ServicesClient(String inServicesUrl) {
		super();
		this.servicesUrl = inServicesUrl;
	}

	@Override
	String getResourceUrl() {
		return this.servicesUrl;
	}

	public User getUserByName(String username) {
		final String path = "/api/user/byname/" + username;
		return this.getResource().path(path).get(UserType);
	}

	public void saveSequence(Sequence inSequence) throws ClientException {
		final String path = "/user/create/sequence";
		ClientResponse response = this.getResource().path(path)
		        .type(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN)
		        .post(ClientResponse.class, inSequence);
		if (!response.getClientResponseStatus()
		        .equals(ClientResponse.Status.OK)) {
			String message = response.getEntity(String.class);
			throw new ClientException(message);
		}
	}

	public void saveCategoricalElement(CategoricalElement inElement)
	        throws ClientException {
		final String path = "/user/create/categorization";
		ClientResponse response = this.getResource().path(path)
		        .type(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN)
		        .post(ClientResponse.class, inElement);
		if (!response.getClientResponseStatus().equals(ClientResponse.Status.OK)) {
			String message = response.getEntity(String.class);
			throw new ClientException(message);
		}
		
	}
}
