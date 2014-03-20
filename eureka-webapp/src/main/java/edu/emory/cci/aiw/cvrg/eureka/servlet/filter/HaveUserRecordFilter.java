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
import com.sun.jersey.api.client.ClientResponse.Status;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.webapp.authentication.WebappAuthenticationSupport;
import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
@Singleton
public class HaveUserRecordFilter implements Filter {

	private static final Logger LOGGER
			= LoggerFactory.getLogger(MessagesFilter.class);

	private ServletContext servletContext;
	private String redirectUrl;
	private final ServicesClient servicesClient;
	private final WebappAuthenticationSupport authenticationSupport;

	@Inject
	public HaveUserRecordFilter(ServicesClient inServicesClient) {
		this.servicesClient = inServicesClient;
		this.authenticationSupport = new WebappAuthenticationSupport(this.servicesClient);
	}

	@Override
	public void init(FilterConfig inFilterConfig) throws ServletException {
		this.servletContext = inFilterConfig.getServletContext();
		this.redirectUrl = inFilterConfig.getInitParameter("redirect-url");
		if (this.redirectUrl == null) {
			throw new ServletException("Parameter redirect-url must be set");
		}
		LOGGER.debug("redirect-url: {}", this.redirectUrl);
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
				/*
				 * CAS has authenticated the user, but we don't have a record
				 * of the user. This means the user has been externally 
				 * authenticated, so create a user request.
				 */
				String fullRedirectUrl = servletRequest.getContextPath()
						+ this.redirectUrl;
				Map<String, Object> attributes = this.authenticationSupport.getUserPrincipalAttributes(servletRequest);
				UriBuilder uriBuilder = UriBuilder.fromUri(fullRedirectUrl);

				Object firstName = attributes.get("firstName");
				Object fullName = attributes.get("fullName");
				Object lastName = attributes.get("lastName");
				if ((firstName == null || lastName == null)
						&& fullName instanceof String) {
					PersonNameSplitter splitter
							= new PersonNameSplitter((String) fullName);
					if (firstName == null) {
						firstName = splitter.getFirstName();
					}
					if (lastName == null) {
						lastName = splitter.getLastName();
					}
				}
				if (firstName != null) {
					uriBuilder = uriBuilder.queryParam("firstName", firstName);
				}
				if (lastName != null) {
					uriBuilder = uriBuilder.queryParam("lastName", lastName);
				}
				if (fullName != null) {
					uriBuilder = uriBuilder.queryParam("fullName", fullName);
				}

				String[] attrNames = {
					"title", "department", "organization", "email", 
					"username"};
				for (String attrName : attrNames) {
					Object val = attributes.get(attrName);
					if (val != null) {
						uriBuilder = uriBuilder.queryParam(attrName, val);
					}
				}
				String redirectUrlWithQueryParams
						= uriBuilder.build().toString();

				servletResponse.sendRedirect(redirectUrlWithQueryParams);
			} else {
				throw new ServletException("Error getting user "
						+ servletRequest.getRemoteUser(), ex);
			}
		}
	}

	@Override
	public void destroy() {
		this.servletContext = null;
	}
	
}
