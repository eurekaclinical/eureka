package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;

/**
 * A data access object interface to retrieve and store information about
 * Protempa ETL jobs.
 *
 * @author hrathod
 *
 */
public interface JobDao extends Dao<Job, Long> {

	/**
	 * Gets a list of jobs that meet the given filter criteria.
	 *
	 * @param jobFilter The filter criteria.
	 * @return A list of jobs that meet the filter criteria.
	 */
	public List<Job> getWithFilter(JobFilter jobFilter);

}
