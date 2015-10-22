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
package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DeidPerPatientParamDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.common.dao.AuthorizedUserDao;

import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobEventDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaDestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaDestinationOffsetDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaEtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaEtlUserDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaJobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaJobEventDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaSourceConfigDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.SourceConfigDao;

/**
 *
 * @author hrathod
 */
public class AppTestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JpaPersistModule("backend-jpa-unit"));
		bind(JobDao.class).to(JpaJobDao.class);
		bind(JobEventDao.class).to(JpaJobEventDao.class);
		bind(AuthorizedUserDao.class).to(JpaEtlUserDao.class);
		bind(EtlGroupDao.class).to(JpaEtlGroupDao.class);
		bind(DestinationDao.class).to(JpaDestinationDao.class);
		bind(DeidPerPatientParamDao.class).to(JpaDestinationOffsetDao.class);
		bind(SourceConfigDao.class).to(JpaSourceConfigDao.class);
	}
}
