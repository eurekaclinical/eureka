/*
 * #%L
 * Eureka Protempa ETL
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
package edu.emory.cci.aiw.cvrg.eureka.etl.test;

import java.util.Date;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataException;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;

/**
 *
 * @author hrathod
 */
public class Setup implements TestDataProvider {

	private static final Long CONF_ID = Long.valueOf(1);
	private static final Long JOB_ID = Long.valueOf(1);
	private static final Long USER_ID = Long.valueOf(1);
	private static final String EMPTY_STRING = "";
	private final Provider<EntityManager> managerProvider;
	private Configuration configuration;
	private Job job;

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
		addConfigs();
		addJobs();
	}

	@Override
	public void tearDown() throws TestDataException {
		EntityManager entityManager = this.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(this.job);
		entityManager.remove(this.configuration);
		entityManager.flush();
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
		this.job = new Job();
		this.job.setTimestamp(new Date());
		this.job.setConfigurationId(this.configuration.getId());
		this.job.setUserId(USER_ID);
		this.job.setNewState("STARTING", null, null);
		entityManager.getTransaction().begin();
		entityManager.persist(this.job);
		entityManager.flush();
		entityManager.getTransaction().commit();
	}

	/**
	 * Add configuration information to the database.
	 */
	private void addConfigs() {
		EntityManager entityManager = this.getEntityManager();
		this.configuration = new Configuration();
		this.configuration.setUserId(USER_ID);
		this.configuration.setI2b2DataPassword(EMPTY_STRING);
		this.configuration.setI2b2DataSchema(EMPTY_STRING);
		this.configuration.setI2b2JdbcUrl(EMPTY_STRING);
		this.configuration.setI2b2MetaPassword(EMPTY_STRING);
		this.configuration.setI2b2MetaSchema(EMPTY_STRING);
		this.configuration.setOntology(EMPTY_STRING);
		this.configuration.setProtempaJdbcUrl(EMPTY_STRING);
		this.configuration.setProtempaPassword(EMPTY_STRING);
		this.configuration.setProtempaSchema(EMPTY_STRING);
		entityManager.getTransaction().begin();
		entityManager.persist(this.configuration);
		entityManager.flush();
		entityManager.getTransaction().commit();
	}
}
