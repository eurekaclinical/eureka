/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.servlet.worker.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;

public class SaveUserWorker implements ServletWorker {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
		ServicesClient servicesClient = new ServicesClient(eurekaServicesUrl);

		String id = req.getParameter("id");
		String activeStatus = req.getParameter("active");
		boolean isActivated = false;

		if (activeStatus != null) {

			isActivated = true;

		}

		User user = servicesClient.getUserById(Long.valueOf(id));
		String[] roles = req.getParameterValues("role");
		List<Role> userRoles = new ArrayList<Role>();
		for (String roleId : roles) {
			Role role = servicesClient.getRole(Long.valueOf(roleId));
			userRoles.add(role);
		}
		user.setRoles(userRoles);
		user.setActive(isActivated);

		try {
			servicesClient.updateUser(user);
		} catch (ClientException e) {
			throw new ServletException(e);
		}

		resp.sendRedirect(req.getContextPath() + "/protected/admin?action=list");
	}
}
