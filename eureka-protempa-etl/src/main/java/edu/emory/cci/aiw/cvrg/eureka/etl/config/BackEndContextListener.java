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

import com.google.inject.Injector;
import javax.servlet.ServletContextEvent;

import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import edu.emory.cci.aiw.cvrg.eureka.common.config.InjectorSupport;

import edu.emory.cci.aiw.cvrg.eureka.etl.job.TaskManager;

/**
 * Loaded up on application initialization, sets up the application with Guice
 * injector and any other bootup processes.
 *
 * @author hrathod
 *
 */
public class BackEndContextListener extends GuiceServletContextListener {

	private static final String JPA_UNIT = "backend-jpa-unit";
	private final EtlProperties etlProperties = new EtlProperties();
	private InjectorSupport injectorSupport;

	@Override
	public void contextInitialized(ServletContextEvent inServletContextEvent) {
		super.contextInitialized(inServletContextEvent);
		initDatabase();
	}
	
	@Override
	protected Injector getInjector() {
		/*
		 * Must be created here in order for the modules to initialize 
		 * correctly.
		 */
		if (this.injectorSupport == null) {
			this.injectorSupport = new InjectorSupport(
			new Module[]{
				new AppModule(),
				new ETLServletModule(this.etlProperties),
				new JpaPersistModule(JPA_UNIT)
			},
			this.etlProperties);
		}
		return this.injectorSupport.getInjector();
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		super.contextDestroyed(servletContextEvent);
		TaskManager taskManager
				= getInjector().getInstance(TaskManager.class);
		taskManager.shutdown();
	}

	private void initDatabase() {
		try (EtlJobRepairerExecutor executor
				= new EtlJobRepairerExecutor(JPA_UNIT)) {
			executor.execute();
		}
	}


}
