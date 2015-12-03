package edu.emory.cci.aiw.cvrg.eureka.servlet.filter;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

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
		String remoteUser = servletRequest.getRemoteUser();
		boolean userIsActivated = false;
		if (!StringUtils.isEmpty(remoteUser)) {
			User user = (User) inRequest.getAttribute("user");
			if (user != null && user.isActive()) {
				userIsActivated = true;
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
