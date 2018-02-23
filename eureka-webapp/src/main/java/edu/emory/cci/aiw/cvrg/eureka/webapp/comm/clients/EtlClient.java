/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.webapp.comm.clients;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.eurekaclinical.eureka.client.comm.DestinationType;
import edu.emory.cci.aiw.cvrg.eureka.webapp.comm.EtlCohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.webapp.comm.EtlDestination;
import edu.emory.cci.aiw.cvrg.eureka.webapp.comm.EtlI2B2Destination;
import edu.emory.cci.aiw.cvrg.eureka.webapp.comm.EtlPatientSetExtractorDestination;
import edu.emory.cci.aiw.cvrg.eureka.webapp.comm.EtlPatientSetSenderDestination;
import edu.emory.cci.aiw.cvrg.eureka.webapp.comm.EtlTabularFileDestination;
import org.eurekaclinical.eureka.client.comm.Job;
import org.eurekaclinical.eureka.client.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.webapp.comm.JobRequest;
import org.eurekaclinical.eureka.client.comm.SourceConfig;
import org.eurekaclinical.eureka.client.comm.Statistics;
import edu.emory.cci.aiw.cvrg.eureka.webapp.comm.ValidationRequest;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import org.eurekaclinical.common.comm.Role;
import org.eurekaclinical.common.comm.clients.ClientException;
import org.protempa.PropositionDefinition;

/**
 * @author hrathod
 */
public class EtlClient extends EurekaClient {

	private static final GenericType<List<Job>> JobListType = new GenericType<List<Job>>() {
	};
	private static final GenericType<Job> JobType = new GenericType<Job>() {
	};
	private static final GenericType<Statistics> JobStatsType = new GenericType<Statistics>() {
	};
	private static final GenericType<List<SourceConfig>> SourceConfigListType
			= new GenericType<List<SourceConfig>>() {
			};
	private static final GenericType<List<EtlDestination>> DestinationListType
			= new GenericType<List<EtlDestination>>() {
			};
	private static final GenericType<List<EtlCohortDestination>> CohortDestinationListType
			= new GenericType<List<EtlCohortDestination>>() {
			};
	private static final GenericType<List<EtlI2B2Destination>> I2B2DestinationListType
			= new GenericType<List<EtlI2B2Destination>>() {
			};
	private static final GenericType<List<EtlPatientSetExtractorDestination>> PatientSetExtractorDestinationListType
			= new GenericType<List<EtlPatientSetExtractorDestination>>() {
			};
	private static final GenericType<List<EtlPatientSetSenderDestination>> PatientSetSenderDestinationListType
			= new GenericType<List<EtlPatientSetSenderDestination>>() {
			};
	private static final GenericType<List<EtlTabularFileDestination>> TabularFileDestinationListType
			= new GenericType<List<EtlTabularFileDestination>>() {
			};
	private static final GenericType<List<PropositionDefinition>> PropositionDefinitionList
			= new GenericType<List<PropositionDefinition>>() {
			};
	private static final GenericType<List<String>> PropositionSearchResultsList
			= new GenericType<List<String>>() {
			};
	private static final GenericType<List<Role>> RoleList = new GenericType<List<Role>>() {
	};
	private final URI resourceUrl;

	@Inject
	public EtlClient(String inEtlUrl) {
		this.resourceUrl = URI.create(inEtlUrl);
	}

	@Override
	protected URI getResourceUrl() {
		return this.resourceUrl;
	}

	public List<SourceConfig> getSourceConfigs() throws
			ClientException {
		final String path = "/api/protected/sourceconfigs";
		return doGet(path, SourceConfigListType);
	}

	public SourceConfig getSourceConfig(String sourceConfigId) throws
			ClientException {
		String path = UriBuilder.fromPath("/api/protected/sourceconfigs/")
				.segment(sourceConfigId)
				.build().toString();
		return doGet(path, SourceConfig.class);
	}

