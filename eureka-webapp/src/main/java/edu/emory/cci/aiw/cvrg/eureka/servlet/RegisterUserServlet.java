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
package edu.emory.cci.aiw.cvrg.eureka.servlet;

import org.eurekaclinical.eureka.client.comm.authentication.AuthenticationMethod;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse.Status;
import org.eurekaclinical.eureka.client.comm.LdapUserRequest;
import org.eurekaclinical.eureka.client.comm.LocalUserRequest;
import org.eurekaclinical.eureka.client.comm.OAuthUserRequest;
import org.eurekaclinical.eureka.client.comm.UserRequest;
import org.eurekaclinical.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class RegisterUserServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterUserServlet.class);
	private static final ResourceBundle messages = ResourceBundle.getBundle("Messages");
	private final ServicesClient servicesClient;

	@Inject
	public RegisterUserServlet(ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String authenticationMethodStr = req.getParameter("authenticationMethod");
		AuthenticationMethod authenticationMethod;
		try {
			authenticationMethod = AuthenticationMethod.valueOf(authenticationMethodStr);
		} catch (IllegalArgumentException ex) {
			throw new ServletException("Invalid authentication method: " + authenticationMethodStr);
		}

		try {
			String username = req.getParameter("username");
			String email = req.getParameter("email");
			String verifyEmail = req.getParameter("verifyEmail");
			String firstName = req.getParameter("firstName");
			String lastName = req.getParameter("lastName");
			String organization = req.getParameter("organization");
			String title = req.getParameter("title");
			String department = req.getParameter("department");
			String fullName = req.getParameter("fullName");
			UserRequest userRequest;
			try {
				switch (authenticationMethod) {
					case LOCAL:
						String password = req.getParameter("password");
						String verifyPassword = req.getParameter("verifyPassword");
						LocalUserRequest localUserRequest = new LocalUserRequest();
						localUserRequest.setVerifyPassword(verifyPassword);
						localUserRequest.setPassword(password);
						localUserRequest.setUsername(email);
						userRequest = localUserRequest;
						break;
					case LDAP:
						userRequest = new LdapUserRequest();
						userRequest.setUsername(username);
						break;
					case OAUTH:
						String providerUsername = req.getParameter("providerUsername");
						String oauthProvider = req.getParameter("oauthProvider");
						OAuthUserRequest oauthUserRequest = new OAuthUserRequest();
						oauthUserRequest.setUsername(username);
						oauthUserRequest.setProviderUsername(providerUsername);
						oauthUserRequest.setOAuthProvider(oauthProvider);
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
			String msg = e.getMessage();
			Status responseStatus = e.getResponseStatus();
			switch (responseStatus) {
                        	case CONFLICT:
                            		resp.setStatus(HttpServletResponse.SC_CONFLICT);
                            		msg = messages.getString("registerUserServlet.error.conflict");
                            		break;
                        	case BAD_REQUEST:
                            		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            		msg = messages.getString("registerUserServlet.error.badRequest");
                            		break;
                        	default:
                            		throw new ServletException();
			}
			resp.setContentType("text/plain");
			LOGGER.debug("Error: {}", msg);
			resp.setContentLength(msg.length());
			resp.getWriter().write(msg);

		}

	}
}
