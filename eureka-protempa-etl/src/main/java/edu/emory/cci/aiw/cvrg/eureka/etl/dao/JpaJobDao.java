package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job_;

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
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaJobDao.class);
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
		return this.getEntityManager().find(Job.class, id);
	}

	@Override
	public List<Job> get(JobFilter jobFilter) {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Job> query = builder.createQuery(Job.class);
		Root<Job> root = query.from(Job.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (jobFilter.getJobId() != null) {
			predicates.add(builder.equal(root.get(Job_.id),
					jobFilter.getJobId()));
		}
		if (jobFilter.getUserId() != null) {
			predicates.add(builder.equal(root.get(Job_.userId),
					jobFilter.getUserId()));
		}
		if (jobFilter.getFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo(
					root.<Date>get(Job_.timestamp), jobFilter.getFrom()));
		}
		if (jobFilter.getTo() != null) {
			predicates.add(builder.lessThanOrEqualTo(
					root.<Date>get(Job_.timestamp), jobFilter.getTo()));
		}
		if (jobFilter.getState() != null) {
			predicates.add(builder.equal(
					root.join(Job_.jobEvents).get(JobEvent_.state),
					jobFilter.getState()));
		}

		Predicate[] predicatesArray = new Predicate[predicates.size()];
		predicates.toArray(predicatesArray);
		query.where(predicatesArray);
		TypedQuery<Job> typedQuery = entityManager.createQuery(query);
		return typedQuery.getResultList();
	}
}
