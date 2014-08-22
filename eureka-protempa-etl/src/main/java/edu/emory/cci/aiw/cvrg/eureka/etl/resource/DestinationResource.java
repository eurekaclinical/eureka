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

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DestinationType;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlCohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlI2B2Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.authentication.EtlAuthenticationSupport;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlUserDao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

@Path("/protected/destinations")
@RolesAllowed({"researcher"})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DestinationResource {
	
	private final EtlProperties etlProperties;
	private final EtlUserDao userDao;
	private final DestinationDao destinationDao;
	private final EtlAuthenticationSupport authenticationSupport;
	private final EtlGroupDao groupDao;

	@Inject
	public DestinationResource(EtlProperties inEtlProperties, EtlUserDao inEtlUserDao, DestinationDao inDestinationDao, EtlGroupDao inGroupDao) {
		this.etlProperties = inEtlProperties;
		this.userDao = inEtlUserDao;
		this.destinationDao = inDestinationDao;
		this.authenticationSupport = new EtlAuthenticationSupport(this.userDao);
		this.groupDao = inGroupDao;
	}
	
	@POST
	public void create(@Context HttpServletRequest request, EtlDestination etlDestination) {
		EtlUserEntity user = this.authenticationSupport.getEtlUser(request);
		new Destinations(this.etlProperties, user, this.destinationDao, this.groupDao).create(etlDestination);
	}
	
	@PUT
	public void update(@Context HttpServletRequest request, EtlDestination etlDestination) {
		EtlUserEntity user = this.authenticationSupport.getEtlUser(request);
		new Destinations(this.etlProperties, user, this.destinationDao, this.groupDao).update(etlDestination);
	}
	
	@GET
	@Path("/{destId}")
	public EtlDestination getDestination(
			@Context HttpServletRequest request,
			@PathParam("destId") String destId) {
		EtlUserEntity user = this.authenticationSupport.getEtlUser(request);
		EtlDestination result 
				= new Destinations(this.etlProperties, user, this.destinationDao, this.groupDao).getOne(destId);
		if (result != null) {
			return result;
		} else {
			throw new HttpStatusException(Status.NOT_FOUND);
		}
	}

	@GET
	public List<EtlDestination> getAll(
			@Context HttpServletRequest request,
			@QueryParam("type") DestinationType type) {
		EtlUserEntity user = this.authenticationSupport.getEtlUser(request);
		Destinations destinations = new Destinations(this.etlProperties, user, this.destinationDao, this.groupDao);
		if (type == null) {
			return destinations.getAll();
		}
		switch (type) {
			case I2B2:
				return new ArrayList<EtlDestination>(destinations.getAllI2B2s());
			case COHORT:
				return new ArrayList<EtlDestination>(destinations.getAllCohorts());
			default:
				throw new AssertionError("Unexpected destination type " + type);
		}
	}

}
