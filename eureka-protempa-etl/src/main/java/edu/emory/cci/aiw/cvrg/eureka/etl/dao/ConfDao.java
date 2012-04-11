package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;

/**
 * A data access object interface used to retrive and store information about
 * backend configurations.
 * 
 * @author hrathod
 * 
 */
public interface ConfDao {

	/**
	 * Save the give configuration.
	 * 
	 * @param conf The configuration to save.
	 */
	public void save(Configuration conf);

	/**
	 * Get a configuration for the give user.
	 * 
	 * @param userId The unique identifier for the user.
	 * @return The configuration belonging to the given user.
	 */
	public Configuration get(Long userId);
}
