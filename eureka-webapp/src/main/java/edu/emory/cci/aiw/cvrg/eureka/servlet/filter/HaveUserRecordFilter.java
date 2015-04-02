package edu.emory.cci.aiw.cvrg.eureka.servlet.filter;

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
import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.client.ClientResponse.Status;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Andrew Post
 */
@Singleton
public class HaveUserRecordFilter implements Filter {

	private static final Logger LOGGER
			= LoggerFactory.getLogger(MessagesFilter.class);

	private final ServicesClient servicesClient;

	@Inject
	public HaveUserRecordFilter(ServicesClient inServicesClient) {
		this.servicesClient = inServicesClient;
	}

	@Override
	public void init(FilterConfig inFilterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest inRequest, ServletResponse inResponse, FilterChain inFilterChain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) inRequest;
		HttpServletResponse servletResponse = (HttpServletResponse) inResponse;
		LOGGER.debug("username: {}", servletRequest.getRemoteUser());
		try {
			User user = this.servicesClient.getMe();
			if (!user.isActive()) {
				servletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			} else {
				inFilterChain.doFilter(inRequest, inResponse);
			}
		} catch (ClientException ex) {
			if (Status.NOT_FOUND.equals(ex.getResponseStatus())) {
				HttpSession session = servletRequest.getSession(false);
				if (session != null) {
					session.invalidate();
				}
				inFilterChain.doFilter(inRequest, inResponse);
			} else if (Status.UNAUTHORIZED.equals(ex.getResponseStatus())) {
				HttpSession session = servletRequest.getSession(false);
				if (session != null) {
					session.invalidate();
				}
				inFilterChain.doFilter(inRequest, inResponse);
			} else {
				throw new ServletException("Error getting user "
						+ servletRequest.getRemoteUser(), ex);
			}
		}
	}

	@Override
	public void destroy() {
	}

}
