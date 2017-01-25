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
package edu.emory.cci.aiw.cvrg.eureka.servlet.cohort;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import org.eurekaclinical.eureka.client.comm.User;
import org.eurekaclinical.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.webapp.authentication.WebappAuthenticationSupport;

public class DeleteCohortServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeleteCohortServlet.class);

	private final ServicesClient servicesClient;
	private final WebappAuthenticationSupport authenticationSupport;

	@Inject
	public DeleteCohortServlet(ServicesClient inClient) {
		this.servicesClient = inClient;
		this.authenticationSupport = new WebappAuthenticationSupport();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

		LOGGER.debug("DeleteCohortServlet");
		String propKey = req.getParameter("key");

		try {
			User user = this.authenticationSupport.getMe(req);
			this.servicesClient.deleteDestination(user.getId(), propKey);
		} catch (ClientException e) {
			resp.setContentType(MediaType.TEXT_PLAIN);
			try {
				switch (e.getResponseStatus()) {
					case UNAUTHORIZED:
						this.authenticationSupport.needsToLogin(req, resp);
						break;
					case INTERNAL_SERVER_ERROR:
						resp.setStatus(
								HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						LOGGER.error("Error deleting cohort " + propKey, e);
						ResourceBundle messages
								= (ResourceBundle) req.getAttribute("messages");
						String msg
								= messages.getString("deleteCohort.error.internalServerError");
						resp.getWriter().write(msg);
						break;
					default:
						LOGGER.debug("Deleting cohort {} failed", propKey, e);
						resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						resp.getWriter().write(e.getMessage());
				}
			} catch (IOException ex) {
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				LOGGER.error("IO Error writing response status", ex);
				try {
					resp.getWriter().write("Internal server error.");
				} catch (IOException ignore) {
					LOGGER.error("Error writing the internal server error message: {}", ignore);
				}
			}
		}
	}
}
