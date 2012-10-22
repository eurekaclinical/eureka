/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 Emory University
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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
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

	/**
	 * The Guice injector used to fetch instances of dependency injection
	 * enabled classes.
	 */
	private final Injector injector = Guice
			.createInjector(new ETLServletModule());
	/**
	 * The JPA persistence service, used to interact with the data store.
	 */
	private PersistService ps;

	@Override
	protected Injector getInjector() {
		return this.injector;
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		super.contextDestroyed(servletContextEvent);
		if (this.ps != null) {
			this.ps.stop();
		}
		TaskManager taskManager = this.getInjector().getInstance(TaskManager
			.class);
		taskManager.shutdown();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		this.ps = this.getInjector().getInstance(PersistService.class);
		this.ps.start();
		servletContextEvent.getServletContext().setAttribute("", new Object());
	}
}
