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

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CategoricalElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

/**
 * @author hrathod
 */
public class ServicesClient extends AbstractClient {

	private static final Logger LOGGER = LoggerFactory.getLogger
		(ServicesClient.class);
	private static final GenericType<User> UserType = new GenericType<User>
		() {
	};
	private static final GenericType<List<DataElement>> UserPropositionList
		= new GenericType<List<DataElement>>() {
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

	private void saveDataElement (String inPath, DataElement inDataElement)
		throws ClientException {
		ClientResponse response = this.getResource().path(inPath).type
			(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post
			(ClientResponse.class, inDataElement);
		if (!response.getClientResponseStatus().equals(ClientResponse
			.Status.NO_CONTENT)) {
			String message = response.getEntity(String.class);
			LOGGER.error("Client error while saving element: {}", message);
			throw new ClientException(message);
		}
	}

	public void saveSequence(Sequence inSequence) throws ClientException {
		final String path = "/api/proposition/user/create/sequence";
		this.saveDataElement(path, inSequence);
	}

	public void saveCategoricalElement(CategoricalElement inElement) throws
		ClientException {
		final String path = "/api/proposition/user/create/categorization";
		this.saveDataElement(path, inElement);
	}

	public List<DataElement> getUserPropositions(User inUser) {
		final String path = "/api/proposition/user/list/" + inUser.getId();
		return this.getResource().path(path).accept(MediaType
			.APPLICATION_JSON).get(UserPropositionList);
	}
}
