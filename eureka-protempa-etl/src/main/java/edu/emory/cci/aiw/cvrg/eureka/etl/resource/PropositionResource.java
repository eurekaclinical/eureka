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

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.common.filter.SearchFilter;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionDefinitionFinder;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionDefinitionSearcher;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinderException;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidator;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidatorException;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;

/**
 * @author hrathod
 */
@Path("/protected/proposition")
@RolesAllowed({"researcher"})
public class PropositionResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PropositionResource.class);
	private final PropositionValidator propositionValidator;
	private final EtlProperties etlProperties;

	@Inject
	public PropositionResource(PropositionValidator inValidator,
			EtlProperties inEtlProperties) {
		this.propositionValidator = inValidator;
		this.etlProperties = inEtlProperties;
	}

	@POST
	@Path("/validate/{configId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validatePropositions(
			@PathParam("configId") String inConfigId,
			ValidationRequest inRequest) {

		boolean result;
		try {
			propositionValidator.setConfigId(inConfigId);
			propositionValidator.setPropositions(inRequest
					.getPropositions());
			propositionValidator.setTargetProposition(inRequest
					.getTargetProposition());
			result = propositionValidator.validate();
		} catch (PropositionValidatorException e) {
			LOGGER.error(e.getMessage(), e);
			result = false;
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
	@Path("/{configId}/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public PropositionDefinition getProposition(
			@PathParam("configId") String inConfigId,
			@PathParam("key") String inKey) {
		try {
			if (this.etlProperties.getConfigDir() != null) {
				PropositionDefinitionFinder propositionFinder
						= new PropositionDefinitionFinder(
								inConfigId, this.etlProperties);
				PropositionDefinition definition = propositionFinder
						.find(inKey);
				if (definition != null) {
					return definition;
				} else {
					throw new HttpStatusException(
							Response.Status.NOT_FOUND,
							"No proposition with id " + inKey);
				}
			} else {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR,
						"No Protempa configuration directory is "
						+ "specified in application.properties. "
						+ "Proposition finding will not work without it. "
						+ "Please create it and try again.");
			}
		} catch (PropositionFinderException e) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	@GET
	@Path("/{configId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PropositionDefinition> getPropositionsGet(
			@PathParam("configId") String inConfigId,
			@QueryParam("key") List<String> inKeys,
			@QueryParam("withChildren") String withChildren) {
		return getPropositionsCommon(inConfigId, inKeys, withChildren);
	}

	@POST
	@Path("/{configId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PropositionDefinition> getPropositionsPost(
			@PathParam("configId") String inConfigId,
			@FormParam("key") List<String> inKeys,
			@FormParam("withChildren") String withChildren) {
		return getPropositionsCommon(inConfigId, inKeys, withChildren);
	}

	private List<PropositionDefinition> getPropositionsCommon(String inConfigId, List<String> inKeys, String withChildren) throws HttpStatusException {
		try {
			if (this.etlProperties.getConfigDir() != null) {
				List<PropositionDefinition> result = new ArrayList<>();
				PropositionDefinitionFinder propositionFinder
						= new PropositionDefinitionFinder(inConfigId,
								this.etlProperties);
				for (String key : inKeys) {
					PropositionDefinition definition = propositionFinder
							.find(key);
					if (definition != null) {
						result.add(definition);
						if (Boolean.parseBoolean(withChildren)) {
							for (String childId : definition.getChildren()) {
								PropositionDefinition child = propositionFinder
										.find(childId);
								if (child != null) {
									result.add(child);
								} else {
									throw new HttpStatusException(
											Response.Status.NOT_FOUND,
											"No proposition with id " + childId);
								}
							}
						}
					}
				}
				return result;
			} else {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR,
						"No Protempa configuration directory is "
						+ "specified in application.properties. "
						+ "Proposition finding will not work without it. "
						+ "Please create it and try again.");
			}
		} catch (PropositionFinderException e) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	@GET
	@Path("/search/{sourceConfigId}/{searchKey}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> searchPropositionsInTheOntology(
			@PathParam("sourceConfigId") String inSourceConfigId,
			@PathParam("searchKey") String inSearchKey) {
		try {
			LOGGER.debug("Searching for String " + inSearchKey
					+ " in the system element tree");
			if (this.etlProperties.getConfigDir() != null) {
				PropositionDefinitionSearcher propositionSearcher = new PropositionDefinitionSearcher(
						inSourceConfigId, this.etlProperties);
				SearchFilter eurekaFilter = new SearchFilter(
						this.etlProperties.getDefaultSystemPropositions());
				return propositionSearcher.searchPropositions(inSearchKey,
						eurekaFilter);
			} else {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR,
						"No Protempa configuration directory is "
						+ "specified in application.properties. "
						+ "Proposition finding will not work without it. "
						+ "Please create it and try again.");
			}

		} catch (PropositionFinderException e) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR, e);
		}

	}
}
