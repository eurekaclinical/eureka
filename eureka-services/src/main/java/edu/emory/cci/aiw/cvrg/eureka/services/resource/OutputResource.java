package edu.emory.cci.aiw.cvrg.eureka.services.resource;

/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.services.config.EtlClient;
import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.io.IOUtils;

/**
 * Operations related to a data file (doPost, status, etc)
 *
 * @author Andrew Post
 *
 */
@Path("/protected")
@RolesAllowed({"researcher"})
public class OutputResource {
	private final EtlClient etlClient;

	/**
	 * Create an object with the give data access object.
	 *
	 */
	@Inject
	public OutputResource(EtlClient inEtlClient) {
		this.etlClient = inEtlClient;
	}

	@GET
	@Path("/output/{destinationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response doGet(@PathParam("destinationId") String inId) throws ClientException {
		final ClientResponse response = this.etlClient.getOutput(inId);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException {
				try (InputStream inputStream = response.getEntityInputStream()) {
					IOUtils.copy(inputStream, os);
				}
			}
		};
		MultivaluedMap<String, String> headers = response.getHeaders();
		String outputType = headers.getFirst(HttpHeaders.CONTENT_TYPE);
		String contentDisposition = headers.getFirst("content-disposition");
		return Response.ok(stream, outputType).header("content-disposition", contentDisposition).build();
	}

	@DELETE
	@Path("/output/{destinationId}")
	public Response doDelete(@PathParam("destinationId") String inId) throws ClientException {
		this.etlClient.deleteOutput(inId);
		return Response.noContent().build();
	}

}
