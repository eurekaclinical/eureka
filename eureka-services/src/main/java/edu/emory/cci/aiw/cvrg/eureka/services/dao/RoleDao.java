package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;

/**
 * A data access object interface for working with {@link Role} objects in the
 * data store.
 *
 * @author hrathod
 *
 */
public interface RoleDao extends Dao<Role, Long> {

	/**
	 * Get a role, given the name of that role.
	 *
	 * @param name The name of the role to search for.
	 * @return A {@link Role} object with a name matching the given name.
	 */
	Role getRoleByName(String name);
}
