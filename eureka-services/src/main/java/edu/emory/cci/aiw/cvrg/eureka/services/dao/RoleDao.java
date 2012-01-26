package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;

/**
 * A data access object interface for working with {@link Role} objects in the
 * data store.
 * 
 * @author hrathod
 * 
 */
public interface RoleDao {

	/**
	 * Get all the roles available in the system.
	 * 
	 * @return A Set of @{link Role} objects.
	 */
	List<Role> getRoles();

	/**
	 * Get a role, given the name of that role.
	 * 
	 * @param name The name of the role to search for.
	 * @return A {@link Role} object with a name matching the given name.
	 */
	Role getRoleByName(String name);

	/**
	 * Persist the given role object to the data store.
	 * 
	 * @param role The role to be persisted.
	 */
	void save(Role role);
}
