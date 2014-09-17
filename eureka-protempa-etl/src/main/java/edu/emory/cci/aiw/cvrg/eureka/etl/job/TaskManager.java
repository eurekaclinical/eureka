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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.protempa.PropositionDefinition;
import org.protempa.backend.dsb.filter.Filter;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;

@Singleton
public class TaskManager {
	private final ExecutorService executorService;
	private final Provider<Task> taskProvider;

	@Inject
	public TaskManager (Provider<Task> inTaskProvider,
		EtlProperties inEtlProperties) {
		final int poolSize = inEtlProperties.getTaskThreadPoolSize();
		this.taskProvider = inTaskProvider;
		this.executorService = Executors.newFixedThreadPool(poolSize);
	}

	public void queueTask (Long inJobId, 
			List<PropositionDefinition> inPropositionDefinitions, 
			List<String> propIdsToShow, Filter filter, boolean appendData) {
		Task task = this.taskProvider.get();
		task.setJobId(inJobId);
		task.setPropositionDefinitions(inPropositionDefinitions);
		task.setPropositionIdsToShow(propIdsToShow);
		task.setFilter(filter);
		task.setAppendData(appendData);
		this.executorService.execute(task);
	}

	public void shutdown () {
		this.executorService.shutdown();
	}

	public void shutdownNow () {
		this.executorService.shutdownNow();
	}
}
