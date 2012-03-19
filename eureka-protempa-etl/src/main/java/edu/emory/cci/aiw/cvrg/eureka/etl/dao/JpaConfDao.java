package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;

public class JpaConfDao implements ConfDao {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JpaConfDao.class);
	private final EntityManager entityManager;

	@Inject
	public JpaConfDao(EntityManager manager) {
		this.entityManager = manager;
	}

	@Transactional
	public void save(Configuration conf) {
		this.entityManager.persist(conf);
	}

	public Configuration get(Long userId) {
		Configuration configuration;
		try {
			Query query = this.entityManager.createQuery(
					"select c from Configuration c where c.userId = ?1",
					Configuration.class).setParameter(1, userId);
			configuration = (Configuration) query.getSingleResult();
		} catch (NoResultException nre) {
			LOGGER.error(nre.getMessage(), nre);
			configuration = null;
		} catch (NonUniqueResultException nure) {
			LOGGER.error(nure.getMessage(), nure);
			configuration = null;
		}
		return configuration;
	}
}
