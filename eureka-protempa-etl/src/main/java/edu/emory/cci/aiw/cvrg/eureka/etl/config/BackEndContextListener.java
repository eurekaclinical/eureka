/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import com.google.inject.Injector;
import javax.servlet.ServletContextEvent;

import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

import edu.emory.cci.aiw.cvrg.eureka.etl.job.TaskManager;
import org.eurekaclinical.common.config.InjectorSupport;

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
	private Injector injector;

	@Override
	public void contextInitialized(ServletContextEvent inServletContextEvent) {
		super.contextInitialized(inServletContextEvent);
		initDatabase();
	}

	@Override
	protected Injector getInjector() {
		this.injector = new InjectorSupport(
				new Module[]{
					new AppModule(),
					new ETLServletModule(this.etlProperties),
					new JpaPersistModule(JPA_UNIT)
				},
				this.etlProperties).getInjector();
		return this.injector;
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		super.contextDestroyed(servletContextEvent);
		TaskManager taskManager
				= this.injector.getInstance(TaskManager.class
				);
		taskManager.shutdown();
	}

	private void initDatabase() {
		try (EtlJobRepairerExecutor executor
				= new EtlJobRepairerExecutor(JPA_UNIT)) {
			executor.execute();
		}
	}

}
