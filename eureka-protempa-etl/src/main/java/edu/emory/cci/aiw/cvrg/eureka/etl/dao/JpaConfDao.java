package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import java.util.List;

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

	public Configuration get(Long id) {
		Query query = this.entityManager.createQuery(
				"select c from Configuration c where c.id = ?1",
				Configuration.class).setParameter(1, id);
		List<Configuration> resultList = query.getResultList();
		return resultList.get(0);
	}
}
