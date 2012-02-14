package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;

public class JpaConfDao implements ConfDao {

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

		Query query = this.entityManager.createQuery(
				"select c from Configuration c where c.userId = ?1",
				Configuration.class).setParameter(1, userId);
		return (Configuration)query.getSingleResult();
	}
}
