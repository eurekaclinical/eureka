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
 *
 * @author hrathod
 */
public class ContextTestListener extends GuiceServletContextListener {

	private final Injector injector = Guice.createInjector(new AppTestModule(),
			new ServletTestModule());
	private PersistService persistService;

	@Override
	protected final Injector getInjector() {
		return this.injector;
	}

	@Override
	public void contextInitialized(ServletContextEvent inServletContextEvent) {
		super.contextInitialized(inServletContextEvent);
		this.persistService = this.getInjector().getInstance(
				PersistService.class);
		this.persistService.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent inServletContextEvent) {
		super.contextDestroyed(inServletContextEvent);
		if (this.persistService != null) {
			this.persistService.stop();
		}
		TaskManager taskManager = this.injector.getInstance(TaskManager.class);
		taskManager.shutdown();
	}
}
