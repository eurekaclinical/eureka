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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEventType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SourceConfigEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataException;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;

/**
 *
 * @author hrathod
 */
public class Setup implements TestDataProvider {

	private final Provider<EntityManager> managerProvider;
	private JobEntity job;
	private DestinationEntity destination;
	private EtlUserEntity etlUser;
	private SourceConfigEntity sce;

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
		addJobs();
		addSourceConfig();
	}

	@Override
	public void tearDown() throws TestDataException {
		EntityManager entityManager = this.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(this.sce);
		entityManager.remove(this.job);
		entityManager.remove(this.destination);
		entityManager.remove(this.etlUser);
		entityManager.getTransaction().commit();
	}

	private EntityManager getEntityManager() {
		return this.managerProvider.get();
	}

	/**
	 * Add job information to the database.
	 */
	private void addJobs() {
		EntityManager entityManager = this.getEntityManager();
		entityManager.getTransaction().begin();
		this.etlUser = new EtlUserEntity();
		this.etlUser.setUsername("user@emory.edu");
		entityManager.persist(this.etlUser);
		this.destination = new I2B2DestinationEntity();
		this.destination.setName("0");
		entityManager.persist(this.destination);
		this.job = new JobEntity();
		this.job.setCreated(new Date());
		this.job.setSourceConfigId("0");
		this.job.setDestination(this.destination);
		this.job.setEtlUser(this.etlUser);
		JobEvent jobEvent = new JobEvent();
		jobEvent.setState(JobEventType.VALIDATING);
		this.job.addJobEvent(jobEvent);
		entityManager.persist(this.job);
		entityManager.getTransaction().commit();
	}

	private void addSourceConfig() {
		EntityManager entityManager = this.getEntityManager();
		entityManager.getTransaction().begin();
		this.sce = new SourceConfigEntity();
		this.sce.setName("foo");
		this.sce.setOwner(this.etlUser);
		entityManager.persist(this.sce);
		entityManager.getTransaction().commit();
	}
	
	
}
