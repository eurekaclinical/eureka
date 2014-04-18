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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author hrathod
 */
@Singleton
public class UserInfoFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserInfoFilter.class);
	private ServicesClient servicesClient;

	@Inject
	public UserInfoFilter(ServicesClient inServicesClient) {
		this.servicesClient = inServicesClient;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do here, no configuration required
	}

	@Override
	public void doFilter(ServletRequest inRequest, ServletResponse inResponse, FilterChain inFilterChain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) inRequest;
//		HttpServletResponse servletResponse = (HttpServletResponse) inResponse;
		String remoteUser = servletRequest.getRemoteUser();
		LOGGER.debug("username: {}", remoteUser);
		boolean userIsActivated = false;
		if (remoteUser != null && remoteUser.length() > 0) {
			try {
				User user = this.servicesClient.getMe();
				if (user.isActive()) {
					userIsActivated = true;
				}
			} catch (ClientException ex) {
				// nothing to do here.  We assume that the user does not exist,
				// and pass along 'false' in the request attribute below.
			}
		}

		inRequest.setAttribute("userIsActivated", userIsActivated);
		inFilterChain.doFilter(inRequest, inResponse);
	}

	@Override
	public void destroy() {
		// nothing to do here, no resources held.
	}
}
