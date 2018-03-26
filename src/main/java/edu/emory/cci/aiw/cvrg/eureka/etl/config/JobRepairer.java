package edu.emory.cci.aiw.cvrg.eureka.etl.config;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.JobEventEntity;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.eurekaclinical.eureka.client.comm.JobStatus;
import org.eurekaclinical.standardapis.dao.DatabaseSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * On startup, repairs jobs that were running when Eureka backend was shut down.
 *
 * @author Andrew
 */
public class JobRepairer {

	private static Logger LOGGER
			= LoggerFactory.getLogger(JobRepairer.class);

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
			JobStatus currentState = job.getCurrentStatus();
			if (!JobStatus.COMPLETED.equals(currentState)
					&& !JobStatus.FAILED.equals(currentState)) {
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
		JobStatus currentState = job.getCurrentStatus();
		LOGGER.warn("Repairing job {} with status {}", job.getId(), currentState);
		if (!JobStatus.ERROR.equals(job.getCurrentStatus())) {
			JobEventEntity errorJobEvent = new JobEventEntity();
			errorJobEvent.setJob(job);
			errorJobEvent.setTimeStamp(new Date());
			errorJobEvent.setStatus(JobStatus.ERROR);
			errorJobEvent.setMessage("Eureka! shut down during job");
			entityManager.persist(errorJobEvent);
		}
		Date jobFinishedDate = new Date();
		job.setFinished(jobFinishedDate);
		JobEventEntity failedJobEvent = new JobEventEntity();
		failedJobEvent.setJob(job);
		failedJobEvent.setTimeStamp(jobFinishedDate);
		failedJobEvent.setStatus(JobStatus.FAILED);
		failedJobEvent.setMessage("Processing failed");
		entityManager.persist(failedJobEvent);
		JobStatus updatedCurrentState = job.getCurrentStatus();
		LOGGER.warn("After repair, the job {}'s status is {}",
				job.getId(), updatedCurrentState);
	}
}
