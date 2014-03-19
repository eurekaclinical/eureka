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

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;

/**
 * @author hrathod
 */
public class PingServlet extends HttpServlet {

	private final ServicesClient servicesClient;

	@Inject
	public PingServlet (ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userId = req.getParameter("id");
		User user;
		try {
			user = this.servicesClient.getUserById(Long.valueOf(userId));
		} catch (ClientException ex) {
			throw new ServletException("Error pinging services layer", ex);
		}
		resp.setContentType("text/plain");
		try {
			this.servicesClient.pingAccount(user.getId());
			resp.getWriter().println("Ping successful");
		} catch (ClientException e) {
			resp.getWriter().println(e.getMessage());
		}
	}
}
