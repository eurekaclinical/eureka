package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration_;

/**
 * Implementation of the {@link ConfDao} interface, based on JPA.
 *
 * @author hrathod
 *
 */
public class JpaConfDao implements ConfDao {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaConfDao.class);
	/**
	 * The entity manager used to interact with the data store.
	 */
	private final EntityManager entityManager;

	/**
	 * Creates an object using the given entity manager.
	 *
	 * @param manager The entity manager to use for interaction with the data
	 * store.
	 */
	@Inject
	public JpaConfDao(EntityManager manager) {
		this.entityManager = manager;
	}

	@Override
	@Transactional
	public void save(Configuration conf) {
		this.entityManager.persist(conf);
	}

	@Override
	public Configuration get(Long userId) {
		Configuration configuration = null;
		try {
			CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
			CriteriaQuery<Configuration> criteriaQuery = builder.createQuery(Configuration.class);
			Root<Configuration> root = criteriaQuery.from(Configuration.class);
			TypedQuery<Configuration> query = this.entityManager.createQuery(criteriaQuery.
					where(builder.equal(root.get(Configuration_.userId), userId)));
			configuration = query.getSingleResult();
		} catch (NoResultException nre) {
			LOGGER.error(nre.getMessage(), nre);
		} catch (NonUniqueResultException nure) {
			LOGGER.error(nure.getMessage(), nure);
		}
		return configuration;
	}
}
