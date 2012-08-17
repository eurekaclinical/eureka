/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;

/**
 * A data access object interface for working with {@link Proposition} objects
 * in the data store.
 *
 * @author hrathod
 */
public interface PropositionDao extends Dao<Proposition, Long> {
	/**
	 * Gets a proposition based on the "key" attribute.
	 * @param inKey The key to be searched in the database.
	 * @return A proposition if found, null otherwise.
	 */
	public Proposition getByKey(String inKey);

	/**
	 * Gets a list of propositions for the given user ID.
	 * @param inId the unique identifier for the given user.
	 * @return A list of propositions belonging to the given user.
	 */
	public List<Proposition> getByUserId(Long inId);
}
