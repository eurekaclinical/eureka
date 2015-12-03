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
package edu.emory.cci.aiw.cvrg.eureka.servlet.filter;

import com.google.inject.Singleton;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
@Singleton
public class MessagesFilter implements Filter {
	
	private static final Logger LOGGER = 
			LoggerFactory.getLogger(MessagesFilter.class);
	
	private ServletContext servletContext;

	@Override
	public void init(FilterConfig fc) throws ServletException {
		this.servletContext = fc.getServletContext();
	}

	@Override
	public void doFilter(ServletRequest inRequest, ServletResponse inResponse, 
	FilterChain inFilterChain) throws IOException, ServletException {
		Locale locale = inRequest.getLocale();
		String resourceBundleName = 
				this.servletContext.getInitParameter(
				"javax.servlet.jsp.jstl.fmt.localizationContext");
		LOGGER.debug("Sending response text using locale {}", locale);
		ResourceBundle resourceBundle = 
				ResourceBundle.getBundle(resourceBundleName, locale);
		inRequest.setAttribute("messages", resourceBundle);
		inFilterChain.doFilter(inRequest, inResponse);
	}

	@Override
	public void destroy() {
	}
	
}
