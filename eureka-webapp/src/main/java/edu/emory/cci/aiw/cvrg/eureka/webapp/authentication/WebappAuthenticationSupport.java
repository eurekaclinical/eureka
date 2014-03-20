package edu.emory.cci.aiw.cvrg.eureka.webapp.authentication;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.authentication.AbstractAuthenticationSupport;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Andrew Post
 */
public final class WebappAuthenticationSupport extends AbstractAuthenticationSupport {
	private final ServicesClient servicesClient;
	
	public WebappAuthenticationSupport(ServicesClient inServicesClient) {
		if (inServicesClient == null) {
			throw new IllegalArgumentException("inServicesClient cannot be null");
		}
		
		this.servicesClient = inServicesClient;
	}
	
	public User getMe(HttpServletRequest req) throws ServletException {
		User user;
		try {
			user = this.servicesClient.getMe();
		} catch (ClientException ex) {
			throw new ServletException("Error getting user " + req.getRemoteUser(), ex);
		}
		return user;
	}
}
