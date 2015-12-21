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
import edu.emory.cci.aiw.cvrg.eureka.webapp.authentication.WebappAuthenticationSupport;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Sanjay Agravat on 4/21/15.
 */
public class ProxyServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProxyServlet.class);
	private final ServicesClient servicesClient;
	private final WebappAuthenticationSupport authenticationSupport;

	protected String targetUri;
	protected URI targetUriObj;//new URI(targetUri)
	/**
	 * The parameter name for the target (destination) URI to proxy to.
	 */
	protected static final String P_TARGET_URI = "targetUri";

	@Inject
	public ProxyServlet(ServicesClient inClient) {
		this.servicesClient = inClient;
		this.authenticationSupport = new WebappAuthenticationSupport(this.servicesClient);
	}


	/**
	 * Reads a configuration parameter. By default it reads servlet init parameters but
	 * it can be overridden.
	 * @param key the key.
	 * @return the configuration parameter.
	 */
	protected String getConfigParam(String key) {
		return getServletConfig().getInitParameter(key);
	}

	@Override
	public void init() throws ServletException {

		targetUri = getConfigParam(P_TARGET_URI);
		if (targetUri == null)
			throw new ServletException(P_TARGET_URI + " is required.");
		//test it's valid
		try {
			targetUriObj = new URI(targetUri);
		} catch (Exception e) {
			throw new ServletException("Trying to process targetUri init parameter: " + e, e);
		}

	}


	@Override
	protected void doPut(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
		LOGGER.debug("ProxyServlet - PUT");

		StringBuilder stringBuilder = new StringBuilder(1000);
		Scanner scanner = new Scanner(servletRequest.getInputStream());
		while (scanner.hasNextLine()) {
			stringBuilder.append(scanner.nextLine());
		}
		LOGGER.debug("json: {}", stringBuilder.toString());
		StringBuilder uri = new StringBuilder(500);
		uri.append(getTargetUri());
		// Handle the path given to the servlet
		if (servletRequest.getPathInfo() != null) {//ex: /my/path.html
			uri.append(servletRequest.getPathInfo());
		}
		LOGGER.debug("uri: {}", uri.toString());
		try {
			servicesClient.proxyPut(uri.toString(), stringBuilder.toString());
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
			throws ServletException, IOException {

		LOGGER.debug("ProxyServlet - POST");

		StringBuilder stringBuilder = new StringBuilder(1000);
		Scanner scanner = new Scanner(servletRequest.getInputStream());
		while (scanner.hasNextLine()) {
			stringBuilder.append(scanner.nextLine());
		}
		LOGGER.debug("json: {}", stringBuilder.toString());
		StringBuilder uri = new StringBuilder(500);
		uri.append(getTargetUri());
		// Handle the path given to the servlet
		if (servletRequest.getPathInfo() != null) {//ex: /my/path.html
			uri.append(servletRequest.getPathInfo());
		}
		LOGGER.debug("uri: {}", uri.toString());
		try {
			servicesClient.proxyPost(uri.toString(), stringBuilder.toString());
		} catch (ClientException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doDelete(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
			throws ServletException, IOException {

		LOGGER.debug("ProxyServlet - DELETE");

		StringBuilder uri = new StringBuilder(500);
		uri.append(getTargetUri());
		// Handle the path given to the servlet
		if (servletRequest.getPathInfo() != null) {//ex: /my/path.html
			uri.append(servletRequest.getPathInfo());
		}
		LOGGER.debug("uri: {}", uri.toString());


		try {
			servicesClient.proxyDelete(uri.toString());
		} catch (ClientException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
			throws ServletException, IOException {

		LOGGER.debug("ProxyServlet - GET");

		StringBuilder uri = new StringBuilder(500);
		uri.append(getTargetUri());
		// Handle the path given to the servlet
		if (servletRequest.getPathInfo() != null) {//ex: /my/path.html
			uri.append(servletRequest.getPathInfo());
		}
		LOGGER.debug("uri: " + uri.toString());
		try {
			String response = servicesClient.proxyGet(uri.toString(), getQueryParamsFromURI(servletRequest.getParameterMap()));
			servletResponse.getWriter().write(response);
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}


	/**
	 * The target URI as configured. Not null.
	 */
	public String getTargetUri() {
		return targetUri;
	}

	public MultivaluedMap getQueryParamsFromURI(Map inQueryParameters) {
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		;
		Iterator queryParamIterator = inQueryParameters.entrySet().iterator();
		while (queryParamIterator.hasNext()) {
			Map.Entry parameter = (Map.Entry) queryParamIterator.next();
			parameter.getKey();
			String[] values = (String[]) parameter.getValue();
			queryParams.add((String) parameter.getKey(), values[0]);
		}
		return queryParams;

	}
}
