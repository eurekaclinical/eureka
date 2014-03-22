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

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.webapp.config.WebappProperties;

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
	private final ServicesClient servicesClient;
	private final WebappProperties properties;

	@Inject
	public VerifyUserServlet (ServicesClient inClient) {
		this.servicesClient = inClient;
		this.properties = new WebappProperties();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String code = req.getParameter("code");

		try {
			this.servicesClient.verifyUser(code);
		} catch (ClientException e) {
			throw new ServletException(e);
		}
		
		req.getRequestDispatcher("/registration_info.jsp").forward(req,
				resp);
	}
}
