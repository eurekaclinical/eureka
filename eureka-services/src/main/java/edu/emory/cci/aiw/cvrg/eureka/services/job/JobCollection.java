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
package edu.emory.cci.aiw.cvrg.eureka.services.job;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import java.util.ArrayList;
import java.util.List;


/**
 * A class to hold a collection of jobs.
 * 
 * @author hrathod
 * 
 */
public class JobCollection {

	/**
	 * A current list of jobs.
	 */
	private static List<Job> JOBS = new ArrayList<Job>();

	/**
	 * Sets/updates the list of jobs.
	 * 
	 * @param inJobs List of jobs to be held by the collection.
	 */
	public static void setJobs(List<Job> inJobs) {
		synchronized (JobCollection.class) {
			JobCollection.JOBS = inJobs;
		}
	}

	/**
	 * Get a list of jobs.
	 * 
	 * @return A list of jobs.
	 */
	public static List<Job> getJobs() {
		return JobCollection.JOBS;
	}

}
