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

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

import edu.emory.cci.aiw.cvrg.eureka.etl.job.TaskManager;

/**
 * Loaded up on application initialization, sets up the application with Guice
 * injector and any other bootup processes.
 *
 * @author hrathod
 *
 */
public class BackEndContextListener extends GuiceServletContextListener {

	private static Logger LOGGER =
			LoggerFactory.getLogger(BackEndContextListener.class);
	static final String JPA_UNIT = "backend-jpa-unit";
	/**
	 * The Guice injector used to fetch instances of dependency injection
	 * enabled classes.
	 */
	private Injector injector = null;

	@Override
	protected Injector getInjector() {
		if (null == this.injector) {
			this.injector = Guice.createInjector(
								new AppModule(),
								new ETLServletModule(new EtlProperties()),
								new JpaPersistModule(JPA_UNIT));
		}
		return this.injector;
	}

	@Override
	public void contextInitialized(ServletContextEvent inServletContextEvent) {
		super.contextInitialized(inServletContextEvent);
		initDatabase();
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		super.contextDestroyed(servletContextEvent);
		TaskManager taskManager =
				this.getInjector().getInstance(TaskManager.class);
		taskManager.shutdown();
	}

	private void initDatabase() {
		final EtlProperties etlProperties = 
				getInjector().getInstance(EtlProperties.class);
		try (EtlJobRepairerExecutor executor = 
				new EtlJobRepairerExecutor(JPA_UNIT, etlProperties)) {
			executor.execute();
		}
	}
}
