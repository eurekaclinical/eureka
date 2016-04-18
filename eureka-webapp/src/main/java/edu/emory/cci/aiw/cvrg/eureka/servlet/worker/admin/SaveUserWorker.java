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
package edu.emory.cci.aiw.cvrg.eureka.servlet.worker.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;

public class SaveUserWorker implements ServletWorker {

	private final ServicesClient servicesClient;

	public SaveUserWorker(ServicesClient inServicesClient) {
		this.servicesClient = inServicesClient;
	}

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String id = req.getParameter("id");
		String activeStatus = req.getParameter("active");
		boolean isActivated = false;

		if (activeStatus != null) {
			isActivated = true;

		}
		try {
			User user = this.servicesClient.getUserById(Long.valueOf(id));
			String[] roles = req.getParameterValues("role");
			List<Long> userRoles = new ArrayList<>();
			if (roles != null) {
				for (String roleId : roles) {
					try {
						userRoles.add(Long.valueOf(roleId));
					} catch (NumberFormatException nfe) {
						throw new ServletException(nfe);
					}
				}
			}
			user.setRoles(userRoles);
			user.setActive(isActivated);

			this.servicesClient.updateUser(user,Long.valueOf(id));
		} catch (ClientException e) {
			throw new ServletException("Error saving user", e);
		}

		resp.sendRedirect(req.getContextPath() + "/protected/admin?action=list");
	}
}
