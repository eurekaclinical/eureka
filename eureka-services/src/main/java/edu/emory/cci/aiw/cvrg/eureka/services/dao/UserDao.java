package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

/**
 * A data access object interface for working with {@link User} objects in the
 * data store.
 *
 * @author hrathod
 *
 */
public interface UserDao extends Dao<User, Long> {

	/**
	 * Get a user by the verification code.
	 *
	 * @param code The verification code to search for.
	 * @return The user who has the given verification code, or null.
	 */
	User getByVerificationCode(String code);

	/**
	 * Get a user object, given the user name.
	 *
	 * @param name The name of the user to retrieve.
	 * @return The user object that corresponds to the given user name.
	 */
	User getByName(String name);
}
