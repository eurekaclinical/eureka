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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;

public class RegisterUserServlet extends HttpServlet {

	private static Logger LOGGER = LoggerFactory.getLogger(RegisterUserServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
		ServicesClient servicesClient = new ServicesClient(eurekaServicesUrl);

		String email 		= req.getParameter("email");
		String verifyEmail 	= req.getParameter("verifyEmail");
		String firstName 	= req.getParameter("firstName");
		String lastName 	= req.getParameter("lastName");
		String organization = req.getParameter("organization");
		String password 	= req.getParameter("password");
		String verifyPassword = req.getParameter("verifyPassword");

		UserRequest userRequest = new UserRequest();
		userRequest.setFirstName(firstName);
		userRequest.setLastName(lastName);
		userRequest.setEmail(email);
		userRequest.setVerifyEmail(verifyEmail);
		userRequest.setOrganization(organization);
		userRequest.setVerifyPassword(verifyPassword);
		userRequest.setPassword(password);

		try {
			servicesClient.addUser(userRequest);
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().close();
		} catch (ClientException e) {
			resp.setContentType("text/plain");
			String msg = e.getMessage();
			LOGGER.debug("Error: {}", msg);
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.setContentLength(msg.length());
			resp.getWriter().write(msg);
			resp.getWriter().close();			
		}
	}
}
