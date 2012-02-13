package edu.emory.cci.aiw.cvrg.sample.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.sample.entity.User;

/**
 * A data access object interface, to interact with {@link User} objects in the
 * data store.
 * 
 * @author hrathod
 * 
 */
public interface UserDao {

    /**
     * Get a list of users currently in the data store.
     * 
     * @return a list of {@link User} objects.
     */
    public abstract List<User> getUsers();

}