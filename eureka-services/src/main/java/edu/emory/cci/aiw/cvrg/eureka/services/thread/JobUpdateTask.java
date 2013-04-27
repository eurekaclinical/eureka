/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.thread;

import com.google.inject.Inject;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.services.config.EtlClient;
import edu.emory.cci.aiw.cvrg.eureka.services.config.EtlClientImpl;
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
	 * The etlClient used to call into the ETL layer.
	 */
	private final EtlClient etlClient;

	/**
	 * Create a job update thread using the given backend url.
	 *
	 * @param inBackendUrl The backend url used to make the update calls.
	 * @throws NoSuchAlgorithmException Thrown when the secure etlClient can not
	 * be created properly.
	 * @throws KeyManagementException Thrown when the secure etlClient can not
	 * be create properly.
	 */
	@Inject
	public JobUpdateTask(EtlClient inEtlClient) {
		this.etlClient = inEtlClient;
	}

	@Override
	public void run() {
		JobFilter filter = new JobFilter(null, null,
				null, null, null);
		try {
			List<Job> updatedJobs = this.etlClient.getJobStatus(filter);
			if (updatedJobs != null) {
				JobCollection.setJobs(updatedJobs);
			} else {
				LOGGER.debug("Job update thread received null");
			}
		} catch (ClientException e) {
			LOGGER.error(
				"Job update task could not get job status from the ETL layer", 
				e);
			LOGGER.error("Job update task will keep trying");
		}
	}
}
