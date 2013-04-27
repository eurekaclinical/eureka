/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;

/**
 * A data access object interface to retrieve and store information about
 * Protempa ETL jobs.
 *
 * @author hrathod
 *
 */
public interface JobDao extends Dao<JobEntity, Long> {

	/**
	 * Gets a list of jobs that meet the given filter criteria.
	 *
	 * @param jobFilter The filter criteria.
	 * @return A list of jobs that meet the filter criteria.
	 */
	public List<JobEntity> getWithFilter(JobFilter jobFilter);
	
	public List<JobEntity> getWithFilterDesc(JobFilter jobFilter);

}
