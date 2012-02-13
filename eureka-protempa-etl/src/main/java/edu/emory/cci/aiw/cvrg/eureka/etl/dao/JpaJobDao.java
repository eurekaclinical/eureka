package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.etl.resource.JobFilter;

public class JpaJobDao implements JobDao {

	private final EntityManager entityManager;

	@Inject
	public JpaJobDao(EntityManager manager) {
		this.entityManager = manager;
	}

	@Transactional
	public void save(Job job) {
		this.entityManager.persist(job);
	}

	public Job get(Long id) {
		Query query = this.entityManager.createQuery(
				"select j from Job j where j.id = ?1", Job.class).setParameter(
				1, id);
		List<Job> resultList = query.getResultList();
		return resultList.get(0);
	}

	public List<Job> get(JobFilter jobFilter) {
		Query query = this.entityManager.createQuery("select j from Job j",
				Job.class);
		List<Job> resultList = query.getResultList();
		List<Job> ret = new ArrayList<Job>(resultList.size());

		for (Job candidate : resultList) {
			if (jobFilter.evaluate(candidate)) {
				ret.add(candidate);
			}
		}

		return ret;
	}
}
