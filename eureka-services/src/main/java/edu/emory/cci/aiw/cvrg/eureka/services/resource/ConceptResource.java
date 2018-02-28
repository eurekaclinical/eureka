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

import org.eurekaclinical.eureka.client.comm.SourceConfigParams;
import org.eurekaclinical.eureka.client.comm.SystemPhenotype;
import org.eurekaclinical.protempa.client.EurekaClinicalProtempaClient;

import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PropositionUtil;
import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import org.eurekaclinical.common.comm.clients.ClientException;
import org.eurekaclinical.standardapis.exception.HttpStatusException;

/**
 * @author hrathod
 */
@Path("/protected/concepts")
@RolesAllowed({"researcher"})
@Produces(MediaType.APPLICATION_JSON)
public class ConceptResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConceptResource.class);
	private final SystemPropositionFinder finder;
	private final ServiceProperties serviceProperties;
	private final SourceConfigResource sourceConfigResource;
	private final EurekaClinicalProtempaClient etlClient;
	
	@Inject
	public ConceptResource(SystemPropositionFinder inFinder,
			SourceConfigResource inSourceConfigResource,
			ServiceProperties inServiceProperties,
			EurekaClinicalProtempaClient inEtlClient) {
		this.finder = inFinder;
		this.serviceProperties = inServiceProperties;
		this.sourceConfigResource = inSourceConfigResource;
		this.etlClient=inEtlClient;
	}

	/**
	 * Gets all of the system phenotypes for a user
	 *
	 * @return a {@link List} of {@link SystemPhenotype}s
	 */
	@GET
	public List<SystemPhenotype> getAll() {
		List<SystemPhenotype> result = new ArrayList<>();
		/*
		 * Hack to get an ontology source that assumes all Protempa configurations
		 * for a user point to the same knowledge source backends. This will go away.
		 */
		List<SourceConfigParams> scps = this.sourceConfigResource.getParamsList();
		if (scps.isEmpty()) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, "No source configs");
		}
		try {
			List<PropositionDefinition> definitions = this.finder.findAll(
					scps.get(0).getId(),
					this.serviceProperties.getDefaultSystemPropositions(),
					Boolean.FALSE);
			if (definitions.isEmpty()) {
				LOGGER.warn("No proposition definitions retrieved");
			} else {
				for (PropositionDefinition definition : definitions) {
					SystemPhenotype phenotype = PropositionUtil.toSystemPhenotype(
							scps.get(0).getId(), definition, true, this.finder);
					result.add(phenotype);
				}
			}
		} catch (PropositionFindException e) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		return result;
	}

	@GET
	@Path("/{key}")
	public SystemPhenotype get(@PathParam("key") String inKey, @DefaultValue("false") @QueryParam("summarize") boolean inSummarize) {
		return getSystemPhenotypeCommon(inKey, inSummarize);
	}
	
	@POST
	public List<SystemPhenotype> getPropositionsPost(@FormParam("key") List<String> inKeys, @DefaultValue("false") @FormParam("summarize") String inSummarize) {
		return getSystemPhenotypesCommon(inKeys, Boolean.parseBoolean(inSummarize));

	}
	
	@GET
	@Path("/search/{searchKey}")
	public List<String> searchSystemPhenotypes(
			@PathParam("searchKey") String inSearchKey) {
		LOGGER.info("Searching system phenotype tree for the searchKey {}",
				inSearchKey);

		List<SourceConfigParams> scps = this.sourceConfigResource
				.getParamsList();
		if (scps.isEmpty()) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR,
					"No source configs");
		}

		try {
			List<String> searchResult = etlClient.getPropositionSearchResults(
					scps.get(0).getId(), inSearchKey);

			LOGGER.info("returning search results list of size"
					+ searchResult.size());
			return searchResult;
		} catch (ClientException e) {
			LOGGER.error(e.getMessage(), e);
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, e);
		}

	}

	@GET
	@Path("/propsearch/{searchKey}")
	public List<SystemPhenotype> getSystemPhenotypesBySearchKey(
			@PathParam("searchKey") String inSearchKey) {
		LOGGER.info("Searching system phenotype tree for the searchKey {}",
				inSearchKey);
		List<SystemPhenotype> result = new ArrayList<>();
		List<SourceConfigParams> scps = this.sourceConfigResource
				.getParamsList();
		if (scps.isEmpty()) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR,
					"No source configs");
		}

		try {
			List<PropositionDefinition> definitions = etlClient.getPropositionSearchResultsBySearchKey(
					scps.get(0).getId(), inSearchKey);

			for (PropositionDefinition definition : definitions) {

				SystemPhenotype phenotype = PropositionUtil.toSystemPhenotype(
						scps.get(0).getId(), definition, true, this.finder);
				result.add(phenotype);
			}
			LOGGER.info("returning search results list of size"
					+ definitions.size());
			return result;
		} catch (ClientException e) {
			LOGGER.error(e.getMessage(), e);
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, e);
		} catch (PropositionFindException e) {
			LOGGER.error(e.getMessage(), e);
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR, e);
		}

	}
	
	private List<SystemPhenotype> getSystemPhenotypesCommon(List<String> inKeys, boolean inSummarize) throws HttpStatusException {
		LOGGER.info("Finding system phenotype {}", inKeys);
		/*
		* Hack to get an ontology source that assumes all Protempa configurations
		* for a user point to the same knowledge source backends. This will go away.
		*/
		List<SourceConfigParams> scps = this.sourceConfigResource.getParamsList();
		if (scps.isEmpty()) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, "No source configs");
		}
		String sourceConfigId = scps.get(0).getId();
		List<PropositionDefinition> definition;
		try {
			definition = this.finder.findAll(sourceConfigId, inKeys, Boolean.FALSE);
			List<SystemPhenotype> result = new ArrayList<>(definition.size());
			for (PropositionDefinition propDef : definition) {
				result.add(PropositionUtil.toSystemPhenotype(sourceConfigId, propDef, inSummarize,
					this.finder));
			}
			return result;
		} catch (PropositionFindException ex) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
	}

	private SystemPhenotype getSystemPhenotypeCommon(String inKey, boolean inSummarize) throws HttpStatusException {
		LOGGER.info("Finding system phenotype {}", inKey);
		/*
		* Hack to get an ontology source that assumes all Protempa configurations
		* for a user point to the same knowledge source backends. This will go away.
		*/
		List<SourceConfigParams> scps = this.sourceConfigResource.getParamsList();
		if (scps.isEmpty()) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, "No source configs");
		}
		PropositionDefinition definition;
		try {
			definition = this.finder.find(scps.get(0).getId(), inKey);
			if (definition == null) {
				throw new HttpStatusException(Response.Status.NOT_FOUND);
			}
			return PropositionUtil.toSystemPhenotype(scps.get(0).getId(), definition, inSummarize,
					this.finder);
		} catch (PropositionFindException ex) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
	}
}