	public List<EtlDestination> getDestinations() throws
			ClientException {
		final String path = "/api/protected/destinations";
		return doGet(path, DestinationListType);
	}

	public List<EtlCohortDestination> getCohortDestinations() throws
			ClientException {
		final String path = "/api/protected/destinations/";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("type", DestinationType.COHORT.name());
		return doGet(path, queryParams, CohortDestinationListType);
	}

	public List<EtlI2B2Destination> getI2B2Destinations() throws
			ClientException {
		final String path = "/api/protected/destinations/";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("type", DestinationType.I2B2.name());
		return doGet(path, queryParams, I2B2DestinationListType);
	}

	public List<EtlPatientSetExtractorDestination> getPatientSetExtractorDestinations() throws ClientException {
		final String path = "/api/protected/destinations/";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("type", DestinationType.PATIENT_SET_EXTRACTOR.name());
		return doGet(path, queryParams, PatientSetExtractorDestinationListType);
	}
	
	public List<EtlPatientSetSenderDestination> getPatientSetSenderDestinations() throws ClientException {
		final String path = "/api/protected/destinations/";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("type", DestinationType.PATIENT_SET_SENDER.name());
		return doGet(path, queryParams, PatientSetSenderDestinationListType);
	}
	
	public List<EtlTabularFileDestination> getTabularFileDestinations() throws ClientException {
		final String path = "/api/protected/destinations/";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("type", DestinationType.TABULAR_FILE.name());
		return doGet(path, queryParams, TabularFileDestinationListType);
	}

	public EtlDestination getDestination(String destId) throws
			ClientException {
		String path = UriBuilder.fromPath("/api/protected/destinations/")
				.segment(destId)
				.build().toString();
		return doGet(path, EtlDestination.class);
	}

	public Long createDestination(EtlDestination etlDest) throws ClientException {
		String path = "/api/protected/destinations";
		URI destURI = doPostCreate(path, etlDest);
                return extractId(destURI);
	}

	public void updateDestination(EtlDestination etlDest) throws ClientException {
		String path = "/api/protected/destinations";
		doPut(path, etlDest);
	}

	public void deleteDestination(String etlDestId) throws ClientException {
		String path = UriBuilder.fromPath("/api/protected/destinations/")
				.segment(etlDestId)
				.build().toString();
		doDelete(path);
	}

	public Long submitJob(JobRequest inJobRequest) throws ClientException {
		final String path = "/api/protected/jobs";
		URI jobUri = doPostCreate(path, inJobRequest);
		return extractId(jobUri);
	}

	public List<Job> getJobStatus(JobFilter inFilter) throws ClientException {
		final String path = "/api/protected/jobs/status";
		return doGet(path, JobListType);
	}

	public List<Job> getJobs() throws ClientException {
		final String path = "/api/protected/jobs";
		return doGet(path, JobListType);
	}

	public List<Job> getJobsDesc() throws ClientException {
		final String path = "/api/protected/jobs";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("order", "desc");
		return doGet(path, queryParams, JobListType);
	}

	public List<Job> getLatestJob() throws ClientException {
		final String path = "/api/protected/jobs/latest";
		return doGet(path, JobListType);
	}

	public Job getJob(Long inJobId) throws ClientException {
		final String path = "/api/protected/jobs/" + inJobId;
		return doGet(path, JobType);
	}

	public Statistics getJobStats(Long inJobId, String inPropId) throws ClientException {
		UriBuilder uriBuilder = UriBuilder.fromPath("/api/protected/jobs/{arg1}/stats/");
		if (inPropId != null) {
			uriBuilder = uriBuilder.segment(inPropId);
		}
		return doGet(uriBuilder.build(inJobId).toString(), JobStatsType);
	}

	public void validatePropositions(ValidationRequest inRequest) throws
			ClientException {
		final String path = "/api/protected/validate";
		doPost(path, inRequest);
	}

