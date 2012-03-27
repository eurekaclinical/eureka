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
	User getById(Long id);

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

	/**
	 * Refresh the give user from the database.
	 * 
	 * @param user The user to refresh.
	 */
	void refresh(User user);

}
