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
package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse.Status;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.LdapUserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.LocalUserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.OAuthUserRequest;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.OAuthProvider;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.jasig.cas.client.authentication.AttributePrincipal;

public class RegisterUserServlet extends HttpServlet {

	private static Logger LOGGER = LoggerFactory.getLogger(RegisterUserServlet.class);
	private static final ResourceBundle messages
			= ResourceBundle.getBundle("Messages");
	private final ServicesClient servicesClient;

	@Inject
	public RegisterUserServlet(ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		AttributePrincipal principal
				= (AttributePrincipal) req.getUserPrincipal();
		String authenticationMethod;
		if (principal != null) {
			authenticationMethod = (String) principal.getAttributes().get("authenticationMethod");
		} else {
			authenticationMethod = AuthenticationMethod.LOCAL.name();
		}

		try {
			String username = req.getParameter("username");
			String email = req.getParameter("email");
			String verifyEmail = req.getParameter("verifyEmail");
			String firstName = req.getParameter("firstName");
			String lastName = req.getParameter("lastName");
			String organization = req.getParameter("organization");
			String password = req.getParameter("password");
			String verifyPassword = req.getParameter("verifyPassword");
			String title = req.getParameter("title");
			String department = req.getParameter("department");
			String fullName = req.getParameter("fullName");
			String remoteUser = req.getRemoteUser();
			UserRequest userRequest;
			try {
				switch (AuthenticationMethod.valueOf(authenticationMethod)) {
					case LOCAL:
						LocalUserRequest localUserRequest = new LocalUserRequest();
						localUserRequest.setVerifyPassword(verifyPassword);
						localUserRequest.setPassword(password);
						localUserRequest.setUsername(email);
						userRequest = localUserRequest;
						break;
					case LDAP:
						userRequest = new LdapUserRequest();
						userRequest.setUsername(remoteUser);
						break;
					case OAUTH:
						OAuthProvider oauthProvider
								= this.servicesClient.getOAuthProviderByName((String) principal.getAttributes().get("providerType"));
						OAuthUserRequest oauthUserRequest = new OAuthUserRequest();
						oauthUserRequest.setUsername(remoteUser);
						oauthUserRequest.setProviderUsername(username);
						oauthUserRequest.setOAuthProvider(oauthProvider.getId());
						userRequest = oauthUserRequest;
						break;
					default:
						throw new ServletException("Unexpected authentication method: "
						+ authenticationMethod);
				}
			} catch (IllegalArgumentException iae) {
				throw new ServletException("Invalid authentication method: "
						+ authenticationMethod);
			}

			if (fullName == null || fullName.trim().length() == 0) {
				fullName = MessageFormat.format(messages.getString("registerUserServlet.fullName"), new Object[]{firstName, lastName});
			}

			userRequest.setFirstName(firstName);
			userRequest.setLastName(lastName);
			userRequest.setEmail(email);
			userRequest.setVerifyEmail(verifyEmail);
			userRequest.setOrganization(organization);
			userRequest.setTitle(title);
			userRequest.setDepartment(department);
			userRequest.setFullName(fullName);

			this.servicesClient.addUser(userRequest);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (ClientException e) {
			Status responseStatus = e.getResponseStatus();
			if (responseStatus.equals(Status.CONFLICT)) {
				resp.setStatus(HttpServletResponse.SC_CONFLICT);
			} else if (responseStatus.equals(Status.BAD_REQUEST)) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			String msg = e.getMessage();
			resp.setContentType("text/plain");
			LOGGER.debug("Error: {}", msg);
			resp.setContentLength(msg.length());
			resp.getWriter().write(msg);

		}

	}
}
