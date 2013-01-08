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
import java.util.Calendar;
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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

/**
 * @author hrathod
 */
public class PasswordExpiredFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger
			(PasswordExpiredFilter.class);
	private ServicesClient servicesClient;
	private String redirectUrl;
	private String saveUrl;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String serviceUrl = filterConfig.getServletContext()
				.getInitParameter("eureka-services-url");
		this.servicesClient = new ServicesClient(serviceUrl);
		this.redirectUrl = filterConfig.getInitParameter("redirect-url");
		this.saveUrl = filterConfig.getInitParameter("save-url");
		if (this.redirectUrl == null) {
			throw new ServletException("Parameter redirect-url must be set");
		}
		if (this.saveUrl == null) {
			throw new ServletException("Parameter save-url must be set");
		}
		LOGGER.info("redirect-url: {}", this.redirectUrl);
		LOGGER.info("save-url: {}", this.saveUrl);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, 
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest && response instanceof 
				HttpServletResponse) {
			HttpServletRequest servletRequest = (HttpServletRequest) request;
			HttpServletResponse servletResponse = (HttpServletResponse) 
					response;
			Principal principal = servletRequest.getUserPrincipal();
			if (principal != null) {
				LOGGER.info("username: {}", principal.getName());
				User user = this.servicesClient.getUserByName(principal
						.getName());
				Date now = Calendar.getInstance().getTime();
				Date expiration = user.getPasswordExpiration();
				LOGGER.info("expiration date: {}", user.getPasswordExpiration
						());
				if (expiration != null && now.after(expiration)) {
					String targetUrl = servletRequest.getRequestURI();
					String fullRedirectUrl = servletRequest.getContextPath() 
							+ this.redirectUrl;
					String fullSaveUrl = servletRequest.getContextPath() + 
							this.saveUrl;
					LOGGER.info("fullRedirectUrl: {}", fullRedirectUrl);
					LOGGER.info("fullSaveUrl: {}", fullSaveUrl);
					LOGGER.info("targetUrl: {}", targetUrl);
					if (!targetUrl.equals(fullRedirectUrl) && !targetUrl
							.equals(fullSaveUrl)) {
						servletResponse.sendRedirect(fullRedirectUrl);
					} else {
						chain.doFilter(request, response);
					}
				} else {
					chain.doFilter(request, response);
				}
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		this.servicesClient = null;
	}
}
