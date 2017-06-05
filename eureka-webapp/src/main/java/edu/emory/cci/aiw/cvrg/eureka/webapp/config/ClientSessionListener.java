package edu.emory.cci.aiw.cvrg.eureka.webapp.config;

/*-
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2017 Emory University
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

import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.EtlClient;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.eurekaclinical.user.client.EurekaClinicalUserProxyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
public class ClientSessionListener implements HttpSessionListener {
	
	private Logger LOGGER = LoggerFactory.getLogger(ClientSessionListener.class);
	
	@Inject
    private Injector injector;

	@Override
	public void sessionCreated(HttpSessionEvent hse) {
		LOGGER.info("Creating session for " + hse.getSession().getServletContext().getContextPath());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent hse) {
		LOGGER.info("Destroying session for " + hse.getSession().getServletContext().getContextPath());
		try {
			this.injector.getInstance(EurekaClinicalUserProxyClient.class).close();
		} catch (ConfigurationException ce) {}
		try {
			this.injector.getInstance(ServicesClient.class).close();
		} catch (ConfigurationException ce) {}
		try {
			this.injector.getInstance(EtlClient.class).close();
		} catch (ConfigurationException ce) {}
	}
	
}
