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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;

/**
 * Servlet to handle user verification requests.
 * 
 * @author sagrava
 * @author hrathod
 * 
 */
public class VerifyUserServlet extends HttpServlet {

	/**
	 * Used for serialization/deserialization.
	 */
	private static final long serialVersionUID = -737043484641381552L;
	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(VerifyUserServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Client c;
		try {
			c = CommUtils.getClient();

			String eurekaServicesUrl = req.getSession().getServletContext()
					.getInitParameter("eureka-services-url");

			WebResource webResource = c.resource(eurekaServicesUrl);

			String code = req.getParameter("code");

			ClientResponse response = webResource
					.path("/api/user/verify/" + code)
					.accept(MediaType.TEXT_PLAIN).put(ClientResponse.class);

			int status = response.getClientResponseStatus().getStatusCode();
			if (status >= HttpServletResponse.SC_BAD_REQUEST) {

				String msg = response.getEntity(String.class);
				req.setAttribute("error", msg);
				LOGGER.debug("Error: {}", msg);
			}
			req.getRequestDispatcher("/registration_info.jsp").forward(req,
					resp);

		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
