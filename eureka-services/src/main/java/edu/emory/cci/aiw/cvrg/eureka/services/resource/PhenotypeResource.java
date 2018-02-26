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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import org.eurekaclinical.eureka.client.comm.Phenotype;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.PhenotypeEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.PropositionChildrenVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.UserEntity;
import org.eurekaclinical.eureka.client.comm.exception.PhenotypeHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.PhenotypeEntityTranslatorVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.PhenotypeTranslatorVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.SummarizingPhenotypeEntityTranslatorVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PhenotypeEntityDao;
import java.net.URI;
import org.eurekaclinical.standardapis.exception.HttpStatusException;

/**
 * PropositionCh
 *
 * @author hrathod
 */
@Transactional
@Path("/protected/phenotypes")
@RolesAllowed({"researcher"})
@Consumes(MediaType.APPLICATION_JSON)
public class PhenotypeResource {
    
	private static final Logger LOGGER
			= LoggerFactory.getLogger(PhenotypeResource.class);
        
	private static final ResourceBundle messages
			= ResourceBundle.getBundle("Messages");
	private final PhenotypeEntityDao phenotypeEntityDao;
	private final UserDao userDao;
	private final PhenotypeEntityTranslatorVisitor pETranslatorVisitor;
	private final PhenotypeTranslatorVisitor phenotypeTranslatorVisitor;
	private final SummarizingPhenotypeEntityTranslatorVisitor summpETranslatorVisitor;

	@Inject
	public PhenotypeResource(PhenotypeEntityDao inDao, UserDao inUserDao,
			PhenotypeEntityTranslatorVisitor inPETranslatorVisitor,
			SummarizingPhenotypeEntityTranslatorVisitor inSummpETranslatorVisitor,
			PhenotypeTranslatorVisitor inPhenotypeTranslatorVisitor) {
		this.phenotypeEntityDao = inDao;
		this.pETranslatorVisitor = inPETranslatorVisitor;
		this.summpETranslatorVisitor = inSummpETranslatorVisitor;
		this.phenotypeTranslatorVisitor = inPhenotypeTranslatorVisitor;
		this.userDao = inUserDao;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Phenotype> getAll(@Context HttpServletRequest inRequest,
			@DefaultValue("false") @QueryParam("summarize") boolean inSummarize) {
		UserEntity user = this.userDao.getByHttpServletRequest(inRequest);
		List<Phenotype> result = new ArrayList<>();
		List<PhenotypeEntity> phenotypeEntities
				= this.phenotypeEntityDao.getByUserId(user.getId());
		for (PhenotypeEntity phenotypeEntity : phenotypeEntities) {
			result.add(convertToPhenotype(phenotypeEntity, inSummarize));
		}

		return result;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{key}")
	public Phenotype get(@Context HttpServletRequest inRequest,
			@PathParam("key") String inKey,
			@DefaultValue("false") @QueryParam("summarize") boolean inSummarize) {
		UserEntity user = this.userDao.getByHttpServletRequest(inRequest);
		Phenotype result = readPhenotype(user, inKey, inSummarize);
		if (result == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		} else {
			return result;
		}
	}

	@POST
	public Response create(@Context HttpServletRequest request, Phenotype inPhenotype) {
		if (inPhenotype.getId() != null) {
			throw new HttpStatusException(
					Response.Status.PRECONDITION_FAILED, "Phenotype to "
					+ "be created should not have an identifier.");
		}

		if (inPhenotype.getUserId() == null) {
			throw new HttpStatusException(
					Response.Status.PRECONDITION_FAILED, "Phenotype to "
					+ "be created should have a user identifier.");
		}

		if (inPhenotype.isSummarized()) {
			throw new HttpStatusException(
					Response.Status.PRECONDITION_FAILED, "Phenotype to "
					+ "be created cannot be summarized.");
		}

		try {
			inPhenotype.accept(this.phenotypeTranslatorVisitor);
		} catch (PhenotypeHandlingException ex) {
			throw new HttpStatusException(ex.getStatus(), ex);
		}
                
		PhenotypeEntity phenotypeEntity = this.phenotypeTranslatorVisitor
				.getPhenotypeEntity();

		if (this.phenotypeEntityDao.getByUserAndKey(
				inPhenotype.getUserId(), phenotypeEntity.getKey()) != null) {
			String msg = messages.getString(
					"phenotypeResource.create.error.duplicate");
			throw new HttpStatusException(Status.CONFLICT, msg);
		}

		Date now = new Date();
		phenotypeEntity.setCreated(now);
		phenotypeEntity.setLastModified(now);

		this.phenotypeEntityDao.create(phenotypeEntity);
                
                Long id;
                id = phenotypeEntity.getId();
                return Response.created(URI.create("/" + id)).build();
	}

	@PUT
	@Path("/{id}")
	public void update(@Context HttpServletRequest inRequest,
			@PathParam("id") Long inId,
			Phenotype inElement) {
            
		if (inElement.getId() == null) {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"The phenotype to be updated must "
					+ "have a unique identifier.");
		}

		if (inElement.getUserId() == null) {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"The phenotype to be updated must "
					+ "have a user identifier");
		}

		if (inElement.isSummarized()) {
			throw new HttpStatusException(
					Response.Status.PRECONDITION_FAILED, "Phenotype to "
					+ "be updated cannot be summarized.");
		}

		try {
			inElement.accept(this.phenotypeTranslatorVisitor);
		} catch (PhenotypeHandlingException ex) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
		PhenotypeEntity phenotypeEntity = this.phenotypeTranslatorVisitor
				.getPhenotypeEntity();
		
		PhenotypeEntity potentialConflict = 
				this.phenotypeEntityDao.getByUserAndKey(
				inElement.getUserId(), phenotypeEntity.getKey());
		if (potentialConflict != null && 
				!potentialConflict.getId().equals(phenotypeEntity.getId())) {
			String msg = messages.getString(
					"phenotypeResource.update.error.duplicate");
			throw new HttpStatusException(Status.CONFLICT, msg);
		}
		
		PhenotypeEntity oldPhenotypeEntity
				= this.phenotypeEntityDao.retrieve(inElement.getId());

		if (oldPhenotypeEntity == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		} else if (!oldPhenotypeEntity.getUserId().equals(
				inElement.getUserId())) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		} else {
			UserEntity user
					= this.userDao.getByHttpServletRequest(inRequest);
			if (!user.getId().equals(oldPhenotypeEntity.getUserId())) {
				throw new HttpStatusException(Response.Status.NOT_FOUND);
			}
		}

