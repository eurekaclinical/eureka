/*
 * #%L
 * Eureka Protempa ETL
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
package edu.emory.cci.aiw.cvrg.eureka.etl.test;

import java.util.Date;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobStatus;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PatientSetExtractorDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SourceConfigEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataException;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.etl.Constants;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author hrathod
 */
public class Setup implements TestDataProvider {

	private final Provider<EntityManager> managerProvider;
	private JobEntity job;
	private DestinationEntity i2b2DE;
	private AuthorizedUserEntity user;
	private SourceConfigEntity sce;
	private PatientSetExtractorDestinationEntity patientSetSenderDE;

	/**
	 * Sets up necessary data for testing.
	 *
	 * @param inManagerProvider the entity manager provider.
	 */
	@Inject
	public Setup(Provider<EntityManager> inManagerProvider) {
		this.managerProvider = inManagerProvider;
	}

	@Override
	public void setUp() throws TestDataException {
		addUsers();
		addDestinations();
		addSourceConfig();
		addJobs();
	}

	@Override
	public void tearDown() throws TestDataException {
		EntityManager entityManager = this.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.refresh(this.user);
		for (JobEntity job : this.user.getJobs()) {
			entityManager.remove(job);
		}
		entityManager.remove(this.sce);
		entityManager.remove(this.i2b2DE);
		entityManager.remove(this.patientSetSenderDE);
		entityManager.remove(this.user);
		entityManager.getTransaction().commit();
	}

	private EntityManager getEntityManager() {
		return this.managerProvider.get();
	}
	
	private void addUsers() {
		EntityManager entityManager = this.getEntityManager();
		entityManager.getTransaction().begin();
		this.user = new AuthorizedUserEntity();
		this.user.setUsername("user@emory.edu");
		entityManager.persist(this.user);
		entityManager.getTransaction().commit();
	}

	/**
	 * Add job information to the database.
	 */
	private void addJobs() {
		EntityManager entityManager = this.getEntityManager();
		entityManager.getTransaction().begin();
		this.job = new JobEntity();
		this.job.setCreated(new Date());
		this.job.setSourceConfigId("0");
		this.job.setDestination(this.i2b2DE);
		this.job.setUser(this.user);
		JobEvent jobEvent = new JobEvent();
		jobEvent.setStatus(JobStatus.VALIDATING);
		this.job.addJobEvent(jobEvent);
		entityManager.persist(this.job);
		entityManager.getTransaction().commit();
	}

	private void addSourceConfig() {
		EntityManager entityManager = this.getEntityManager();
		entityManager.getTransaction().begin();
		this.sce = new SourceConfigEntity();
		this.sce.setName(Constants.SOURCECONFIG_NAME);
		this.sce.setOwner(this.user);
		entityManager.persist(this.sce);
		entityManager.getTransaction().commit();
	}
	
	private void addDestinations() {
		EntityManager entityManager = this.getEntityManager();
		entityManager.getTransaction().begin();
		this.i2b2DE = new I2B2DestinationEntity();
		this.i2b2DE.setName("0");
		entityManager.persist(this.i2b2DE);
		this.patientSetSenderDE = new PatientSetExtractorDestinationEntity();
		this.patientSetSenderDE.setName(Constants.DESTINATION_NAME);
		this.patientSetSenderDE.setOwner(this.user);
		this.patientSetSenderDE.setAliasPatientIdProperty("patientId");
		this.patientSetSenderDE.setAliasPropositionId(Constants.ALIAS_PROPOSITION_ID);
		this.patientSetSenderDE.setOutputType(MediaType.APPLICATION_JSON);
		entityManager.persist(this.patientSetSenderDE);
		entityManager.getTransaction().commit();
	}
}
