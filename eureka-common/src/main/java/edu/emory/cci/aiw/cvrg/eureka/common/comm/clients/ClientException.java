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

/**
 * Thrown by a REST client when the response of a REST call signifies an error 
 * condition.
 * 
 * @author hrathod
 */
public final class ClientException extends Exception {

	private final ClientResponse.Status responseStatus;

	/**
	 * Creates the exception using the provided response status and message.
	 *
	 * @param responseStatus the <code>com.sun.jersey.api.client.ClientResponse.Status</code>.
	 *                          Cannot be <code>null</code>.
	 * @param message the exception's message.
	 */
	public ClientException(ClientResponse.Status responseStatus,
			String message) {
		super(message);
		this.responseStatus = responseStatus;
	}

	/**
	 * Returns the response status of the REST call that this exception 
	 * concerns.
	 * 
	 * @return the <code>com.sun.jersey.api.client.ClientResponse.Status</code>.
	 * Guaranteed not <code>null</code>.
	 */
	public ClientResponse.Status getResponseStatus() {
		return responseStatus;
	}
}
