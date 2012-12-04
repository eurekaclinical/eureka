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
package edu.emory.cci.aiw.cvrg.eureka.servlet.proposition;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class SavePropositionServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
	        .getLogger(SavePropositionServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
		LOGGER.debug("SavePropositionServlet");
		ObjectMapper objectMapper = new ObjectMapper();
		DataElement dataElement = objectMapper.readValue(req.getReader(),
				DataElement.class);

		System.out.println("dataElement.getType() = " + dataElement.getType
				());

		String username = req.getUserPrincipal().getName();
		String servicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
		ServicesClient servicesClient = new ServicesClient(servicesUrl);
		User user = servicesClient.getUserByName(username);
		try {
			dataElement.setUserId(user.getId());
			if (dataElement.getId() == null) {
				servicesClient.saveUserElement(dataElement);
			} else {
				servicesClient.updateUserElement(dataElement);
			}
		} catch (ClientException e) {
			System.out.println(e.getMessage());
			req.setAttribute("error", e.getMessage());
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {

		doPost(req, resp);
	}
}
