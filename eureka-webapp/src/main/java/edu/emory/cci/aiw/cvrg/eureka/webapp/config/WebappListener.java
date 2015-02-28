package edu.emory.cci.aiw.cvrg.eureka.webapp.config;

/*
 * #%L
 * Eureka WebApp
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
import com.google.inject.Injector;
import javax.servlet.ServletContextEvent;

import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import edu.emory.cci.aiw.cvrg.eureka.common.config.InjectorSupport;

/**
 *
 * @author Andrew Post
 * @author hrathod
 */
public class WebappListener extends GuiceServletContextListener {

	private final WebappProperties webappProperties;

	public WebappListener() {
		this.webappProperties = new WebappProperties();
	}
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		servletContextEvent.getServletContext().setAttribute(
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
		return new InjectorSupport(
				new Module[]{
					new AppModule(this.webappProperties),
					new ServletModule(this.webappProperties)
				},
				this.webappProperties).getInjector();
	}

}
