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
		this.servletContext = null;
	}
	
}
