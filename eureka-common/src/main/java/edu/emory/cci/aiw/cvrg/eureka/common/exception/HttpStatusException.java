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
