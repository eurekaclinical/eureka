package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(
			JpaConfDao.class);
	/**
	 * The entity manager used to interact with the data store.
	 */
	private final Provider<EntityManager> entityManagerProvider;

	/**
	 * Creates an object using the given entity manager.
	 *
	 * @param provider A provider that can deliver new entity manager instances
	 * on-demand.
	 */
	@Inject
	public JpaConfDao(Provider<EntityManager> provider) {
		this.entityManagerProvider = provider;
	}

	/**
	 * Returns a new entity manager to be used to interact with the data store.
	 *
	 * @return a new entity manager.
	 */
	private EntityManager getEntityManager() {
		return this.entityManagerProvider.get();
	}

	@Override
	@Transactional
	public void save(Configuration conf) {
		EntityManager entityManager = this.getEntityManager();
		entityManager.persist(conf);
		entityManager.flush();
	}

	@Override
	public List<Configuration> getAll() {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Configuration> criteriaQuery = builder.createQuery(
				Configuration.class);
		criteriaQuery.from(Configuration.class);
		TypedQuery<Configuration> typedQuery = entityManager.createQuery(
				criteriaQuery);
		return typedQuery.getResultList();
	}

	@Override
	public Configuration get(Long userId) {
		Configuration configuration = null;
		EntityManager entityManager = this.getEntityManager();
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Configuration> criteriaQuery = builder.createQuery(
					Configuration.class);
			Root<Configuration> root = criteriaQuery.from(Configuration.class);
			TypedQuery<Configuration> query = entityManager.createQuery(
					criteriaQuery.where(builder.equal(root.get(
					Configuration_.userId), userId)));
			configuration = query.getSingleResult();
		} catch (NoResultException nre) {
			LOGGER.error(nre.getMessage(), nre);
		} catch (NonUniqueResultException nure) {
			LOGGER.error(nure.getMessage(), nure);
		}
		return configuration;
	}
}
