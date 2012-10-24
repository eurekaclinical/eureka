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
package edu.emory.cci.aiw.cvrg.eureka.etl.job;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;

public class TaskProvider implements Provider<Task> {

	private final JobDao jobDao;
	private final EtlProperties etlProperties;

	@Inject
	public TaskProvider (JobDao inJobDao, EtlProperties inEtlProperties) {
		this.jobDao = inJobDao;
		this.etlProperties = inEtlProperties;
	}

	@Override
	public Task get() {
		return new Task(this.jobDao, new ETL(this.etlProperties));
	}
}
