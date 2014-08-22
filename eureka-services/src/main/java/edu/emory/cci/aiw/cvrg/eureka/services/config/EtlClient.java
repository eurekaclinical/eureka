package edu.emory.cci.aiw.cvrg.eureka.services.config;

/*
 * #%L
 * Eureka Services
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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlCohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlI2B2Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import java.io.InputStream;
import java.util.List;
import org.protempa.PropositionDefinition;

/**
 *
 * @author Andrew Post
 */
public interface EtlClient {

	EtlDestination getDestination(String destId) throws ClientException;
	
	List<EtlCohortDestination> getCohortDestinations() throws ClientException;
	
	List<EtlI2B2Destination> getI2B2Destinations() throws ClientException;

	List<EtlDestination> getDestinations() throws ClientException;
	
	void createDestination(EtlDestination etlDest) throws ClientException;

	void updateDestination(EtlDestination etlDest) throws ClientException;

	Job getJob(Long inJobId) throws ClientException;

	List<Job> getJobStatus(JobFilter inFilter) throws ClientException;

	List<Job> getJobs() throws ClientException;

	/**
	 * Gets a proposition definition with a specified id for a source
	 * configuration.
	 *
	 * @param sourceConfigId the source config id of interest
	 * @param inKey the proposition id of interest.
	 * @return the proposition id, if found, or <code>null</code> if not.
	 *
	 * @throws ClientException if an error occurred looking for the proposition
	 * definition.
	 */
	PropositionDefinition getPropositionDefinition(String sourceConfigId, String inKey) throws ClientException;

	/**
	 * Gets all of the proposition definitions given by the key IDs for the
	 * given source configuration.
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
	List<PropositionDefinition> getPropositionDefinitions(String sourceConfigId, List<String> inKeys, Boolean withChildren) throws ClientException;

	SourceConfig getSourceConfig(String sourceConfigId) throws ClientException;

	List<SourceConfig> getSourceConfigs() throws ClientException;

	Long submitJob(JobRequest inJobRequest) throws ClientException;

	void upload(String fileName, String sourceId, String fileTypeId, InputStream inputStream) throws ClientException;

	void validatePropositions(ValidationRequest inRequest) throws ClientException;

	public List<Job> getJobsDesc() throws ClientException;
	
	public  List<String> getPropositionSearchResults(String sourceConfigID, String inSearchKey) throws ClientException;

}
