package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration_;

/**
 * Implementation of the {@link ConfDao} interface, based on JPA.
 *
 * @author hrathod
 *
 */
public class JpaConfDao extends GenericDao<Configuration, Long> implements
		ConfDao {

	/**
	 * Creates an object using the given entity manager.
	 *
	 * @param provider A provider that can deliver new entity manager instances
	 * on-demand.
	 */
	@Inject
	public JpaConfDao(Provider<EntityManager> provider) {
		super(Configuration.class, provider);
	}

	@Override
	public Configuration getByUserId(Long userId) {
		return getUniqueByAttribute(Configuration_.userId, userId);
	}
}
