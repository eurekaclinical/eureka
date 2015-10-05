/*
 * #%L
 * Eureka Common
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

import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import org.arp.javautil.arrays.Arrays;

/**
 * @author hrathod
 */
public abstract class EurekaClient extends AbstractClient {

	private WebResourceWrapperFactory webResourceWrapperFactory;
	
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
