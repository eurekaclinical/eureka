/*
 * #%L
 * Eureka Protempa ETL
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
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Destination;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

@Path("/destination")
@RolesAllowed({"researcher"})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DestinationResource {

	private final EtlProperties etlProperties;
	private final Destinations destFactory;
	

	@Inject
	public DestinationResource(EtlProperties inEtlProperties) {
		this.etlProperties = inEtlProperties;
		this.destFactory = new Destinations(this.etlProperties);
	}

	@GET
	@Path("/{destId}")
	public Destination getDestination(
			@Context HttpServletRequest request,
			@PathParam("destId") String destId) {
		
		return this.destFactory.getDestination(destId);
	}

	@GET
	@Path("/list")
	public List<Destination> getAll() {
		return this.destFactory.getAll();
	}

	/*@POST
	 public void create(Destination source) {
	 }
	
	 @PUT
	 public void update(Destination source) {
	 }*/
}
