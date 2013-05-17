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
package edu.emory.cci.aiw.cvrg.eureka.services.config;

import com.google.inject.Inject;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.protempa.PropositionDefinition;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.EurekaClient;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import java.io.InputStream;
import java.net.URI;

/**
 * @author hrathod
 */
public class EtlClientImpl extends EurekaClient implements EtlClient {

	private static final GenericType<List<Job>> JobListType = new GenericType<List<Job>>() {
	};
	private static final GenericType<Job> JobType = new GenericType<Job>() {
	};
	private static final GenericType<List<SourceConfig>> SourceConfigListType =
			new GenericType<List<SourceConfig>>() {
			};
	private static final GenericType<List<Destination>> DestinationListType =
			new GenericType<List<Destination>>() {
			};
	private static final GenericType<List<PropositionDefinition>> PropositionDefinitionList =
			new GenericType<List<PropositionDefinition>>() {
			};
	private final String resourceUrl;

	@Inject
	public EtlClientImpl(ServiceProperties serviceProperties) {
		this.resourceUrl = serviceProperties.getEtlUrl();
	}

	@Override
	protected String getResourceUrl() {
		return this.resourceUrl;
	}

	@Override
	public List<SourceConfig> getSourceConfigs() throws
			ClientException {
		final String path = "/api/protected/sourceconfigs";
		return doGet(path, SourceConfigListType);
	}

	@Override
	public SourceConfig getSourceConfig(String sourceConfigId) throws
			ClientException {
		String path = UriBuilder.fromPath("/api/protected/sourceconfigs/")
				.segment(sourceConfigId)
				.build().toString();
		return doGet(path, SourceConfig.class);
	}

	@Override
	public List<Destination> getDestinations() throws
			ClientException {
		final String path = "/api/protected/destinations";
		return doGet(path, DestinationListType);
	}

	@Override
	public Destination getDestination(String destId) throws
			ClientException {
		String path = UriBuilder.fromPath("/api/protected/destinations/")
				.segment(destId)
				.build().toString();
		return doGet(path, Destination.class);
	}

	@Override
	public Long submitJob(JobRequest inJobRequest) throws ClientException {
		final String path = "/api/protected/jobs";
		URI jobUri = doPostCreate(path, inJobRequest);
		return extractId(jobUri);
	}

	@Override
	public List<Job> getJobStatus(JobFilter inFilter) throws ClientException {
		final String path = "/api/protected/jobs/status";
		return doGet(path, JobListType);
	}

	@Override
	public List<Job> getJobs() throws ClientException {
		final String path = "/api/protected/jobs";
		return doGet(path, JobListType);
	}
	
	@Override
	public List<Job> getJobsDesc() throws ClientException {
		final String path = "/api/protected/jobs";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("order", "desc");
		return doGet(path, JobListType, queryParams);
	}

	@Override
	public Job getJob(Long inJobId) throws ClientException {
		final String path = "/api/protected/jobs/" + inJobId;
		return doGet(path, JobType);
	}

	@Override
	public void validatePropositions(ValidationRequest inRequest) throws
			ClientException {
		final String path = "/api/protected/validate";
		doPost(path, inRequest);
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
	@Override
	public PropositionDefinition getPropositionDefinition(
			String sourceConfigId, String inKey) throws ClientException {
		String path = UriBuilder.fromPath("/api/protected/proposition/")
					.segment(sourceConfigId, inKey)
					.build().toString();
		return doGet(path, PropositionDefinition.class);
	}

	/**
	 * Gets all of the proposition definitions given by the key IDs for the
	 * given user.
	 *
	 * @param inUserId the user's ID
	 * @param inKeys the keys (IDs) of the proposition definitions to get
	 * @param withChildren whether to get the children of specified proposition
	 * definitions as well
	 * @return a {@link List} of {@link PropositionDefinition}s
	 *
	 * @throws ClientException if an error occurred looking for the proposition
	 * definitions
	 */
	@Override
	public List<PropositionDefinition> getPropositionDefinitions(
			String sourceConfigId, List<String> inKeys, Boolean withChildren) throws ClientException {
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			for (String key : inKeys) {
				queryParams.add("key", key);
			}
			queryParams.add("withChildren", withChildren.toString());
			String path = UriBuilder.fromPath("/api/protected/proposition/")
					.segment(sourceConfigId)
					.build().toString();
		return doGet(path, PropositionDefinitionList, queryParams);
	}

	@Override
	public void ping(Long inUserId) throws ClientException {
		String path = "/api/protected/ping/testuser/" + inUserId;
		doGet(path, ClientResponse.class);
	}

	@Override
	public void upload(String fileName, String sourceId,
			String fileTypeId, InputStream inputStream)
			throws ClientException {
		String path = UriBuilder
				.fromPath("/api/protected/file/upload/")
				.segment(sourceId)
				.segment(fileTypeId)
				.build().toString();
		doPostMultipart(path, fileName, inputStream);
	}
}
