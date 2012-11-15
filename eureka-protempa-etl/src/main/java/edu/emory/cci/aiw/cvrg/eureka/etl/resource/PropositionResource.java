/*
 * #%L
 * Eureka Protempa ETL
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
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinderException;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidator;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidatorException;

/**
 * @author hrathod
 */
@Path("/proposition")
public class PropositionResource {

	private static final Logger LOGGER = LoggerFactory
	        .getLogger(PropositionResource.class);
	private final PropositionValidator propositionValidator;
	private final ConfDao confDao;
	private final EtlProperties etlProperties;

	@Inject
	public PropositionResource(PropositionValidator inValidator,
	        ConfDao inConfDao, EtlProperties inEtlProperties) {
		this.propositionValidator = inValidator;
		this.confDao = inConfDao;
		this.etlProperties = inEtlProperties;
	}

	@POST
	@Path("/validate")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validatePropositions(ValidationRequest inRequest) {

		boolean result;
		Configuration configuration = this.confDao.getByUserId(inRequest
		        .getUserId());
		if (configuration != null) {
			try {
				propositionValidator.setConfiguration(configuration);
				propositionValidator.setPropositions(inRequest
				        .getPropositions());
				propositionValidator.setTargetProposition(inRequest
				        .getTargetProposition());
				result = propositionValidator.validate();
			} catch (PropositionValidatorException e) {
				LOGGER.error(e.getMessage(), e);
				result = false;
			}
		} else {
			result = false;
			LOGGER.error("No Protempa configuration found for user "
			        + inRequest.getUserId());
		}

		Response response;
		if (result) {
			response = Response.ok().build();
		} else {
			response = Response.status(Response.Status.NOT_ACCEPTABLE)
			        .entity(propositionValidator.getMessages()).build();
		}
		return response;
	}

	@GET
	@Path("/{userId}/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public PropositionDefinition getProposition(
	        @PathParam("userId") Long inUserId, @PathParam("key") String inKey) {
		try {
			Configuration configuration = this.confDao.getByUserId(inUserId);
			if (configuration != null) {
				PropositionFinder propositionFinder = new PropositionFinder(
				        configuration, this.etlProperties.getConfigDir());
				PropositionDefinition definition = propositionFinder
				        .find(inKey);
				if (definition != null) {
					return definition;
				} else {
					throw new HttpStatusException(Response.Status.NOT_FOUND,
					        "No proposition with id " + inKey);
				}
			} else {
				throw new HttpStatusException(Response.Status.NOT_FOUND,
				        "No Protempa configuration found for user " + inUserId);
			}
		} catch (PropositionFinderException e) {
			throw new HttpStatusException(
			        Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}
}
