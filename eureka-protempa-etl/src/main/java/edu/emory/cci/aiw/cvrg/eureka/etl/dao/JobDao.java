package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.etl.resource.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;

public interface JobDao {

	public void save (Job job);
	public Job get (Long id);
	public List<Job> get (JobFilter jobFilter);
}
