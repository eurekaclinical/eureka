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

import com.sun.jersey.api.client.ClientResponse;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Servlet implementation class ForgotPasswordServlet
 */
public class ForgotPasswordServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger LOGGER =
			LoggerFactory.getLogger(ForgotPasswordServlet.class);

	public ForgotPasswordServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String eurekaServicesUrl = request.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
		ServicesClient servicesClient = new ServicesClient(eurekaServicesUrl);
		String email = request.getParameter("email");
		try {
			servicesClient.resetPassword(email);
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(HttpServletResponse.SC_OK);
		} catch (ClientException ex) {
			LOGGER.error("Error resetting password for user {}", email, ex);
			if (ClientResponse.Status.NOT_FOUND.equals(ex.getResponseStatus())) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				String formattedMsg = MessageFormat.format(ex.getMessage(), "aiwhelp@emory.edu");
				response.getWriter().write(formattedMsg);
			} else {
				ResourceBundle messages =
						(ResourceBundle) request.getAttribute("messages");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				String msg = messages.getString("passwordChange.error.internalServerError");
				String formattedMsg = MessageFormat.format(msg, "aiwhelp@emory.edu");
				response.getWriter().write(formattedMsg);
			}
		}
	}
}
