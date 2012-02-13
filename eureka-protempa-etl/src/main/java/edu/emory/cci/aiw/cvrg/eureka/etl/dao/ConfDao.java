package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;


public interface ConfDao {

	public void save (Configuration conf);
	public Configuration get (Long id);
}
