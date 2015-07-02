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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlPatientSetSenderDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Statistics;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.protempa.PropositionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;

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
	public void createDestination(EtlDestination etlDest) throws ClientException {
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
	public List<EtlPatientSetSenderDestination> getPatientSetSenderDestinations() throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Statistics getJobStats(Long inJobId, String inPropId) throws ClientException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
