/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.common.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Andrew Post
 */
public class HttpStatusException extends WebApplicationException {

	public HttpStatusException(Status status) {
		super(Response.status(status).build());
	}

	public HttpStatusException(Status status, String message) {
		super(Response.status(status).entity(message).type("text/plain").build());
	}

	public HttpStatusException(Status status, Throwable cause) {
		super(cause,
			Response.status(status).type("text/plain").build());
	}

	public HttpStatusException(Status status, String message, Throwable cause) {
		super(cause,
			Response.status(status).entity(message).type("text/plain").build());
	}



}
