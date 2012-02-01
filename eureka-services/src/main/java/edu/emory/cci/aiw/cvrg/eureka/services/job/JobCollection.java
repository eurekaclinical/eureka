package edu.emory.cci.aiw.cvrg.eureka.services.job;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;

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
	private static List<Job> jobs;

	/**
	 * Set/update the list of jobs.
	 * 
	 * @param inJobs
	 */
	public static void setJobs(List<Job> inJobs) {
		synchronized (JobCollection.jobs) {
			JobCollection.jobs = inJobs;
		}
	}

	/**
	 * Get a list of jobs.
	 * 
	 * @return A list of jobs.
	 */
	public static List<Job> getJobs() {
		return JobCollection.jobs;
	}

}
