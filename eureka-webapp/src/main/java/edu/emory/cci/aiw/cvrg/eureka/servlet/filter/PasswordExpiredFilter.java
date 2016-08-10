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
package edu.emory.cci.aiw.cvrg.eureka.servlet.filter;

import java.io.IOException;
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

import com.google.inject.Singleton;
import org.eurekaclinical.eureka.client.comm.LocalUser;
import org.eurekaclinical.eureka.client.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.webapp.config.RequestAttributes;

/**
 * @author hrathod
 */
@Singleton
public class PasswordExpiredFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordExpiredFilter.class);
	private String redirectUrl;
	private String saveUrl;

	public PasswordExpiredFilter() {
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
            
		HttpServletRequest servletRequest = (HttpServletRequest) request;
                
		HttpServletResponse servletResponse = (HttpServletResponse) response;
                
		User user = (User) request.getAttribute(RequestAttributes.USER);
                
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
                                LOGGER.info("targetUrl is: "+targetUrl+ " fullRedirectUrl is: "+ fullRedirectUrl+ " fullSaveUrl is: "+fullSaveUrl);                                
				if (!targetUrl.equals(fullRedirectUrl) && !targetUrl
						.equals(fullSaveUrl)) {
					String encodeRedirectURL = servletResponse.encodeRedirectURL(fullRedirectUrl
							+ "?firstLogin=" + (user.getLastLogin() == null) + "&redirectURL=" + targetUrl);
                    //https://localhost:8443/eureka-webapp/protected/password_expiration.jsp?firstLogin=true&redirectURL=/eureka-webapp/protected/login                                        
					LOGGER.debug("encodeRedirectURL: {}", encodeRedirectURL);
					servletResponse.sendRedirect(encodeRedirectURL);
				} else {
					chain.doFilter(request, response);
				}
			} else {
				chain.doFilter(request, response);
			}
		}
	}

	@Override
	public void destroy() {
	}
}
