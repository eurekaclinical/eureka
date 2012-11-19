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
import java.util.Collection;
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

import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PairDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.PropositionDefinitionVisitor;
import org.protempa.SliceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionChildrenVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.SequenceTranslator;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PropositionUtil;

/**
 * REST Web Service
 * 
 * @author hrathod
 */
@Path("/proposition")
public class PropositionResource {

	private final PropositionDao propositionDao;
	private final ServiceProperties applicationProperties;
	private final SystemPropositionFinder systemPropositionFinder;
	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
	        .getLogger(PropositionResource.class);

	/**
	 * Creates a new instance of PropositionResource
	 */
	@Inject
	public PropositionResource(PropositionDao inPropositionDao,
	        ServiceProperties inApplicationProperties,
	        SystemPropositionFinder inFinder) {
		this.propositionDao = inPropositionDao;
		this.applicationProperties = inApplicationProperties;
		this.systemPropositionFinder = inFinder;
	}

	@GET
	@Path("/system/{userId}/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SystemElement> getSystemPropositions(
	        @PathParam("userId") Long inUserId) {

		List<SystemElement> wrappers = new ArrayList<SystemElement>();

		List<String> propNames = this.applicationProperties
		        .getDefaultSystemPropositions();
		for (String name : propNames) {
			try {
				wrappers.add(fetchSystemProposition(inUserId, name));
			} catch (UniformInterfaceException e) {
				if (e.getResponse().getStatus() != 404) {
					throw new HttpStatusException(
					        Response.Status.INTERNAL_SERVER_ERROR, e);
				} else {
					LOGGER.warn("Invalid proposition id specified in system "
					        + "propositions list: " + name);
				}
			}
		}

		return wrappers;
	}

	@GET
	@Path("/system/{userId}/{propKey}")
	@Produces(MediaType.APPLICATION_JSON)
	public SystemElement getSystemProposition(
	        @PathParam("userId") Long inUserId,
	        @PathParam("propKey") String inKey) {
		return fetchSystemProposition(inUserId, inKey);
	}

	@POST
	@Path("/system/{userId}/batch")
	@Produces(MediaType.APPLICATION_JSON)
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
	@Produces(MediaType.APPLICATION_JSON)
	public List<DataElement> getUserPropositions(
	        @PathParam("userId") Long inUserId) {
		List<DataElement> result = new ArrayList<DataElement>();
		for (Proposition p : this.propositionDao.getByUserId(inUserId)) {
			this.propositionDao.refresh(p);
			result.add(PropositionUtil.wrap(p, propositionDao,
			        systemPropositionFinder));
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
		validationRequest.setTargetProposition(PropositionUtil.pack(
		        targetProposition));

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
	@Produces(MediaType.APPLICATION_JSON)
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
				dataElement = PropositionUtil.wrap(proposition, propositionDao,
				        systemPropositionFinder);
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
							                + "contains a " + "reference to "
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
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateProposition(DataElement inDataElement) {
		if (inDataElement.getUserId() != null && inDataElement.getId() != null) {
			Proposition proposition = PropositionUtil.unwrap(inDataElement,
			        inDataElement.getUserId(), this.propositionDao, this.systemPropositionFinder);
			proposition.setLastModified(new Date());
			this.propositionDao.update(proposition);
		} else {
			throw new IllegalArgumentException(
			        "Both the user ID and the proposition ID must be provided.");
		}
	}

	@POST
	@Path("/user/create/sequence")
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertProposition(Sequence inSequence) {
		if (inSequence.getUserId() != null) {
			// create any necessary system level elements, if needed
			String primaryKey = inSequence.getPrimaryDataElement()
			        .getDataElementKey();
			createIfNeeded(inSequence.getUserId(), primaryKey);
			for (Sequence.RelatedDataElementField relatedDataElementField : inSequence
			        .getRelatedDataElements()) {
				String relatedKey = relatedDataElementField
				        .getDataElementField().getDataElementKey();
				createIfNeeded(inSequence.getUserId(), relatedKey);
			}

			SequenceTranslator translator = new SequenceTranslator(
			        inSequence.getUserId(), this.propositionDao, this.systemPropositionFinder);
			HighLevelAbstraction abstraction = translator
			        .translateFromElement(inSequence);
			Date now = new Date();
			abstraction.setCreated(now);
			abstraction.setLastModified(now);
			abstraction.setKey(abstraction.getAbbrevDisplayName());
			this.propositionDao.create(abstraction);
		} else {
			throw new IllegalArgumentException("User ID must be provided.");
		}
	}

	private void createIfNeeded(Long userId, String key) {
		Proposition proposition = this.propositionDao.getByUserAndKey(userId,
		        key);
		if (proposition == null) {
			PropositionDefinition definition = this.systemPropositionFinder
			        .find(userId, key);
			SystemProposition systemProposition = new SystemProposition();
			Date now = new Date();
			systemProposition.setCreated(now);
			systemProposition.setAbbrevDisplayName(definition
			        .getAbbreviatedDisplayName());
			systemProposition.setDisplayName(definition.getDisplayName());
			systemProposition.setInSystem(Boolean.TRUE);
			systemProposition.setKey(key);
			systemProposition.setUserId(userId);
			systemProposition.setLastModified(now);

			SystemTypeVisitor visitor = new SystemTypeVisitor();
			definition.accept(visitor);
			systemProposition.setSystemType(visitor.getSystemType());
			this.propositionDao.create(systemProposition);
		}
	}

	private SystemElement fetchSystemProposition(Long inUserId, String inKey) {
		return PropositionUtil.wrap(
		        systemPropositionFinder.find(inUserId, inKey), false, inUserId,
		        this.systemPropositionFinder);
	}

	private static class SystemTypeVisitor implements
	        PropositionDefinitionVisitor {

		private SystemProposition.SystemType type = null;

		public SystemProposition.SystemType getSystemType() {
			return this.type;
		}

		@Override
		public void visit(
		        Collection<? extends PropositionDefinition> inPropositionDefinitions) {
			throw new UnsupportedOperationException("Not implemented.");
		}

		@Override
		public void visit(
		        LowLevelAbstractionDefinition inLowLevelAbstractionDefinition) {
			this.type = SystemProposition.SystemType.LOW_LEVEL_ABSTRACTION;
		}

		@Override
		public void visit(
		        HighLevelAbstractionDefinition inHighLevelAbstractionDefinition) {
			this.type = SystemProposition.SystemType.HIGH_LEVEL_ABSTRACTION;
		}

		@Override
		public void visit(SliceDefinition inSliceDefinition) {
			this.type = SystemProposition.SystemType.SLICE_ABSTRACTION;
		}

		@Override
		public void visit(EventDefinition inDefinition) {
			this.type = SystemProposition.SystemType.EVENT;
		}

		@Override
		public void visit(
		        PrimitiveParameterDefinition inPrimitiveParameterDefinition) {
			this.type = SystemProposition.SystemType.PRIMITIVE_PARAMETER;
		}

		@Override
		public void visit(ConstantDefinition inConstantDefinition) {
			this.type = SystemProposition.SystemType.CONSTANT;
		}

		@Override
		public void visit(PairDefinition inPairDefinition) {
			throw new UnsupportedOperationException("Not implemented.");
		}
	}
}
