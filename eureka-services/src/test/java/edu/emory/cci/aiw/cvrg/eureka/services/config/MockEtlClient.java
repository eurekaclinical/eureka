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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Statistics;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.eurekaclinical.common.comm.Role;
import org.eurekaclinical.common.comm.clients.ClientException;

import org.protempa.PropositionDefinition;

/**
 *
 * @author Andrew Post
 */
public class MockEtlClient implements EtlClient {

	@Override
	public EtlDestination getDestination(String destId) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<EtlDestination> getDestinations() throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Job getJob(Long inJobId) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Job> getJobStatus(JobFilter inFilter) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Job> getJobs() throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PropositionDefinition getPropositionDefinition(String sourceConfigId, String inKey) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<PropositionDefinition> getPropositionDefinitions(String sourceConfigId, List<String> inKeys, Boolean withChildren) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public SourceConfig getSourceConfig(String sourceConfigId) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<SourceConfig> getSourceConfigs() throws ClientException {
		SourceConfig mockSourceConfig = new SourceConfig();
		mockSourceConfig.setId("mockSourceConfig");
		mockSourceConfig.setDisplayName("Mock Source Configuration");
		return Collections.singletonList(mockSourceConfig);
	}

	@Override
	public Long submitJob(JobRequest inJobRequest) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void upload(String fileName, String sourceId, String fileTypeId, InputStream inputStream) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void validatePropositions(ValidationRequest inRequest) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Job> getJobsDesc() throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	@Override
	public List<String> getPropositionSearchResults(String sourceConfigId,String inSearchKey)
			throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<PropositionDefinition> getPropositionSearchResultsBySearchKey(String sourceConfigID, String inSearchKey) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Long createDestination(EtlDestination etlDest) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void updateDestination(EtlDestination etlDest) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<EtlCohortDestination> getCohortDestinations() throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<EtlI2B2Destination> getI2B2Destinations() throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void deleteDestination(String etlDestId) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<EtlPatientSetExtractorDestination> getPatientSetExtractorDestinations() throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Statistics getJobStats(Long inJobId, String inPropId) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public ClientResponse getOutput(String destinationId) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void deleteOutput(String destinationId) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<Job> getLatestJob() throws ClientException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Role> getRoles() throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Role getRole(Long inRoleId) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Role getRoleByName(String name) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
