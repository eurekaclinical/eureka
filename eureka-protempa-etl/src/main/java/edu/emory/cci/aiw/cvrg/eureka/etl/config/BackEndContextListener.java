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
package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import edu.emory.cci.aiw.cvrg.eureka.common.dao.DatabaseSupport;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEventType;

import edu.emory.cci.aiw.cvrg.eureka.etl.job.TaskManager;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loaded up on application initialization, sets up the application with Guice
 * injector and any other bootup processes.
 *
 * @author hrathod
 *
 */
public class BackEndContextListener extends GuiceServletContextListener {
	
	private static Logger LOGGER =
			LoggerFactory.getLogger(BackEndContextListener.class);
	
	static final String JPA_UNIT = "backend-jpa-unit";

	/**
	 * The Guice injector used to fetch instances of dependency injection
	 * enabled classes.
	 */
	private final Injector injector = Guice
			.createInjector(new ETLServletModule());

	@Override
	protected Injector getInjector() {
		return this.injector;
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		// FIXME what is this line for?
		servletContextEvent.getServletContext().setAttribute("", new Object());
		initDatabase();
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		super.contextDestroyed(servletContextEvent);
		TaskManager taskManager = 
				this.getInjector().getInstance(TaskManager.class);
		taskManager.shutdown();
	}
	
	private static void repairJobsIfNeeded(
			EntityManager entityManager, List<JobEntity> jobs) {
		entityManager.getTransaction().begin();
		int numJobsRepaired = 0;
		for (JobEntity job : jobs) {
			JobEventType currentState = job.getCurrentState();
			if (!JobEventType.COMPLETED.equals(currentState) 
					&& !JobEventType.FAILED.equals(currentState)) {
				if (numJobsRepaired == 0) {
					LOGGER.warn(
						"Repairing jobs table, probably because the "
						+ "application shut down during a processing run.");
				}
				LOGGER.warn("Repairing job {}", job.toString());
				if (!JobEventType.ERROR.equals(currentState)) {
					job.newEvent(JobEventType.ERROR, 
							"Eureka! shut down during job", null);
				}
				job.newEvent(JobEventType.FAILED, null, null);
				entityManager.merge(job);
				LOGGER.warn("After repair, the job's status is {}", 
						job.toString());
				numJobsRepaired++;
			}
		}
		entityManager.getTransaction().commit();
		if (numJobsRepaired > 0) {
			LOGGER.warn("Repaired {} job(s).", numJobsRepaired);
		}
	}

	private static List<JobEntity> getAllJobs(EntityManager entityManager) {
		DatabaseSupport dbSupport = new DatabaseSupport(entityManager);
		return dbSupport.getAll(JobEntity.class);
	}

	private static void initDatabase() {
		EntityManagerFactory factory = 
				Persistence.createEntityManagerFactory(JPA_UNIT);
		try {
			EntityManager entityManager = factory.createEntityManager();
			try {
				List<JobEntity> jobs = getAllJobs(entityManager);
				repairJobsIfNeeded(entityManager, jobs);
				entityManager.close();
				entityManager = null;
			} finally {
				if (entityManager != null) {
					try {
						entityManager.close();
					} catch (Throwable ignore) {}
				}
			}
			factory.close();
			factory = null;
		} finally {
			if (factory != null) {
				try {
					factory.close();
				} catch (Throwable ignore) {}
			}
		}
	}
}