	/**
	 * Gets a proposition definition with a specified id for a specified user.
	 *
	 * @param sourceConfigId the source config id of interest.
	 * @param inKey the proposition id of interest.
	 * @return the proposition id, if found, or <code>null</code> if not.
	 *
	 * @throws ClientException if an error occurred looking for the proposition
	 * definition.
	 */
	public PropositionDefinition getPropositionDefinition(
			String sourceConfigId, String inKey) throws ClientException {
		MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
		formParams.add("key", inKey);
		String path = UriBuilder.fromPath("/api/protected/concepts/")
				.segment(sourceConfigId)
				.build().toString();
		List<PropositionDefinition> propDefs = doPost(path, formParams, PropositionDefinitionList);
		if (propDefs.isEmpty()) {
			throw new ClientException(ClientResponse.Status.NOT_FOUND, null);
		} else {
			return propDefs.get(0);
		}
	}

	/**
	 * Gets all of the proposition definitions given by the key IDs for the
	 * given user.
	 *
	 * @param sourceConfigId the ID of the source configuration to use
	 * @param inKeys the keys (IDs) of the proposition definitions to get
	 * @param withChildren whether to get the children of specified proposition
	 * definitions as well
	 * @return a {@link List} of {@link PropositionDefinition}s
	 *
	 * @throws ClientException if an error occurred looking for the proposition
	 * definitions
	 */
	public List<PropositionDefinition> getPropositionDefinitions(
			String sourceConfigId, List<String> inKeys, Boolean withChildren) throws ClientException {
		MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
		for (String key : inKeys) {
			formParams.add("key", key);
		}
		formParams.add("withChildren", withChildren.toString());
		String path = UriBuilder.fromPath("/api/protected/concepts/")
				.segment(sourceConfigId)
				.build().toString();
		return doPost(path, formParams, PropositionDefinitionList);
	}

	public void upload(String fileName, String sourceId,
			String fileTypeId, InputStream inputStream)
			throws ClientException {
		String path = UriBuilder
				.fromPath("/api/protected/file/upload/")
				.segment(sourceId)
				.segment(fileTypeId)
				.build().toString();
		FormDataMultiPart part = new FormDataMultiPart();
        part.bodyPart(
                new FormDataBodyPart(
                        FormDataContentDisposition
                                .name("file")
                                .fileName(fileName)
                                .build(),
                        inputStream, MediaType.APPLICATION_OCTET_STREAM_TYPE));
		doPostMultipart(path, part);
	}

	public List<String> getPropositionSearchResults(String sourceConfigId,
			String inSearchKey) throws ClientException {

		String path = UriBuilder.fromPath("/api/protected/concepts/search/")
				.segment(sourceConfigId)
				.segment(inSearchKey)
				.build().toString();
		return doGet(path, PropositionSearchResultsList);
	}

	public List<PropositionDefinition> getPropositionSearchResultsBySearchKey(String sourceConfigId,
			String inSearchKey) throws ClientException {

		String path = UriBuilder.fromPath("/api/protected/concepts/propsearch/")
				.segment(sourceConfigId)
				.segment(inSearchKey)
				.build().toString();
		return doGet(path, PropositionDefinitionList);
	}

	public ClientResponse getOutput(String destinationId) throws ClientException {
		String path = UriBuilder.fromPath("/api/protected/output/")
				.segment(destinationId)
				.build().toString();
		return doGetResponse(path);
	}

	public void deleteOutput(String destinationId) throws ClientException {
		String path = UriBuilder.fromPath("/api/protected/output/")
				.segment(destinationId)
				.build().toString();
		doDelete(path);
	}
	
	public List<Role> getRoles() throws ClientException {
		final String path = "/api/protected/roles";
		return doGet(path, RoleList);
	}

	public Role getRole(Long inRoleId) throws ClientException {
		final String path = "/api/protected/roles/" + inRoleId;
		return doGet(path, Role.class);
	}
	
	public Role getRoleByName(String name) throws ClientException {
		return doGet("/api/protected/roles/byname/" + name, Role.class);
	}

}
