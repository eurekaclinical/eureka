package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.etl.resource.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.etl.resource.ProtempaDevice;

/**
 * Implements the {@link JobDao} interface, with the use of JPA entity managers.
 * 
 * @author gmilton
 * @author hrathod
 * 
 */

public class JpaJobDao implements JobDao {

	/**
	 * The provider for the entity managers used within the DAO.
	 */
	private final Provider<EntityManager> emProvider;
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaJobDao.class);

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
	 * @return
	 */
	private EntityManager getEntityManager() {
		return this.emProvider.get();
	}

	@Override
	@Transactional
	public void save(Job job) {
		this.getEntityManager().persist(job);
	}

	@Override
	public Job get(Long id) {

		try {

			Query query = this.getEntityManager()
				.createQuery("select j from Job j where j.id = ?1", Job.class)
				.setParameter(1, id);
			return (Job)query.getSingleResult();
		}
		catch (Exception e) {

			LOGGER.debug("JobDao exception " + e.getMessage());
		}
		return null;
	}

	@Override
	public List<Job> get(JobFilter jobFilter) {

		List<Job> ret = new ArrayList<Job>();
		try {

			Query query = this.getEntityManager().createQuery("select j from Job j", Job.class);
			List<Job> resultList = query.getResultList();

			for (Job candidate : resultList) {
				if (jobFilter.evaluate(candidate)) {
					ret.add(candidate);
				}
			}
		}
		catch (Exception e) {

			LOGGER.debug("JobDao exception " + e.getMessage());
		}
		return ret;
	}
}
