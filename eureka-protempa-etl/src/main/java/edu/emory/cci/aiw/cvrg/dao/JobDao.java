package edu.emory.cci.aiw.cvrg.dao;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;

public interface JobDao {

	public void save (Job job);
	public Job get (Long id);
}
