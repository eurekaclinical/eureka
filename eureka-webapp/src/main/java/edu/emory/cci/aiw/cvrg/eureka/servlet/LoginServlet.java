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
package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class LoginServlet extends HttpServlet {

	private static final String GET_BY_NAME_URL = "/api/user/byname/";
	private static final String PUT_USER_URL = "/api/user/put";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");

		Client client;
		client = CommUtils.getClient();
		Principal principal = req.getUserPrincipal();
		String userName = principal.getName();

		WebResource webResource = client.resource(eurekaServicesUrl);
		User user = webResource.path(GET_BY_NAME_URL + userName)
				.accept(MediaType.APPLICATION_JSON).get(User.class);
		user.setLastLogin(new Date());
		ClientResponse response = webResource.path(PUT_USER_URL)
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, user);
		if (response.getClientResponseStatus() != Status.OK) {
			throw new ServletException(
					"Could not update user, got response "
							+ response.getClientResponseStatus().toString());
		}
		resp.sendRedirect(req.getContextPath() + "/index.jsp");
	}
}
