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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import java.util.Date;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
public class DatabasePopulator {

	private static Logger LOGGER =
			LoggerFactory.getLogger(DatabasePopulator.class);
	private final JobDao jobDao;
	
	@Inject
	public DatabasePopulator(JobDao jobDao) {
		this.jobDao = jobDao;
	}
	
	void doPopulateIfNeeded() {
		repairJobsIfNeeded();
	}
	
	private void repairJobsIfNeeded() {
		int numJobsRepaired = 0;
		for (Job job : this.jobDao.getAll()) {
			String currentState = job.getCurrentState();
			if (!"DONE".equals(currentState)) {
				if (numJobsRepaired == 0) {
					LOGGER.warn(
						"Repairing jobs table, probably because the "
						+ "application shut down during a processing run.");
				}
				LOGGER.warn("Repairing job {}", job.toString());
				if (!"EXCEPTION".equals(currentState)) {
					job.setNewState("EXCEPTION", 
							"Eureka! shut down during job", null);
				}
				job.setNewState("DONE", null, null);
				this.jobDao.update(job);
				LOGGER.warn("After repair, the job's status is {}", 
						job.toString());
				numJobsRepaired++;
			}
		}
		if (numJobsRepaired > 0) {
			LOGGER.warn("Repaired {} job(s).", numJobsRepaired);
		}
	}
}
