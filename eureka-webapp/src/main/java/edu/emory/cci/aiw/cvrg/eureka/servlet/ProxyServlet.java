package edu.emory.cci.aiw.cvrg.eureka.servlet;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
import com.google.inject.Inject;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/**
 * @author Sanjay Agravat, Miao Ai
 */
public class ProxyServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProxyServlet.class);
	
	private final ServicesClient servicesClient;
	
	protected URI targetProtectedUri;
	protected URI targetUnprotectedUri;

	@Inject
	public ProxyServlet(ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	public void init() throws ServletException {
		String targetPUriStr = "api/protected";
		String targetUpUriStr = "api";

		try {
			this.targetProtectedUri = new URI(targetPUriStr);
			this.targetUnprotectedUri = new URI(targetUpUriStr);
		} catch (Exception e) {
			throw new ServletException("Trying to process targetProtectedUri/targetUnprotectedUri init parameter: " + e, e);
		}
	}

	@Override
	protected void doPut(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
		LOGGER.debug("ProxyServlet - PUT");

		String content = extractContent(servletRequest);
		String uri = doRoute(servletRequest);

		try {
			servicesClient.proxyPut(uri, content);
		} catch (ClientException e) {
			servletResponse.setStatus(e.getResponseStatus().getStatusCode());
			servletResponse.getOutputStream().print(e.getMessage());
		}
	}

	@Override
	protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
			throws IOException {
		LOGGER.debug("ProxyServlet - POST");

		String content = extractContent(servletRequest);
		String uri = doRoute(servletRequest);

		try {
			URI created = servicesClient.proxyPost(uri, content);
			if (created != null) {
				servletResponse.setStatus(HttpServletResponse.SC_CREATED);
				servletResponse.setHeader("Location", created.toString());
			}
		} catch (ClientException e) {
			servletResponse.setStatus(e.getResponseStatus().getStatusCode());
			servletResponse.getOutputStream().print(e.getMessage());
		}

	}

	@Override
	protected void doDelete(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
			throws IOException {
		LOGGER.debug("ProxyServlet - DELETE");

		String uri = doRoute(servletRequest);

		try {
			servicesClient.proxyDelete(uri);
		} catch (ClientException e) {
			servletResponse.setStatus(e.getResponseStatus().getStatusCode());
			servletResponse.getOutputStream().print(e.getMessage());
		}

	}

	@Override
	protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
			throws IOException {
		LOGGER.debug("ProxyServlet - GET");

		String uri = doRoute(servletRequest);

		try {
			Map<String, String[]> parameterMap = servletRequest.getParameterMap();
			MultivaluedMap multivaluedMap = toMultivaluedMap(parameterMap);
			String response = servicesClient.proxyGet(uri, multivaluedMap);
			servletResponse.getWriter().write(response);
		} catch (ClientException e) {
			servletResponse.setStatus(e.getResponseStatus().getStatusCode());
			servletResponse.getOutputStream().print(e.getMessage());
		}
	}
	
	private String doRoute(HttpServletRequest servletRequest) {
		String pathInfo = servletRequest.getPathInfo();
		UriBuilder uriBuilder;
                
		if(pathInfo.contains("appproperties")){
			uriBuilder = UriBuilder.fromUri(this.targetUnprotectedUri);
		}
		else{
			uriBuilder = UriBuilder.fromUri(this.targetProtectedUri);
		}

		uriBuilder = uriBuilder.path(pathInfo);
		String uri = uriBuilder.build().toString();

		LOGGER.debug("uri: {}", uri);
		return uri;
	}
	
	private static MultivaluedMap toMultivaluedMap(Map<String, String[]> inQueryParameters) {
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		for (Map.Entry<String, String[]> parameter : inQueryParameters.entrySet()) {
			String[] values = parameter.getValue();
			for (String value : values) {
				queryParams.add(parameter.getKey(), value);
			}
		}
		return queryParams;
	}

	private static String extractContent(HttpServletRequest servletRequest) throws IOException {
		InputStream inputStream = servletRequest.getInputStream();
		String charEncoding = servletRequest.getCharacterEncoding();
		String content = IOUtils.toString(inputStream, charEncoding);
		LOGGER.debug("json: {}", content);
		return content;
	}
	
}
