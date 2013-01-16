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

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Category;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.EtlClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionChildrenVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.conversion.PropositionDefinitionConverterVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.CategorizationTranslator;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.DataElementTranslatorVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.FrequencyTranslator;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.PropositionTranslatorVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.SequenceTranslator;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.SystemPropositionTranslator;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.ValueThresholdsTranslator;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PropositionUtil;
import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private final FrequencyTranslator frequencyTranslator;
	private final ValueThresholdsTranslator valueThresholdsTranslator;

	private final PropositionDefinitionConverterVisitor converterVisitor;

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
			FrequencyTranslator inFrequencyTranslator,
			ValueThresholdsTranslator inValueThresholdsTranslator,
			PropositionDefinitionConverterVisitor inVisitor) {
		this.propositionDao = inPropositionDao;
		this.applicationProperties = inApplicationProperties;
		this.systemPropositionFinder = inFinder;
		this.sequenceTranslator = inSequenceTranslator;
		this.categorizationTranslator = inCategorizationTranslator;
		this.systemPropositionTranslator = inSystemPropositionTranslator;
		this.frequencyTranslator = inFrequencyTranslator;
		this.valueThresholdsTranslator = inValueThresholdsTranslator;
		this.converterVisitor = inVisitor;
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
				SystemElement systemElement = fetchSystemProposition(inUserId, name);
				if (systemElement == null) {
					LOGGER.warn("Invalid proposition id specified in system "
							+ "propositions list: " + name);
				} else {
					elements.add(systemElement);
				}
			} catch (PropositionFindException e) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR,
						"Error getting proposition " + name
						+ " (message from Protempa ETL backend): "
						+ e.getMessage(),
						e);

			}
		}

		return elements;
	}

	@GET
	@Path("/system/{userId}/{propKey}")
	public SystemElement getSystemProposition(
			@PathParam("userId") Long inUserId,
			@PathParam("propKey") String inKey) {
		try {
			SystemElement systemElement = fetchSystemProposition(inUserId, inKey);
			if (systemElement == null) {
				LOGGER.warn("Invalid proposition id specified in system "
						+ "propositions list: " + inKey);
				throw new HttpStatusException(
						Response.Status.NOT_FOUND,
						"Invalid proposition id specified in system "
						+ "propositions list: " + inKey);
			} else {
				return systemElement;
			}
		} catch (PropositionFindException e) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR,
					"Error getting proposition " + inKey
					+ " (message from Protempa ETL backend): "
					+ e.getMessage(),
					e);
		}
	}

	@POST
	@Path("/system/{userId}/batch")
	public List<SystemElement> batchSystemPropositions(
			@PathParam("userId") Long inUserId, List<String> inIdList) {
		List<SystemElement> systemElements = new ArrayList<SystemElement>(
				inIdList.size());
		for (String id : inIdList) {
			try {
				systemElements.add(this.fetchSystemProposition(inUserId, id));
			} catch (PropositionFindException ex) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, ex);
			}
		}
		return systemElements;
	}

	@GET
	@Path("/user/list/{userId}")
	public List<DataElement> getUserPropositions(
			@PathParam("userId") Long inUserId) {
		List<DataElement> result = new ArrayList<DataElement>();
		for (DataElementEntity p : this.propositionDao.getByUserId(inUserId)) {
			this.propositionDao.refresh(p);
			if (!p.isInSystem()) {
				PropositionTranslatorVisitor visitor =
						getPropositionTranslatorVisitor();
				p.accept(visitor);
				DataElement dataElement = visitor.getDataElement();
				result.add(dataElement);
			}
		}
		return result;
	}

	@POST
	@Path("/user/validate/{userId}")
	public void validateProposition(@PathParam("userId") Long inUserId,
			DataElement inDataElement) {
		List<DataElementEntity> propositions = this.propositionDao
				.getByUserId(inUserId);
		DataElementEntity targetProposition = this.propositionDao.getByUserAndKey(
				inUserId, inDataElement.getKey());
		List<PropositionDefinition> propDefs = new ArrayList<PropositionDefinition>();

		this.converterVisitor.setUserId(inUserId);
		for (DataElementEntity proposition : propositions) {
			proposition.accept(this.converterVisitor);
			propDefs.addAll(this.converterVisitor.getPropositionDefinitions());
		}

		ValidationRequest validationRequest = new ValidationRequest();
		validationRequest.setUserId(inUserId);
		validationRequest.setPropositions(propDefs);
//		validationRequest.setTargetProposition(PropositionUtil
//				.convert(targetProposition, this.valueCompDao));

		EtlClient etlClient = new EtlClient(this.applicationProperties
				.getEtlUrl());
		try {
			etlClient.validatePropositions(validationRequest);
		} catch (ClientException e) {
			LOGGER.error(e.getMessage(), e);
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED, e.getMessage());
		}
	}

	@GET
	@Path("/user/get/{propId}")
	public DataElement getUserProposition(
			@PathParam("propId") Long inPropositionId) {
		DataElement dataElement = null;
		DataElementEntity proposition = this.propositionDao.retrieve(inPropositionId);
		this.propositionDao.refresh(proposition);
		if (proposition != null) {
			if (proposition.isInSystem()) {
				try {
					dataElement = this.fetchSystemProposition(
							proposition.getUserId(), proposition.getKey());
				} catch (PropositionFindException ex) {
					throw new HttpStatusException(
							Response.Status.INTERNAL_SERVER_ERROR, ex);
				}
				dataElement.setCreated(proposition.getCreated());
				dataElement.setLastModified(proposition.getLastModified());
			} else {
				PropositionTranslatorVisitor visitor =
						getPropositionTranslatorVisitor();
				proposition.accept(visitor);
				dataElement = visitor.getDataElement();
			}
		}
		return dataElement;
	}

	private PropositionTranslatorVisitor getPropositionTranslatorVisitor() {
		return new PropositionTranslatorVisitor(
				this.systemPropositionTranslator,
				this.sequenceTranslator, this.categorizationTranslator,
				this.frequencyTranslator, this.valueThresholdsTranslator);
	}

	@DELETE
	@Path("/user/delete/{userId}/{propId}")
	public Response deleteUserPropositions(@PathParam("userId") Long inUserId,
			@PathParam("propId") Long inPropositionId) {

		// the response to return;
		Response response = Response.ok().build();

		// fetch the proposition for the given ID
		DataElementEntity target = this.propositionDao.retrieve(inPropositionId);
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
			List<DataElementEntity> others = this.propositionDao.getByUserId(target
					.getUserId());

			// now loop through and make sure that the given proposition is
			// not used in the definition of any of the other propositions.
			boolean error = false;
			for (DataElementEntity proposition : others) {
				if (proposition.getId().equals(target.getId())) {
					continue;
				} else {
					PropositionChildrenVisitor visitor = new PropositionChildrenVisitor();
					proposition.accept(visitor);
					List<? extends DataElementEntity> children = visitor.getChildren();

					for (DataElementEntity p : children) {
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

			DataElementTranslatorVisitor visitor =
					getDataElementTranslatorVisitor();
			try {
				inDataElement.accept(visitor);
			} catch (DataElementHandlingException ex) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, ex);
			}
			DataElementEntity proposition = visitor.getProposition();
			proposition.setLastModified(new Date());
			this.propositionDao.update(proposition);
		} else {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"Both user ID and " + "proposition ID must be provided.");
		}
	}

	private DataElementTranslatorVisitor getDataElementTranslatorVisitor() {
		return new DataElementTranslatorVisitor(
				this.systemPropositionTranslator,
				this.sequenceTranslator,
				this.categorizationTranslator,
				this.frequencyTranslator, this.valueThresholdsTranslator);
	}

	@POST
	@Path("/user/create/sequence")
	public void insertProposition(Sequence inSequence) {
		if (inSequence.getUserId() != null) {
			try {
				SequenceEntity abstraction = this.sequenceTranslator
						.translateFromElement(inSequence);
				Date now = new Date();
				abstraction.setCreated(now);
				abstraction.setLastModified(now);
				this.propositionDao.create(abstraction);
			} catch (DataElementHandlingException ex) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, ex);
			}
		} else {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"User ID must be provided.");
		}
	}

	@PUT
	@Path("/user/update/sequence")
	public void updateProposition(Sequence inSequence) {
		if (inSequence.getId() != null && inSequence.getUserId() != null) {
			try {
				SequenceEntity abstraction = this.sequenceTranslator
						.translateFromElement(inSequence);
				Date now = new Date();
				abstraction.setLastModified(now);
				this.propositionDao.update(abstraction);
			} catch (DataElementHandlingException ex) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, ex);
			}
		} else {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"User ID and proposition ID must be provided.");
		}
	}

	@POST
	@Path("/user/create/categorization")
	public void insertProposition(Category inElement) {
		if (inElement.getUserId() != null) {
			try {
				CategoryEntity categorization = this.categorizationTranslator
						.translateFromElement(inElement);
				Date now = new Date();
				categorization.setCreated(now);
				categorization.setLastModified(now);
				this.propositionDao.create(categorization);
			} catch (DataElementHandlingException ex) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, ex);
			}
		} else {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"User ID must be " + "provided.");
		}
	}

	@PUT
	@Path("/user/update/categorization")
	public void updateProposition(Category inElement) {
		if (inElement.getId() != null && inElement.getUserId() != null) {
			try {
				CategoryEntity categorization = this.categorizationTranslator
						.translateFromElement(inElement);
				Date now = new Date();
				categorization.setLastModified(now);
				this.propositionDao.update(categorization);
			} catch (DataElementHandlingException ex) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, ex);
			}
		} else {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"Both user ID and " + "proposition ID must be provided.");
		}
	}

	private SystemElement fetchSystemProposition(Long inUserId,
			String inKey) throws PropositionFindException {
		return PropositionUtil.toSystemElement(
				systemPropositionFinder.find(inUserId, inKey), false, inUserId,
				this.systemPropositionFinder);
	}
}
