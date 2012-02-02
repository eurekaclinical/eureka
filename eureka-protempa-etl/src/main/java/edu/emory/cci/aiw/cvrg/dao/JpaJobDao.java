package edu.emory.cci.aiw.cvrg.dao;

import javax.persistence.EntityManager;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;


public class JpaJobDao implements JobDao {

	private final EntityManager entityManager;

	@Inject
	public JpaJobDao (EntityManager manager) {

		this.entityManager = manager;
	}


	public void save (Job job) {

	}

	public Job get (Long id) {

		return null;
	}
}
