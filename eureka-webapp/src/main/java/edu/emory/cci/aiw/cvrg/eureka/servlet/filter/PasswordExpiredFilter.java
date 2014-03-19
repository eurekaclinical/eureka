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
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.LocalUser;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;

/**
 * @author hrathod
 */
@Singleton
public class PasswordExpiredFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordExpiredFilter.class);
	private final ServicesClient servicesClient;
	private String redirectUrl;
	private String saveUrl;

	@Inject
	public PasswordExpiredFilter(ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.redirectUrl = filterConfig.getInitParameter("redirect-url");
		this.saveUrl = filterConfig.getInitParameter("save-url");
		if (this.redirectUrl == null) {
			throw new ServletException("Parameter redirect-url must be set");
		}
		if (this.saveUrl == null) {
			throw new ServletException("Parameter save-url must be set");
		}
		LOGGER.debug("redirect-url: {}", this.redirectUrl);
		LOGGER.debug("save-url: {}", this.saveUrl);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletRequest servletRequest = (HttpServletRequest) request;
			HttpServletResponse servletResponse = (HttpServletResponse) response;
			Principal principal = servletRequest.getUserPrincipal();
			LOGGER.debug("username: {}", principal.getName());
			User user;
			try {
				user = this.servicesClient.getUserByName(principal
						.getName());
			} catch (ClientException ex) {
				throw new ServletException("Error getting user " + principal.getName(), ex);
			}
			if (!(user instanceof LocalUser)) {
				chain.doFilter(request, response);
			} else {
				Date now = new Date();
				Date expiration = ((LocalUser) user).getPasswordExpiration();
				LOGGER.debug("expiration date: {}", expiration);
				if (expiration != null && now.after(expiration)) {
					String targetUrl = servletRequest.getRequestURI();
					String fullRedirectUrl = servletRequest.getContextPath()
							+ this.redirectUrl;
					String fullSaveUrl = servletRequest.getContextPath()
							+ this.saveUrl;
					LOGGER.debug("fullRedirectUrl: {}", fullRedirectUrl);
					LOGGER.debug("fullSaveUrl: {}", fullSaveUrl);
					LOGGER.debug("targetUrl: {}", targetUrl);
					if (!targetUrl.equals(fullRedirectUrl) && !targetUrl
							.equals(fullSaveUrl)) {
						servletResponse.sendRedirect(fullRedirectUrl + "?firstLogin=" + (user.getLastLogin() == null) + "&redirectURL=" + targetUrl);
					} else {
						chain.doFilter(request, response);
					}
				} else {
					chain.doFilter(request, response);
				}
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}
}
