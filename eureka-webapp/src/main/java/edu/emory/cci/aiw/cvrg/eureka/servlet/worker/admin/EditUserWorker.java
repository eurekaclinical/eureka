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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;

public class EditUserWorker implements ServletWorker {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			String eurekaServicesUrl = req.getSession().getServletContext()
					.getInitParameter("eureka-services-url");

			String id = req.getParameter("id");
			Client c = CommUtils.getClient();

			WebResource webResource = c.resource(eurekaServicesUrl);
			User user = webResource.path("/api/user/byid/" + id)
					.accept(MediaType.APPLICATION_JSON).get(User.class);

			webResource = c.resource(eurekaServicesUrl);
			List<Role> roles = webResource.path("/api/role/list")
					.accept(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Role>>() {
						// Nothing to implement, used to hold returned data.
					});

			req.setAttribute("roles", roles);
			req.setAttribute("user", user);
			req.getRequestDispatcher("/protected/edit_user.jsp").forward(req,
					resp);
		} catch (KeyManagementException e) {
			throw new ServletException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new ServletException(e);
		}
	}
}
