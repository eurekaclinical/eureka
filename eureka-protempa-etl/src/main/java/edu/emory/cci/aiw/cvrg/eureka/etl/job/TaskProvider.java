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
package edu.emory.cci.aiw.cvrg.eureka.etl.job;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobEventDao;

@Singleton
public class TaskProvider implements Provider<Task> {

	private final JobDao jobDao;
	private final ETL etl;
	private final JobEventDao jobEventDao;

	@Inject
	public TaskProvider (JobDao inJobDao, JobEventDao inJobEventDao, ETL inETL) {
		this.jobDao = inJobDao;
		this.jobEventDao = inJobEventDao;
		this.etl = inETL;
	}

	@Override
	public Task get() {
		return new Task(this.jobDao, this.jobEventDao, this.etl);
	}
}
