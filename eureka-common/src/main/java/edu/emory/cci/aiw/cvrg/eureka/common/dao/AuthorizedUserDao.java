package edu.emory.cci.aiw.cvrg.eureka.common.dao;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;
import org.jasig.cas.client.authentication.AttributePrincipal;

/**
 * A data access object interface for working with {@link UserEntity} objects in the
 * data store.
 *
 * @author hrathod
 *
 */
public interface AuthorizedUserDao extends Dao<AuthorizedUserEntity, Long> {
	
	AuthorizedUserEntity getByAttributePrincipal(AttributePrincipal principal);

	/**
	 * Get a user object, given the user name.
	 *
	 * @param name The name of the user to retrieve.
	 * @return The user object that corresponds to the given user name.
	 */
	AuthorizedUserEntity getByUsername(String username);
}
