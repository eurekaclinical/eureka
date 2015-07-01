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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfigParams;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.services.config.EtlClient;

/**
 * @author hrathod
 */
@Path("/protected/systemelement")
@RolesAllowed({"researcher"})
@Produces(MediaType.APPLICATION_JSON)
public class SystemElementResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SystemElementResource.class);
	private final SystemPropositionFinder finder;
	private final ServiceProperties serviceProperties;
	private final SourceConfigResource sourceConfigResource;
	private final EtlClient etlClient;
	
	@Inject
	public SystemElementResource(SystemPropositionFinder inFinder,
			SourceConfigResource inSourceConfigResource,
			ServiceProperties inServiceProperties,
			EtlClient inEtlClient) {
		this.finder = inFinder;
		this.serviceProperties = inServiceProperties;
		this.sourceConfigResource = inSourceConfigResource;
		this.etlClient=inEtlClient;
	}

	/**
	 * Gets all of the system elements for a user
	 *
	 * @return a {@link List} of {@link SystemElement}s
	 */
	@GET
	public List<SystemElement> getAll() {
		List<SystemElement> result = new ArrayList<>();
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
					SystemElement element = PropositionUtil.toSystemElement(
							scps.get(0).getId(), definition, true, this.finder);
					result.add(element);
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
	public SystemElement get(@PathParam("key") String inKey,
							@DefaultValue("false") @QueryParam("summary") String summary) {
		return getSystemElementCommon(inKey, summary);
	}

	@POST
	public SystemElement getPropositionsPost(@FormParam("key") String inKey,
											 @DefaultValue("false") @QueryParam("summary") String summary) {
		return getSystemElementCommon(inKey, summary);
	}
	
	@GET
	@Path("/search/{searchKey}")
	public List<String> searchSystemElements(
			@PathParam("searchKey") String inSearchKey) {
		LOGGER.info("Searching system element tree for the searchKey {}",
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
	public List<SystemElement> getSystemElementsBySearchKey(
			@PathParam("searchKey") String inSearchKey) {
		LOGGER.info("Searching system element tree for the searchKey {}",
				inSearchKey);
		List<SystemElement> result = new ArrayList<>();
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

				SystemElement element = PropositionUtil.toSystemElement(
						scps.get(0).getId(), definition, true, this.finder);
				result.add(element);
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
	
	private SystemElement getSystemElementCommon(String inKey, String summary) throws HttpStatusException {
		LOGGER.info("Finding system element {}", inKey);
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
			return PropositionUtil.toSystemElement(scps.get(0).getId(), definition, summary.equals("true") ? true : false,
					this.finder);
		} catch (PropositionFindException ex) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
	}
}