		Date now = new Date();
		phenotypeEntity.setLastModified(now);
		phenotypeEntity.setCreated(oldPhenotypeEntity.getCreated());
		phenotypeEntity.setId(oldPhenotypeEntity.getId());
		this.phenotypeEntityDao.update(phenotypeEntity);
	}

	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") Long inId, @Context HttpServletRequest inRequest) {
		UserEntity user = this.userDao.getByHttpServletRequest(inRequest);
		PhenotypeEntity phenotypeEntity
				= this.phenotypeEntityDao.getById(inId);
		if (phenotypeEntity == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		}
		List<String> phenotypesUsedIn
				= getPhenotypesUsedIn(user.getId(), phenotypeEntity);
		if (!phenotypesUsedIn.isEmpty()) {
			deleteFailed(phenotypesUsedIn, phenotypeEntity);
		}

		this.phenotypeEntityDao.remove(phenotypeEntity);
	}

	private void deleteFailed(List<String> phenotypesUsedIn,
			PhenotypeEntity proposition) throws HttpStatusException {
		String phenotypeList;
		int size = phenotypesUsedIn.size();
		if (size > 1) {
			List<String> subList
					= phenotypesUsedIn.subList(0,
							phenotypesUsedIn.size() - 1);
			phenotypeList = StringUtils.join(subList, ", ")
					+ " and "
					+ phenotypesUsedIn.get(size - 1);
		} else {
			phenotypeList = phenotypesUsedIn.get(0);
		}
		MessageFormat usedByOtherPhenotypes
				= new MessageFormat(messages.getString(
								"phenotypeResource.delete.error.usedByOtherPhenotypes"));
		String msg = usedByOtherPhenotypes.format(
				new Object[]{
					proposition.getDisplayName(),
					phenotypesUsedIn.size(),
					phenotypeList
				});
		throw new HttpStatusException(
				Response.Status.PRECONDITION_FAILED, msg);
	}

	private List<String> getPhenotypesUsedIn(Long inUserId,
			PhenotypeEntity proposition) {
		List<PhenotypeEntity> others
				= this.phenotypeEntityDao.getByUserId(inUserId);
		List<String> phenotypesUsedIn = new ArrayList<>();
		for (PhenotypeEntity other : others) {
			if (!other.getId().equals(proposition.getId())) {
				PropositionChildrenVisitor visitor
						= new PropositionChildrenVisitor();
				other.accept(visitor);
				for (PhenotypeEntity child : visitor.getChildren()) {
					if (child.getId().equals(proposition.getId())) {
						phenotypesUsedIn.add(other.getDisplayName());
					}
				}
			}
		}
		return phenotypesUsedIn;
	}

	private Phenotype readPhenotype(UserEntity userEntity, 
			String inKey, boolean summarize) {
		Phenotype result;
		PhenotypeEntity phenotypeEntity
				= this.phenotypeEntityDao.getByUserAndKey(
						userEntity.getId(), inKey);
		result = convertToPhenotype(phenotypeEntity, summarize);
		return result;
	}

	private Phenotype convertToPhenotype(
			PhenotypeEntity phenotypeEntity, boolean summarize) {
		Phenotype result = null;
		if (phenotypeEntity != null) {
			if (summarize) {
				result = toSummarizedPhenotype(phenotypeEntity);
			} else {
				result = toPhenotype(phenotypeEntity);
			}
		}
		return result;
	}

	private Phenotype toPhenotype(PhenotypeEntity phenotypeEntity) {
		phenotypeEntity.accept(this.pETranslatorVisitor);
		return this.pETranslatorVisitor.getPhenotype();
	}

	private Phenotype toSummarizedPhenotype(
			PhenotypeEntity phenotypeEntity) {
		phenotypeEntity.accept(this.summpETranslatorVisitor);
		return this.summpETranslatorVisitor.getPhenotype();
	}
}
