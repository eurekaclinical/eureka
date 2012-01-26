package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

/**
 * A data access object interface for working with {@link User} objects in the
 * data store.
 * 
 * @author hrathod
 * 
 */
public interface UserDao {

	/**
	 * Persist a given user to the data store.
	 * 
	 * @param u The user to be persisted.
	 */
	void save(User u);

	/**
	 * Get all the users in the data store.
	 * 
	 * @return A list of users.
	 */
	List<User> getUsers();

	/**
	 * Get a user object, given a unique identifier.
	 * 
	 * @param id The unique identifier to search for.
	 * @return The user who has a unique identifier match the one given.
	 */
	User get(Long id);

	/**
	 * Remove a given user from the data store.
	 * 
	 * @param id The unique identifier of the user to be removed.
	 * @return The user, which has now been removed from the data store.
	 */
	User delete(Long id);
}
