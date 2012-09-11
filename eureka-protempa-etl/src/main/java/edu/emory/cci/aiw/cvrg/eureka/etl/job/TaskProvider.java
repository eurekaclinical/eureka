package edu.emory.cci.aiw.cvrg.eureka.etl.job;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;

public class TaskProvider implements Provider<Task> {

	private final JobDao jobDao;

	@Inject
	public TaskProvider (JobDao inJobDao) {
		this.jobDao = inJobDao;
	}

	@Override
	public Task get() {
		return new Task(this.jobDao, new ETL());
	}
}
