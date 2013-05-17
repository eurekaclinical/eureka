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

import com.google.inject.persist.PersistFilter;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.persist.jpa.JpaPersistModule;
import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import edu.emory.cci.aiw.cvrg.eureka.common.filter.RolesFilter;
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
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidator;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidatorImpl;

/**
 * A Guice configuration module for setting up the web infrastructure and
 * binding appropriate implementations to interfaces.
 *
 * @author hrathod
 *
 */
public class ETLServletModule extends JerseyServletModule {

	@Override
	protected void configureServlets() {
		bind(JobDao.class).to(JpaJobDao.class);
		bind(EtlUserDao.class).to(JpaEtlUserDao.class);
		bind(EtlGroupDao.class).to(JpaEtlGroupDao.class);
		bind(DestinationDao.class).to(JpaDestinationDao.class);
		bind(SourceConfigDao.class).to(JpaSourceConfigDao.class);
		bind(PropositionValidator.class).to(PropositionValidatorImpl.class);
		bind(Task.class).toProvider(TaskProvider.class);

		install(new JpaPersistModule(BackEndContextListener.JPA_UNIT));
		
		filter("/api/*").through(PersistFilter.class);
		Map<String, String> rolesFilterInitParams =
				new HashMap<String, String>();
		rolesFilterInitParams.put("datasource", "java:comp/env/jdbc/EurekaService");
		rolesFilterInitParams.put("sql", "select a.name as role from roles a, user_role b, users c where a.id=b.role_id and b.user_id=c.id and c.email=?");
		rolesFilterInitParams.put("rolecolumn", "role");
		filter("/api/*").through(RolesFilter.class, rolesFilterInitParams);
		Map<String, String> params = new HashMap<String, String>();
		params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		params.put(PackagesResourceConfig.PROPERTY_PACKAGES,
				"edu.emory.cci.aiw.cvrg.eureka.etl.resource;edu.emory.cci.aiw.cvrg.eureka.common.json");
		params.put(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
				RolesAllowedResourceFilterFactory.class.getName());

		serve("/api/*").with(GuiceContainer.class, params);
	}
}
