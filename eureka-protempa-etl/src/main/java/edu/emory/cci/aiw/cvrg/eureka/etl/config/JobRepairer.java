package edu.emory.cci.aiw.cvrg.eureka.etl.config;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.dao.DatabaseSupport;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEventType;
import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * On startup, repairs jobs that were running when Eureka backend was shut 
 * down.
 * 
 * @author Andrew
 */
public class JobRepairer {
	private static Logger LOGGER =
			LoggerFactory.getLogger(JobRepairer.class);
	
	
	private final EntityManager entityManager;

	public JobRepairer(EntityManager inEntityManager) {
		if (inEntityManager == null) {
			throw new IllegalArgumentException("inEntityManager cannot be null");
		}
		this.entityManager = inEntityManager;
	}
	
	public final void repairIfNeeded() {
		this.entityManager.getTransaction().begin();
		int numJobsRepaired = 0;
		for (JobEntity job : getAllJobs()) {
			JobEventType currentState = job.getCurrentState();
			if (!JobEventType.COMPLETED.equals(currentState)
					&& !JobEventType.FAILED.equals(currentState)) {
				if (numJobsRepaired == 0) {
					LOGGER.warn(
							"Repairing jobs, probably because the "
							+ "application shut down during them.");
				}
				doRepair(job);
				numJobsRepaired++;
			}
		}
		this.entityManager.getTransaction().commit();
		if (numJobsRepaired > 0) {
			LOGGER.warn("Repaired {} job(s).", numJobsRepaired);
		}
	}
	
	private List<JobEntity> getAllJobs() {
		DatabaseSupport dbSupport = new DatabaseSupport(this.entityManager);
		return dbSupport.getAll(JobEntity.class);
	}
	
	protected void doRepair(JobEntity job) {
		repairDatabase(job, this.entityManager);
	}
	
	private void repairDatabase(JobEntity job, EntityManager entityManager) {
		LOGGER.warn("Repairing job {} with status {}", job.getId(), job.getCurrentState().name());
		if (!JobEventType.ERROR.equals(job.getCurrentState())) {
			job.newEvent(JobEventType.ERROR,
					"Eureka! shut down during job", null);
		}
		job.newEvent(JobEventType.FAILED, null, null);
		entityManager.merge(job);
		LOGGER.warn("After repair, the job {}'s status is {}",
				job.getId(), job.getCurrentState().name());
	}
}
