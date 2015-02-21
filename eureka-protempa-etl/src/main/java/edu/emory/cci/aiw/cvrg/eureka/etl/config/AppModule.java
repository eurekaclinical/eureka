package edu.emory.cci.aiw.cvrg.eureka.etl.config;

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

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlUserDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaDestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaEtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaEtlUserDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaJobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaSourceConfigDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.SourceConfigDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.job.Task;
import edu.emory.cci.aiw.cvrg.eureka.etl.job.TaskProvider;

/**
 * @author hrathod
 */
public class AppModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(JobDao.class).to(JpaJobDao.class);
		bind(EtlUserDao.class).to(JpaEtlUserDao.class);
		bind(EtlGroupDao.class).to(JpaEtlGroupDao.class);
		bind(DestinationDao.class).to(JpaDestinationDao.class);
		bind(SourceConfigDao.class).to(JpaSourceConfigDao.class);
		bind(Task.class).toProvider(TaskProvider.class);
	}
}
