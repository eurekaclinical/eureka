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
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlUserDao;

import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaEtlUserDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaJobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidator;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidatorImpl;

/**
 *
 * @author hrathod
 */
public class AppTestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JpaPersistModule("backend-jpa-unit"));
		bind(JobDao.class).to(JpaJobDao.class);
		bind(EtlUserDao.class).to(JpaEtlUserDao.class);
		bind(PropositionValidator.class).to(PropositionValidatorImpl.class);
	}
}
