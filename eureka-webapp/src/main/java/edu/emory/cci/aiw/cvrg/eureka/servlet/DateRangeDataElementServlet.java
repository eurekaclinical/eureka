package edu.emory.cci.aiw.cvrg.eureka.servlet;

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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;

/**
 *
 * @author Andrew Post
 */
public class DateRangeDataElementServlet extends HttpServlet {

	private final ServicesClient servicesClient;

	@Inject
	public DateRangeDataElementServlet (ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		
		String destinationId = req.getParameter("destinationId");
		if (destinationId == null) {
			throw new ServletException("Missing query parameter 'destinationId'");
		}
		Destination destination;
		try {
			destination = this.servicesClient.getDestination(destinationId);
		} catch (ClientException ex) {
			throw new ServletException("Error getting destination '" + destinationId + "'");
		}
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(destination.getDataElementFields());
		resp.setContentLength(json.length());
		PrintWriter out = resp.getWriter();
		out.println(json);
	}
}
