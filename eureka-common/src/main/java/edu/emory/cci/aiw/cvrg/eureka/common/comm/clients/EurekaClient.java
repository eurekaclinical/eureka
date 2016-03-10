/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.common.comm.clients;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.cassupport.CasWebResourceWrapperFactory;

import java.io.InputStream;
import java.net.URI;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.arp.javautil.arrays.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hrathod
 */
public abstract class EurekaClient extends AbstractClient {

	private WebResourceWrapperFactory webResourceWrapperFactory;
	private static final Logger LOGGER
			= LoggerFactory.getLogger(EurekaClient.class);
        
	protected EurekaClient() {
		this.webResourceWrapperFactory = new CasWebResourceWrapperFactory();
	}

	protected WebResourceWrapper getResourceWrapper() {
		return this.webResourceWrapperFactory.getInstance(getResource());
	}

	protected void doDelete(String path) throws ClientException {
		ClientResponse response = this.getResourceWrapper()
				.rewritten(path, HttpMethod.PUT)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.NO_CONTENT, ClientResponse.Status.ACCEPTED);
	}

	protected void doPut(String path) throws ClientException {
		ClientResponse response = this.getResourceWrapper()
				.rewritten(path, HttpMethod.PUT)
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.CREATED, ClientResponse.Status.OK, ClientResponse.Status.NO_CONTENT);
	}

	protected void doPut(String path, Object o) throws ClientException {
		ClientResponse response = this.getResourceWrapper()
				.rewritten(path, HttpMethod.PUT)
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, o);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.CREATED, ClientResponse.Status.OK, ClientResponse.Status.NO_CONTENT);
	}

	protected String doGet(String path) throws ClientException {
		ClientResponse response = doGetResponse(path);
		
		return response.getEntity(String.class);
	}

	protected String doGet(String path, MultivaluedMap<String, String> queryParams) throws ClientException {
		ClientResponse response = getResourceWrapper().rewritten(path, HttpMethod.GET, queryParams)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.OK);

		return response.getEntity(String.class);
	}

	protected <T> T doGet(String path, Class<T> cls) throws ClientException {
		ClientResponse response = getResourceWrapper().rewritten(path, HttpMethod.GET)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.OK);
		return response.getEntity(cls);
	}

	protected <T> T doGet(String path, Class<T> cls, MultivaluedMap<String, String> queryParams) throws ClientException {
		ClientResponse response = getResourceWrapper().rewritten(path, HttpMethod.GET)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.OK);
		return response.getEntity(cls);
	}
	
	protected ClientResponse doGetResponse(String path) throws ClientException {
		ClientResponse response = getResourceWrapper().rewritten(path, HttpMethod.GET)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.OK);
		return response;
	}

	protected <T> T doGet(String path, GenericType<T> genericType) throws ClientException {
		ClientResponse response = getResourceWrapper().rewritten(path, HttpMethod.GET)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.OK);
		return response.getEntity(genericType);
	}

	protected <T> T doGet(String path, GenericType<T> genericType, MultivaluedMap<String, String> queryParams) throws ClientException {
		ClientResponse response = getResourceWrapper().rewritten(path, HttpMethod.GET, queryParams)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.OK);
		return response.getEntity(genericType);
	}

	protected <T> T doPost(String path, Class<T> cls, MultivaluedMap<String, String> formParams) throws ClientException {
		ClientResponse response = getResourceWrapper().rewritten(path, HttpMethod.POST)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.post(ClientResponse.class, formParams);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.OK);
		return response.getEntity(cls);
	}

	protected <T> T doPost(String path, GenericType<T> genericType, MultivaluedMap<String, String> formParams) throws ClientException {
		ClientResponse response = getResourceWrapper().rewritten(path, HttpMethod.POST)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.post(ClientResponse.class, formParams);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.OK);
		return response.getEntity(genericType);
	}

	protected void doPost(String path) throws ClientException {
		ClientResponse response = getResourceWrapper().rewritten(path, HttpMethod.POST)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.NO_CONTENT);
	}

	protected void doPost(String path, Object o) throws ClientException {
		ClientResponse response = getResourceWrapper().rewritten(path, HttpMethod.POST)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, o);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.NO_CONTENT);
	}
        
	protected void doDelete(String path, Object o) throws ClientException {
		ClientResponse response = this.getResourceWrapper()
				.rewritten(path, HttpMethod.PUT)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class, o);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.NO_CONTENT, ClientResponse.Status.ACCEPTED);
	}

	protected <T> T doPost(String path, Object o, Class<T> cls) throws ClientException {
		ClientResponse response = getResourceWrapper().rewritten(path, HttpMethod.POST)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, o);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.OK);
		return response.getEntity(cls);
	}

	protected URI doPostCreate(String path, Object o) throws ClientException {
		ClientResponse response = getResourceWrapper().rewritten(path, HttpMethod.POST)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, o);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.CREATED);
		return response.getLocation();
	}

	protected void doPostMultipart(String path, String filename, InputStream inputStream) throws ClientException {
		FormDataMultiPart part = new FormDataMultiPart();
		part.bodyPart(
				new FormDataBodyPart(
						FormDataContentDisposition
								.name("file")
								.fileName(filename)
								.build(),
						inputStream, MediaType.APPLICATION_OCTET_STREAM_TYPE));
		ClientResponse response = getResourceWrapper()
				.rewritten(path, HttpMethod.POST)
				.type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.post(ClientResponse.class, part);
		errorIfStatusNotEqualTo(response, ClientResponse.Status.CREATED);
	}

	protected void errorIfStatusEqualTo(ClientResponse response,
										ClientResponse.Status... status) throws ClientException {
		errorIf(response, status, true);
	}

	protected void errorIfStatusNotEqualTo(ClientResponse response,
										   ClientResponse.Status... status) throws ClientException {
		errorIf(response, status, false);
	}

	protected Long extractId(URI uri) {
		String uriStr = uri.toString();
		return Long.valueOf(uriStr.substring(uriStr.lastIndexOf("/") + 1));
	}

	private void errorIf(ClientResponse response,
						 ClientResponse.Status[] status, boolean bool)
			throws ClientException {
		ClientResponse.Status clientResponseStatus =
				response.getClientResponseStatus();
		if (bool) {
			if (Arrays.contains(status, clientResponseStatus)) {
				String message = response.getEntity(String.class);
				throw new ClientException(clientResponseStatus, message);
			}
		} else {
			if (!Arrays.contains(status, clientResponseStatus)) {
				String message = response.getEntity(String.class);
				throw new ClientException(clientResponseStatus, message);
			}
		}
	}
}
