package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.etl.resource.JobFilter;

/**
 * A data access object interface to retreive and store information about
 * Protempa ETL jobs.
 * 
 * @author hrathod
 * 
 */
public interface JobDao {

	/**
	 * Stores the give job to the data store.
	 * 
	 * @param job The job to persist.
	 */
	public void save(Job job);

	/**
	 * Gets the job associated with the given unique identifier.
	 * 
	 * @param id The unique identifier for the job.
	 * @return The job associated with the give unique identifier.
	 */
	public Job get(Long id);

	/**
	 * Gets a list of jobs that meet the given filter criteria.
	 * 
	 * @param jobFilter The filter criteria.
	 * @return A list of jobs that meet the filter criteria.
	 */
	public List<Job> get(JobFilter jobFilter);

	/**
	 * Refreshes the give job by synchronizing the state of the object with the
	 * latest data in the data store.
	 * 
	 * @param inJob The job to refresh.
	 */
	void refresh(Job inJob);
}
