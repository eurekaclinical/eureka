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
package edu.emory.cci.aiw.cvrg.eureka.servlet.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MultiHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.filter.RolesRequestWrapper;

/**
 * A filter to implement authorization controls to prevent backdoor
 * access.
 * 
 * @author sagrava
 * 
 */
public class AuthorizationFilter implements Filter {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthorizationFilter.class);

	/**
	 * Map of URIs to Roles that can access the URI.
	 * 
	 * Key = URI
	 * Value = Collection of Roles
	 */
	private MultiHashMap authMap = new MultiHashMap();

	@Override
	public void init(FilterConfig inFilterConfig) throws ServletException {
		String [] uriList = inFilterConfig.getInitParameter("URIs").split(",");
		
		for (String uri : uriList) {
			LOGGER.debug("Got URI {}", uri);
			String [] roles = inFilterConfig.getInitParameter(uri.trim()).split(",");
			for (String role : roles) {
				LOGGER.debug("Got Role {}", role);
				authMap.put(uri, role.trim());
				
			}
		}

	}

	@Override
	public void doFilter(ServletRequest inRequest, ServletResponse inResponse,
			FilterChain inChain) throws IOException, ServletException {
		
		HttpServletRequest servletRequest = (HttpServletRequest) inRequest;
		Principal principal = servletRequest.getUserPrincipal();
		if (principal != null) {
			LOGGER.debug("Principal: {}", principal.getName());
			RolesRequestWrapper roleWrapper = (RolesRequestWrapper) inRequest;
			Set<String> roles = roleWrapper.getRoles();
			String reqUri = ((HttpServletRequest)inRequest).getRequestURI();
			String queryStr = ((HttpServletRequest)inRequest).getQueryString();
			if (queryStr != null)
				reqUri = reqUri.replace(queryStr, "");
			String contextPath = servletRequest.getContextPath();
			reqUri = reqUri.replace(contextPath, "");
			LOGGER.debug("URI: {}", reqUri);
			Collection<String> uriRoles = authMap.getCollection(reqUri);
			if (uriRoles != null) {
				
				for (String uriRole : uriRoles) {
					for (String userRole : roles) {
						LOGGER.debug("uriRole: {}, uriRole: {}", reqUri, userRole);
						if (userRole.equals(uriRole)) {
							inChain.doFilter(inRequest, inResponse);
                            return;
						}
					}
				}
				
				inRequest.setAttribute("errorMsg",
						"You are not authorized to access this page");
				
				RequestDispatcher dispatcher = inRequest
						.getRequestDispatcher("/error.jsp");
				dispatcher.forward(inRequest, inResponse);
				
			}
			else {

				inChain.doFilter(inRequest, inResponse);
				
			}
		} 
		else {
			
			inChain.doFilter(inRequest, inResponse);
		}

	}

	@Override
	public void destroy() {
		this.authMap = null;
	}

}
