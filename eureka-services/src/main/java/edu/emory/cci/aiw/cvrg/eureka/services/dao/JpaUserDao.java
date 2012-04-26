package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User_;

/**
 * An implementation of the {@link UserDao} interface, backed by JPA entities
 * and queries.
 *
 * @author hrathod
 */
public class JpaUserDao extends GenericDao<User, Long> implements UserDao {

	/**
	 * Create an object with the give entity manager.
	 *
	 * @param inEMProvider The entity manager to be used for communication with
	 *                     the data store.
	 */
	@Inject
	public JpaUserDao(Provider<EntityManager> inEMProvider) {
		super(User.class, inEMProvider);
	}

	@Override
	public User getByName(String name) {
		return this.getUniqueByAttribute(User_.email, name);
	}

	@Override
	public User getByVerificationCode(String inCode) {
		return this.getUniqueByAttribute(User_.verificationCode, inCode);
	}
}
