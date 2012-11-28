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
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CategoricalElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionChildrenVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.CategorizationTranslator;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.DataElementTranslatorVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.FrequencyLowLevelAbstractionTranslator;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.FrequencySliceTranslator;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.PropositionTranslatorVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.ResultThresholdsTranslator;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.SequenceTranslator;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.SystemPropositionTranslator;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PropositionUtil;

/**
 * REST Web Service
 * 
 * @author hrathod
 */
@Path("/proposition")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PropositionResource {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PropositionResource.class);
	private final PropositionDao propositionDao;
	private final ServiceProperties applicationProperties;
	private final SystemPropositionFinder systemPropositionFinder;
	private final SequenceTranslator sequenceTranslator;
	private final CategorizationTranslator categorizationTranslator;
	private final SystemPropositionTranslator systemPropositionTranslator;
	private final FrequencySliceTranslator frequencySliceTranslator;
	private final FrequencyLowLevelAbstractionTranslator frequencyLowLevelAbstractionTranslator;
	private final ResultThresholdsTranslator resultThresholdsTranslator;

	/**
	 * Creates a new instance of PropositionResource
	 */
	@Inject
	public PropositionResource(
			PropositionDao inPropositionDao,
			ServiceProperties inApplicationProperties,
			SystemPropositionFinder inFinder,
			SequenceTranslator inSequenceTranslator,
			CategorizationTranslator inCategorizationTranslator,
			SystemPropositionTranslator inSystemPropositionTranslator,
			FrequencySliceTranslator inFrequencySliceTranslator,
			FrequencyLowLevelAbstractionTranslator inFrequencyLowLevelAbstractionTranslator,
			ResultThresholdsTranslator inResultThresholdsTranslator) {
		this.propositionDao = inPropositionDao;
		this.applicationProperties = inApplicationProperties;
		this.systemPropositionFinder = inFinder;
		this.sequenceTranslator = inSequenceTranslator;
		this.categorizationTranslator = inCategorizationTranslator;
		this.systemPropositionTranslator = inSystemPropositionTranslator;
		this.frequencySliceTranslator = inFrequencySliceTranslator;
		this.frequencyLowLevelAbstractionTranslator = inFrequencyLowLevelAbstractionTranslator;
		this.resultThresholdsTranslator = inResultThresholdsTranslator;
	}

	@GET
	@Path("/system/{userId}/list")
	public List<SystemElement> getSystemPropositions(
			@PathParam("userId") Long inUserId) {

		List<SystemElement> elements = new ArrayList<SystemElement>();

		List<String> propNames = this.applicationProperties
				.getDefaultSystemPropositions();
		for (String name : propNames) {
			try {
				elements.add(fetchSystemProposition(inUserId, name));
			} catch (UniformInterfaceException e) {
				if (e.getResponse().getStatus() != Response.Status.NOT_FOUND
						.getStatusCode()) {
					throw new HttpStatusException(
							Response.Status.INTERNAL_SERVER_ERROR, e);
				} else {
					LOGGER.warn("Invalid proposition id specified in system "
							+ "propositions list: " + name);
				}
			}
		}

		return elements;
	}

	@GET
	@Path("/system/{userId}/{propKey}")
	public SystemElement getSystemProposition(
			@PathParam("userId") Long inUserId,
			@PathParam("propKey") String inKey) {
		return fetchSystemProposition(inUserId, inKey);
	}

	@POST
	@Path("/system/{userId}/batch")
	public List<SystemElement> batchSystemPropositions(
			@PathParam("userId") Long inUserId, List<String> inIdList) {
		List<SystemElement> systemElements = new ArrayList<SystemElement>(
				inIdList.size());
		for (String id : inIdList) {
			systemElements.add(this.fetchSystemProposition(inUserId, id));
		}
		return systemElements;
	}

	@GET
	@Path("/user/list/{userId}")
	public List<DataElement> getUserPropositions(
			@PathParam("userId") Long inUserId) {
		List<DataElement> result = new ArrayList<DataElement>();
		for (Proposition p : this.propositionDao.getByUserId(inUserId)) {
			this.propositionDao.refresh(p);
			PropositionTranslatorVisitor visitor = new PropositionTranslatorVisitor(
					this.systemPropositionTranslator, this.sequenceTranslator,
					this.categorizationTranslator,
					this.frequencySliceTranslator,
					this.frequencyLowLevelAbstractionTranslator,
					this.resultThresholdsTranslator);
			p.accept(visitor);
			DataElement dataElement = visitor.getDataElement();
			result.add(dataElement);
		}
		return result;
	}

	@POST
	@Path("/user/validate/{userId}")
	public Response validateProposition(@PathParam("userId") Long inUserId,
			DataElement inDataElement) {
		Response result;
		List<Proposition> propositions = this.propositionDao
				.getByUserId(inUserId);
		Proposition targetProposition = this.propositionDao.getByUserAndKey(
				inUserId, inDataElement.getKey());
		List<PropositionDefinition> propDefs = new ArrayList<PropositionDefinition>();

		for (Proposition proposition : propositions) {
			propDefs.add(PropositionUtil.pack(proposition));
		}

		ValidationRequest validationRequest = new ValidationRequest();
		validationRequest.setUserId(inUserId);
		validationRequest.setPropositions(propDefs);
		validationRequest.setTargetProposition(PropositionUtil
				.pack(targetProposition));

		Client client = CommUtils.getClient();
		WebResource resource = client.resource(this.applicationProperties
				.getEtlPropositionValidationUrl());
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, validationRequest);
		result = Response.status(
				response.getClientResponseStatus().getStatusCode()).build();

		return result;
	}

	@GET
	@Path("/user/get/{propId}")
	public DataElement getUserProposition(
			@PathParam("propId") Long inPropositionId) {
		DataElement dataElement = null;
		Proposition proposition = this.propositionDao.retrieve(inPropositionId);
		this.propositionDao.refresh(proposition);
		if (proposition != null) {
			if (proposition.isInSystem()) {
				dataElement = this.fetchSystemProposition(
						proposition.getUserId(), proposition.getKey());
				dataElement.setCreated(proposition.getCreated());
				dataElement.setLastModified(proposition.getLastModified());
			} else {
				PropositionTranslatorVisitor visitor = new PropositionTranslatorVisitor(
						this.systemPropositionTranslator,
						this.sequenceTranslator, this.categorizationTranslator,
						this.frequencySliceTranslator,
						this.frequencyLowLevelAbstractionTranslator,
						this.resultThresholdsTranslator);
				proposition.accept(visitor);
				dataElement = visitor.getDataElement();
			}
		}
		return dataElement;
	}

	@DELETE
	@Path("/user/delete/{userId}/{propId}")
	public Response deleteUserPropositions(@PathParam("userId") Long inUserId,
			@PathParam("propId") Long inPropositionId) {

		// the response to return;
		Response response = Response.ok().build();

		// fetch the proposition for the given ID
		Proposition target = this.propositionDao.retrieve(inPropositionId);
		if (target == null) {
			// if the target is not found, return a NOT_FOUND response
			response = Response.status(Response.Status.NOT_FOUND).build();
		} else if (!target.getUserId().equals(inUserId)) {
			// if the user ID is not a match with the one passed in,
			// return error
			response = Response.notModified(
					"User ID " + inUserId + " did not" + " match the owner ID "
							+ target.getUserId()).build();
		} else {
			// now get the rest of the propositions for the user
			List<Proposition> others = this.propositionDao.getByUserId(target
					.getUserId());

			// now loop through and make sure that the given proposition is
			// not used in the definition of any of the other propositions.
			boolean error = false;
			for (Proposition proposition : others) {
				if (proposition.getId().equals(target.getId())) {
					continue;
				} else {
					PropositionChildrenVisitor visitor = new PropositionChildrenVisitor();
					proposition.accept(visitor);
					List<Proposition> children = visitor.getChildren();

					for (Proposition p : children) {
						if (p.getId().equals(target.getId())) {
							response = Response
									.status(Response.Status.PRECONDITION_FAILED)
									.entity(p.getAbbrevDisplayName() + " "
											+ "contains " + "a "
											+ "reference to "
											+ target.getAbbrevDisplayName())
									.build();
							error = true;
							break;
						}
					}
				}
				if (error) {
					break;
				}
			}
			if (!error) {
				this.propositionDao.remove(target);
			}
		}
		return response;
	}

	@PUT
	@Path("/user/update")
	public void updateProposition(DataElement inDataElement) {
		if (inDataElement.getUserId() != null && inDataElement.getId() != null) {
			DataElementTranslatorVisitor visitor = new DataElementTranslatorVisitor(
					this.systemPropositionTranslator, this.sequenceTranslator,
					this.categorizationTranslator,
					this.frequencySliceTranslator,
					this.frequencyLowLevelAbstractionTranslator,
					this.resultThresholdsTranslator);
			inDataElement.accept(visitor);
			Proposition proposition = visitor.getProposition();
			proposition.setLastModified(new Date());
			this.propositionDao.update(proposition);
		} else {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"Both user ID and " + "proposition ID must be provided.");
		}
	}

	@POST
	@Path("/user/create/sequence")
	public void insertProposition(Sequence inSequence) {
		if (inSequence.getUserId() != null) {
			HighLevelAbstraction abstraction = this.sequenceTranslator
					.translateFromElement(inSequence);
			Date now = new Date();
			abstraction.setCreated(now);
			abstraction.setLastModified(now);
			this.propositionDao.create(abstraction);
		} else {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"User ID must be provided.");
		}
	}

	@PUT
	@Path("/user/update/sequence")
	public void updateProposition(Sequence inSequence) {
		if (inSequence.getId() != null && inSequence.getUserId() != null) {
			HighLevelAbstraction abstraction = this.sequenceTranslator
					.translateFromElement(inSequence);
			Date now = new Date();
			abstraction.setLastModified(now);
			this.propositionDao.update(abstraction);
		} else {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"User ID and proposition ID must be provided.");
		}
	}

	@POST
	@Path("/user/create/categorization")
	public void insertProposition(CategoricalElement inElement) {
		if (inElement.getUserId() != null) {
			Categorization categorization = this.categorizationTranslator
					.translateFromElement(inElement);
			Date now = new Date();
			categorization.setCreated(now);
			categorization.setLastModified(now);
			this.propositionDao.create(categorization);
		} else {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"User ID must be " + "provided.");
		}
	}

	@PUT
	@Path("/user/update/categorization")
	public void updateProposition(CategoricalElement inElement) {
		if (inElement.getId() != null && inElement.getUserId() != null) {
			Categorization categorization = this.categorizationTranslator
					.translateFromElement(inElement);
			Date now = new Date();
			categorization.setLastModified(now);
			this.propositionDao.update(categorization);
		} else {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"Both user ID and " + "proposition ID must be provided.");
		}
	}

	private SystemElement fetchSystemProposition(Long inUserId, String inKey) {
		return PropositionUtil.wrap(
				systemPropositionFinder.find(inUserId, inKey), false, inUserId,
				this.systemPropositionFinder);
	}
}
