/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.config.EtlClient;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import java.util.List;

/**
 * @author Andrew Post
 */
@Path("/protected/destination")
@RolesAllowed({"researcher"})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DestinationResource {

	private final EtlClient etlClient;

	@Inject
	public DestinationResource(EtlClient inEtlClient) {
		this.etlClient = inEtlClient;
	}

	/**
	 * Gets all of the destincations for a user
	 *
	 * @return a {@link List} of {@link Destination}s
	 */
	@GET
	@Path("/list")
	public List<Destination> getAll() {
		try {
			return this.etlClient.getDestinations();
		} catch (ClientException ex) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
		}
	}

	@GET
	@Path("/{id}")
	public Destination get(@PathParam("id") String inId) {
		try {
			return this.etlClient.getDestination(inId);
		} catch (ClientException ex) {
			if (ex.getResponseStatus() == ClientResponse.Status.NOT_FOUND) {
				throw new HttpStatusException(Status.NOT_FOUND);
			} else {
				throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
			}
		}
	}

}
