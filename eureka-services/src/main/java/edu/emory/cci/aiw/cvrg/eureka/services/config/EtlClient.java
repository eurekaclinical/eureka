package edu.emory.cci.aiw.cvrg.eureka.services.config;

/*
 * #%L
 * Eureka Services
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

import com.sun.jersey.api.client.ClientResponse;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlCohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlI2B2Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlPatientSetExtractorDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import java.io.InputStream;
import java.util.List;
import org.eurekaclinical.common.comm.Role;
import org.eurekaclinical.common.comm.clients.ClientException;
import org.eurekaclinical.eureka.client.comm.Job;
import org.eurekaclinical.eureka.client.comm.JobFilter;
import org.eurekaclinical.eureka.client.comm.SourceConfig;
import org.eurekaclinical.eureka.client.comm.Statistics;
import org.protempa.PropositionDefinition;

/**
 *
 * @author Andrew Post
 */
public interface EtlClient {

	EtlDestination getDestination(String destId) throws ClientException;
	
	List<EtlCohortDestination> getCohortDestinations() throws ClientException;
	
	List<EtlI2B2Destination> getI2B2Destinations() throws ClientException;
	
	List<EtlPatientSetExtractorDestination> getPatientSetExtractorDestinations() throws ClientException;

	List<EtlDestination> getDestinations() throws ClientException;
	
	Long createDestination(EtlDestination etlDest) throws ClientException;

	void updateDestination(EtlDestination etlDest) throws ClientException;
	
	void deleteDestination(String etlDestId) throws ClientException;

	Job getJob(Long inJobId) throws ClientException;
	
	Statistics getJobStats(Long inJobId, String inPropId) throws ClientException;

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

	List<Job> getJobsDesc() throws ClientException;
	
	List<String> getPropositionSearchResults(String sourceConfigID, String inSearchKey) throws ClientException;

	List<PropositionDefinition> getPropositionSearchResultsBySearchKey(String sourceConfigID, String inSearchKey) throws ClientException;
	
	ClientResponse getOutput(String destinationId) throws ClientException;
	
	void deleteOutput(String destinationId) throws ClientException;

	/**
	 * gets the latest job submitted by the logged in user
	 * @return
	 * @throws ClientException
	 */
	List<Job> getLatestJob()  throws ClientException;
	
	List<Role> getRoles() throws ClientException;
	
	Role getRole(Long inRoleId) throws ClientException;
	
	Role getRoleByName(String name) throws ClientException;

}
