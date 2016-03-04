/*
 * #%L
 * Eureka Protempa ETL
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
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import java.util.List;
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
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionDefinitionFinder;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinderException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import org.arp.javautil.arrays.Arrays;

/**
 * @author hrathod
 */
@Path("/protected/concepts")
@RolesAllowed({"researcher"})
@Produces(MediaType.APPLICATION_JSON)
public class PropositionResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PropositionResource.class);
	private final EtlProperties etlProperties;

	@Inject
	public PropositionResource(EtlProperties inEtlProperties) {
		this.etlProperties = inEtlProperties;
	}

	@GET
	@Path("/{configId}/{key}")
	@Transactional
	public PropositionDefinition getProposition(
			@PathParam("configId") String inConfigId,
			@PathParam("key") String inKey) {
		if (this.etlProperties.getConfigDir() != null) {
			try (PropositionDefinitionFinder propositionFinder
					= new PropositionDefinitionFinder(
							inConfigId, this.etlProperties)) {
						PropositionDefinition definition = propositionFinder
								.find(inKey);
						if (definition != null) {
							return definition;
						} else {
							throw new HttpStatusException(
									Response.Status.NOT_FOUND,
									"No proposition with id " + inKey);
						}
					} catch (PropositionFinderException e) {
						throw new HttpStatusException(
								Response.Status.INTERNAL_SERVER_ERROR, e);
					}
		} else {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR,
					"No Protempa configuration directory is "
					+ "specified in application.properties. "
					+ "Proposition finding will not work without it. "
					+ "Please create it and try again.");
		}
	}

	@GET
	@Path("/{configId}")
	@Transactional
	public List<PropositionDefinition> getPropositionsGet(
			@PathParam("configId") String inConfigId,
			@QueryParam("key") List<String> inKeys,
			@QueryParam("withChildren") String withChildren) {
		return getPropositionsCommon(inConfigId, inKeys, withChildren);
	}

	@POST
	@Path("/{configId}")
	@Transactional
	public List<PropositionDefinition> getPropositionsPost(
			@PathParam("configId") String inConfigId,
			@FormParam("key") List<String> inKeys,
			@FormParam("withChildren") String withChildren) {
		return getPropositionsCommon(inConfigId, inKeys, withChildren);
	}

	private List<PropositionDefinition> getPropositionsCommon(String inConfigId, List<String> inKeys, String withChildren) throws HttpStatusException {
			if (this.etlProperties.getConfigDir() != null) {
			try (PropositionDefinitionFinder propositionFinder
					= new PropositionDefinitionFinder(inConfigId,
							this.etlProperties)) {
				List<PropositionDefinition> result = propositionFinder.findAll(inKeys);
				if (Boolean.parseBoolean(withChildren)) {
					Set<String> narrower = new HashSet<>();
					for (PropositionDefinition propDef : result) {
						Arrays.addAll(narrower, propDef.getChildren());
					}
					result.addAll(propositionFinder.findAll(narrower));
				}
				return result;
			} catch (PropositionFinderException e) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, e);
			}
		} else {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR,
					"No Protempa configuration directory is "
					+ "specified in application.properties. "
					+ "Proposition finding will not work without it. "
					+ "Please create it and try again.");
		}
	}

	@GET
	@Path("/search/{sourceConfigId}/{searchKey}")
	public List<String> searchPropositionsInTheOntology(
			@PathParam("sourceConfigId") String inSourceConfigId,
			@PathParam("searchKey") String inSearchKey) {
		try {
			LOGGER.debug("Searching for String " + inSearchKey
					+ " in the system element tree");
			if (this.etlProperties.getConfigDir() != null) {
				PropositionDefinitionFinder propositionFinder = new PropositionDefinitionFinder(
						inSourceConfigId, this.etlProperties);
				return propositionFinder.searchPropositions(inSearchKey);
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
	@Path("/propsearch/{sourceConfigId}/{searchKey}")
	@Transactional
	public List<PropositionDefinition> getPropositionsInTheOntologyBySearchKey(
			@PathParam("sourceConfigId") String inSourceConfigId,
			@PathParam("searchKey") String inSearchKey) {
		try {
			LOGGER.debug("Searching for String " + inSearchKey
					+ " in the system element tree");
			if (this.etlProperties.getConfigDir() != null) {
				PropositionDefinitionFinder propositionFinder = new PropositionDefinitionFinder(
						inSourceConfigId, this.etlProperties);
				return propositionFinder.getPropositionsBySearchKey(inSearchKey);
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
