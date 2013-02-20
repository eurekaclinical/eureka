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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.protempa.PropositionDefinition;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;

/**
 * @author hrathod
 */
public class EtlClient extends AbstractClient {
	private static final GenericType<List<Job>> JobListType = new
			GenericType<List<Job>>() {

	};
	private final String resourceUrl;

	public EtlClient(String inResourceUrl) {
		this.resourceUrl = inResourceUrl;
	}

	@Override
	protected String getResourceUrl() {
		return this.resourceUrl;
	}

	public Configuration getConfiguration(Long inUserId) throws
			ClientException {
		final String path = "/api/configuration/get/" + inUserId;
		Configuration result;
		try {
			result = this.getResource().path(path).accept(
					MediaType.APPLICATION_JSON).type(
					MediaType.APPLICATION_JSON).get(Configuration.class);
		} catch (UniformInterfaceException e) {
			throw new ClientException(e.getResponse().getClientResponseStatus(), e.getMessage());
		}
		return result;
	}

	public void submitJob(JobRequest inJobRequest) throws ClientException {
		final String path = "/api/job/submit";
		ClientResponse response = this.getResource()
				.path(path)
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, inJobRequest);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.CREATED);
	}

	public List<Job> getJobStatus(JobFilter inFilter) throws ClientException {
		final String path = "/api/job/status";
		try {
		return this.getResource()
				.path(path)
				.queryParam("filter", inFilter.toQueryParam())
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(JobListType);
		} catch (UniformInterfaceException e) {
			throw new ClientException(e.getResponse().getClientResponseStatus(), e.getMessage());
		}
	}

	public void validatePropositions(ValidationRequest inRequest) throws
			ClientException {
		final String path = "/api/validate";
		ClientResponse response = this.getResource()
				.path(path)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, inRequest);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.OK);
	}

	/**
	 * Gets a proposition definition with a specified id for a specified user.
	 * 
	 * @param inUserId the user's id.
	 * @param inKey the proposition id of interest.
	 * @return the proposition id, if found, or <code>null</code> if not.
	 * 
	 * @throws ClientException if an error occurred looking for the proposition
	 * definition.
	 */
	public PropositionDefinition getPropositionDefinition (Long inUserId,
			String inKey) throws ClientException {
		PropositionDefinition result;
		try {
			/*
			 * The inKey parameter may contain spaces, slashes and other 
			 * characters that are not allowed in URLs, so it needs to be
			 * encoded. We use UriBuilder to guarantee a valid URL. The inKey
			 * string can't be templated because the slashes won't be encoded!
			 */
			String path = UriBuilder.fromPath("/api/proposition/")
					.segment("{arg1}", inKey)
					.build(inUserId).toString();
			result = this.getResource()
					.path(path)
					.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
					.get(PropositionDefinition.class);
		} catch (UniformInterfaceException e) {
			ClientResponse.Status clientResponseStatus = 
					e.getResponse().getClientResponseStatus();
			if (!ClientResponse.Status.NOT_FOUND.equals(clientResponseStatus)) {
				throw new ClientException(clientResponseStatus, 
						e.getMessage());
			} else {
				result = null;
			}
		}
		return result;
	}
	
	/**
	 * Gets all of the proposition definitions given by the key IDs for the given user.
	 * 
	 * @param inUserId the user's ID
	 * @param inKeys the keys (IDs) of the proposition definitions to get
	 * @param withChildren whether to get the children of specified proposition definitions as well
	 * @return a {@link List} of {@link PropositionDefinition}s
	 * 
	 * @throws ClientException if an error occurred looking for the proposition definitions
	 */
	public List<PropositionDefinition> getPropositionDefinitions (Long inUserId,
			List<String> inKeys, Boolean withChildren) throws ClientException {
		List<PropositionDefinition> result;
		try {
			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			for (String key : inKeys) {
				queryParams.add("key", key);
			}
			queryParams.add("withChildren", withChildren.toString());
			String path = UriBuilder.fromPath("/api/proposition/")
					.segment("{arg1}")
					.build(inUserId).toString();
			result = this.getResource()
					.path(path)
					.queryParams(queryParams)
					.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<PropositionDefinition>>() {});
		} catch (UniformInterfaceException e) {
			ClientResponse.Status clientResponseStatus = 
					e.getResponse().getClientResponseStatus();
			if (!ClientResponse.Status.NOT_FOUND.equals(clientResponseStatus)) {
				throw new ClientException(clientResponseStatus, 
						e.getMessage());
			} else {
				result = null;
			}
		}
		return result;
	}

	public void ping (Long inUserId) throws ClientException {
		String path = "/api/ping/" + inUserId;
		ClientResponse response;
		ClientResponse.Status status;
		try {
			response = this.getResource().path(path).accept(MediaType
					.APPLICATION_JSON).get(ClientResponse.class);
			this.errorIfStatusNotEqualTo(response, ClientResponse.Status.OK);
		} catch (UniformInterfaceException e) {
			status = e.getResponse().getClientResponseStatus();
			throw new ClientException(status,e.getMessage());
		}
	}
}
