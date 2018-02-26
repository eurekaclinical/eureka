package edu.emory.cci.aiw.cvrg.eureka.webapp.config;

/*
 * #%L
 * Eureka WebApp
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

import com.google.inject.Injector;
import javax.servlet.ServletContextEvent;

import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

import edu.emory.cci.aiw.cvrg.eureka.webapp.comm.clients.EtlClientProvider;

import javax.servlet.ServletContext;
import org.eurekaclinical.common.config.ApiGatewayServletModule;
import org.eurekaclinical.common.config.ClientSessionListener;
import org.eurekaclinical.common.config.InjectorSupport;

/**
 *
 * @author Andrew Post
 * @author hrathod
 */
public class WebappListener extends GuiceServletContextListener {

	private final WebappProperties webappProperties;
	private final EurekaClinicalUserClientProvider userClientProvider;
	private final ServicesClientProvider servicesClientProvider;
	private final EtlClientProvider etlClientProvider;
	private final EurekaClinicalRegistryClientProvider registryClientProvider;
	private Injector injector;

	public WebappListener() {
		this.webappProperties = new WebappProperties();
		this.servicesClientProvider = new ServicesClientProvider(this.webappProperties.getServiceUrl());
		this.etlClientProvider = new EtlClientProvider(this.webappProperties.getEtlUrl());
		this.userClientProvider = new EurekaClinicalUserClientProvider(this.webappProperties.getUserServiceUrl());
		this.registryClientProvider = new EurekaClinicalRegistryClientProvider(this.webappProperties.getRegistryServiceUrl());
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		ServletContext servletContext = servletContextEvent.getServletContext();
		servletContext.addListener(new ClientSessionListener());
		servletContext.setAttribute(
				"webappProperties", this.webappProperties);
	}



	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		super.contextDestroyed(servletContextEvent);
		servletContextEvent.getServletContext().removeAttribute(
				"webappProperties");
	}

	@Override
	protected Injector getInjector() {
		this.injector = new InjectorSupport(
				new Module[]{
					new AppModule(this.webappProperties, this.servicesClientProvider, this.etlClientProvider, this.userClientProvider, this.registryClientProvider),
					new ApiGatewayServletModule(this.webappProperties)
				},
				this.webappProperties).getInjector();
		return this.injector;
	}
	
}
