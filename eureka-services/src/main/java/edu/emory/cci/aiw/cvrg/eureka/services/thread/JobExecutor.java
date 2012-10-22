/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;

/**
 * Creates an Executor thread pool, and queues new jobs to that pool.
 *
 * @author hrathod
 */
@Singleton
public class JobExecutor {
	/**
	 * The service used to run tasks.
	 */
	private final ExecutorService service;

	/**
	 * Create a new JobExecutor with the given application properties.
	 *
	 * @param properties The application level configuration object.
	 */
	@Inject
	public JobExecutor(final ServiceProperties properties) {
		this.service = Executors
				.newFixedThreadPool(properties.getJobPoolSize());
	}

	/**
	 * Put a new job on the service queue.
	 *
	 * @param task The task to queue up.
	 */
	public void queueJob(final JobTask task) {
		this.service.execute(task);
	}
}
