package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.etl.resource.JobFilter;

/**
 * Implements the {@link JobDao} interface, with the use of JPA entity managers.
 * 
 * @author gmilton
 * @author hrathod
 * 
 */

public class JpaJobDao implements JobDao {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JpaJobDao.class);
	/**
	 * The provider for the entity managers used within the DAO.
	 */
	private final Provider<EntityManager> emProvider;

	/**
	 * Construct instance with the given EntityManager provider.
	 * 
	 * @param inEMProvider The entity manager provider.
	 */
	@Inject
	public JpaJobDao(Provider<EntityManager> inEMProvider) {
		this.emProvider = inEMProvider;
	}

	/**
	 * Get an entity manager to use.
	 * 
	 * @return A new entity manager.
	 */
	private EntityManager getEntityManager() {
		return this.emProvider.get();
	}

	@Override
	@Transactional
	public void save(Job job) {
		LOGGER.debug("Persisting job: {}", job);
		EntityManager entityManager = this.getEntityManager();
		entityManager.persist(job);
		entityManager.flush();
	}

	@Override
	public void refresh(Job job) {
		this.getEntityManager().refresh(job);
	}

	@Override
	public Job get(Long id) {
		Job job = null;
		try {
			Query query = this
					.getEntityManager()
					.createQuery("select j from Job j where j.id = ?1",
							Job.class).setParameter(1, id);
			job = (Job) query.getSingleResult();
		} catch (NoResultException nre) {
			LOGGER.error(nre.getMessage(), nre);
		} catch (NonUniqueResultException nure) {
			LOGGER.error(nure.getMessage(), nure);
		}
		return job;
	}

	@Override
	public List<Job> get(JobFilter jobFilter) {

		List<Job> ret = new ArrayList<Job>();
		Query query = this.getEntityManager().createQuery(
				"select j from Job j", Job.class);
		List<Job> resultList = query.getResultList();

		for (Job candidate : resultList) {
			if (jobFilter.evaluate(candidate)) {
				ret.add(candidate);
			}
		}
		return ret;
	}
}
