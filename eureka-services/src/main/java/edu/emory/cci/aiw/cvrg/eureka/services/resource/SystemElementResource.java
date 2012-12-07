/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PropositionUtil;
import java.util.logging.Level;

/**
 * @author hrathod
 */
@Path("/systemelement")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SystemElementResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemElementResource.class);
	private final SystemPropositionFinder finder;
	private final ServiceProperties serviceProperties;

	@Inject
	public SystemElementResource(SystemPropositionFinder inFinder,
			ServiceProperties inServiceProperties) {
		this.finder = inFinder;
		this.serviceProperties = inServiceProperties;
	}

	@GET
	@Path("/{userId}")
	public List<SystemElement> getAll(@PathParam("userId") Long inUserId) {
		List<SystemElement> result = new ArrayList<SystemElement>();
		for (String key : this.serviceProperties
				.getDefaultSystemPropositions()) {
			try {
				PropositionDefinition definition = this.finder.find(
						inUserId, key);
				if (definition == null) {
					LOGGER.warn(
							"Invalid proposition key specified in system "
							+ "propositions list " + key);
				} else {
					SystemElement element = PropositionUtil.wrap(
							definition, false, inUserId, this.finder);
					result.add(element);
				}
			} catch (PropositionFindException e) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, e);
			}
		}
		return result;
	}

	@GET
	@Path("/{userId}/{key}")
	public SystemElement get(@PathParam("userId") Long inUserId,
			@PathParam("key") String inKey) {
		PropositionDefinition definition;
		try {
			definition = this.finder.find(inUserId, inKey);
			if (definition == null) {
				throw new HttpStatusException(Response.Status.NOT_FOUND);
			}
			return PropositionUtil.wrap(definition, false, inUserId, 
					this.finder);
		} catch (PropositionFindException ex) {
			throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
	}
}
