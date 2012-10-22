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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class JobListServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
		try {
			Client client = CommUtils.getClient();
			Principal principal = req.getUserPrincipal();
			String userName = principal.getName();
					
			WebResource webResource = client.resource(eurekaServicesUrl);
			User user = webResource.path("/api/user/byname/"+userName)
					.accept(MediaType.APPLICATION_JSON).get(User.class);
			List<Job> jobs = webResource.path("/api/job/list/" + user.getId())
					.accept(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Job>>() {
						// Nothing to implement, used to hold returned data.
					});

			req.setAttribute("jobs", jobs);
			req.getRequestDispatcher("/protected/tool.jsp").forward(req, resp);
		} catch (NoSuchAlgorithmException nsae) {
			throw new ServletException(nsae);
		} catch (KeyManagementException kme) {
			throw new ServletException(kme);
		}
	}
}
