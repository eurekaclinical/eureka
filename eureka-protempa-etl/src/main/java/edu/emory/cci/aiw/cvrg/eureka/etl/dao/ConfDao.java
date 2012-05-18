package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;

/**
 * A data access object interface used to retrieve and store information about
 * backend configurations.
 *
 * @author hrathod
 *
 */
public interface ConfDao extends Dao<Configuration, Long> {

	/**
	 * Gets a configuration that belongs to the give user (denoted by the unique
	 * identifier).
	 *
	 * @param userId Unique identifier of the user to whom the configuration
	 * belongs.
	 * @return A configuration belonging to the give user.
	 */
	public Configuration getByUserId(Long userId);
}
