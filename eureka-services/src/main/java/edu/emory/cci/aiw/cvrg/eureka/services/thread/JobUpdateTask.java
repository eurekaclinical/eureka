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

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.services.job.JobCollection;

/**
 * A thread started at startup time to monitor the status of jobs that have been
 * submitted to the back-end.
 *
 * @author hrathod
 *
 */
@Singleton
public class JobUpdateTask implements Runnable {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JobUpdateTask.class);
	/**
	 * The back-end URL needed to make the REST calls for job updates.
	 */
	private final String backendUrl;
	/**
	 * The secure Jersey client used to make the REST calls.
	 */
	private final Client client;

	/**
	 * Create a job update thread using the given backend url.
	 *
	 * @param inBackendUrl The backend url used to make the update calls.
	 * @throws NoSuchAlgorithmException Thrown when the secure client can not be
	 *             created properly.
	 * @throws KeyManagementException Thrown when the secure client can not be
	 *             create properly.
	 */
	public JobUpdateTask(String inBackendUrl) throws KeyManagementException,
			NoSuchAlgorithmException {
		this.backendUrl = inBackendUrl;
		this.client = CommUtils.getClient();
	}

	@Override
	public void run() {
		JobFilter filter = new JobFilter(null, null,
				null, null, null);
		List<Job> updatedJobs = this.client.resource(this.backendUrl)
				.queryParam(
				"filter", filter.toQueryParam())
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Job>>() {
					// nothing to implement
				});
		if (updatedJobs != null) {
			JobCollection.setJobs(updatedJobs);
		} else {
			LOGGER.debug("Job update thread received null");
		}
	}
}
