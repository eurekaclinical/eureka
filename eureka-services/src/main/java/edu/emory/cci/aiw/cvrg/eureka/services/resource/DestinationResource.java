/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse;
import org.eurekaclinical.eureka.client.comm.Destination;
import org.eurekaclinical.eureka.client.comm.DestinationType;
import org.eurekaclinical.protempa.client.EurekaClinicalProtempaClient;
import org.eurekaclinical.protempa.client.comm.EtlDestination;

import edu.emory.cci.aiw.cvrg.eureka.services.conversion.ConversionSupport;
import edu.emory.cci.aiw.cvrg.eureka.services.conversion.DestinationToEtlDestinationVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.conversion.EtlDestinationToDestinationVisitor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.eurekaclinical.common.comm.clients.ClientException;
import org.eurekaclinical.standardapis.exception.HttpStatusException;

/**
 * @author Andrew Post
 */
@Path("/protected/destinations")
@RolesAllowed({"researcher"})
@Consumes(MediaType.APPLICATION_JSON)
public class DestinationResource {

	private final EurekaClinicalProtempaClient protempaClient;
	private final ConversionSupport conversionSupport;

	@Inject
	public DestinationResource(EurekaClinicalProtempaClient inEtlClient, ConversionSupport inConversionSupport) {
		this.protempaClient = inEtlClient;
		this.conversionSupport = inConversionSupport;
	}

	@POST
	public Response create(@Context HttpServletRequest request, Destination inDestination) {
		DestinationToEtlDestinationVisitor v
				= new DestinationToEtlDestinationVisitor(this.conversionSupport);
		inDestination.accept(v);
		EtlDestination etlDest = v.getEtlDestination();
		Long destId;
		try {
			destId = this.protempaClient.createDestination(etlDest);
		} catch (ClientException ex) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
		}

		return Response.created(URI.create("/" + destId)).build();
	}

	@PUT
	public void update(@Context HttpServletRequest request, Destination inDestination) {
		DestinationToEtlDestinationVisitor v
				= new DestinationToEtlDestinationVisitor(this.conversionSupport);
		inDestination.accept(v);
		EtlDestination etlDest = v.getEtlDestination();
		try {
			this.protempaClient.updateDestination(etlDest);
		} catch (ClientException ex) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
		}
	}

	/**
	 * Gets all of the destinations for a user
	 *
	 * @param request
	 * @param type
	 * @return a {@link List} of {@link Destination}s
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Destination> getAll(@Context HttpServletRequest request, @QueryParam("type") DestinationType type) {
		try {
			List<? extends EtlDestination> destinations;
			if (type == null) {
				destinations = this.protempaClient.getDestinations();
			} else {
				switch (type) {
					case I2B2:
						destinations = this.protempaClient.getI2B2Destinations();
						break;
					case COHORT:
						destinations = this.protempaClient.getCohortDestinations();
						break;
					case PATIENT_SET_EXTRACTOR:
						destinations = this.protempaClient.getPatientSetExtractorDestinations();
						break;
					case PATIENT_SET_SENDER:
						destinations = this.protempaClient.getPatientSetSenderDestinations();
						break;
					case TABULAR_FILE:
						destinations = this.protempaClient.getTabularFileDestinations();
						break;
					default:
						throw new AssertionError("Unexpected type " + type);
				}
			}
			List<Destination> result = new ArrayList<>(destinations.size());
			EtlDestinationToDestinationVisitor v
					= new EtlDestinationToDestinationVisitor(this.conversionSupport);
			for (EtlDestination etlDest : destinations) {
				etlDest.accept(v);
				result.add(v.getDestination());
			}
			return result;
		} catch (ClientException ex) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Destination get(@Context HttpServletRequest request, @PathParam("id") String inId) {
		EtlDestinationToDestinationVisitor v
				= new EtlDestinationToDestinationVisitor(this.conversionSupport);
		try {
			this.protempaClient.getDestination(inId).accept(v);
		} catch (ClientException ex) {
			if (ex.getResponseStatus() == ClientResponse.Status.NOT_FOUND) {
				throw new HttpStatusException(Status.NOT_FOUND);
			} else {
				throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
			}
		}
		return v.getDestination();
	}

	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") String inId) {
		try {
			this.protempaClient.deleteDestination(inId);
		} catch (ClientException ex) {
			if (ex.getResponseStatus() == ClientResponse.Status.NOT_FOUND) {
				throw new HttpStatusException(Status.NOT_FOUND);
			} else {
				throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
			}
		}
	}

}
