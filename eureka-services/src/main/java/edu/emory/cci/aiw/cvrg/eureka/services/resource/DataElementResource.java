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

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionChildrenVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.DataElementEntityDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.DataElementTranslatorVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.DataElementEntityTranslatorVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.SummarizingDataElementEntityTranslatorVisitor;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;

/**
 * PropositionCh
 *
 * @author hrathod
 */
@Path("/protected/dataelement")
@RolesAllowed({"researcher"})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DataElementResource {

	private static final ResourceBundle messages =
			ResourceBundle.getBundle("Messages");
	private final DataElementEntityDao propositionDao;
	private final UserDao userDao;
	private final DataElementEntityTranslatorVisitor dataElementEntityTranslatorVisitor;
	private final DataElementTranslatorVisitor dataElementTranslatorVisitor;
	private final SummarizingDataElementEntityTranslatorVisitor summarizingDataElementEntityTranslatorVisitor;

	@Inject
	public DataElementResource(DataElementEntityDao inDao, UserDao userDao,
			DataElementEntityTranslatorVisitor inPropositionTranslatorVisitor,
			SummarizingDataElementEntityTranslatorVisitor inSummarizingPropositionTranslatorVisitor,
			DataElementTranslatorVisitor inDataElementTranslatorVisitor) {
		this.propositionDao = inDao;
		this.dataElementEntityTranslatorVisitor = inPropositionTranslatorVisitor;
		this.summarizingDataElementEntityTranslatorVisitor = inSummarizingPropositionTranslatorVisitor;
		this.dataElementTranslatorVisitor = inDataElementTranslatorVisitor;
		this.userDao = userDao;
	}

	@GET
	@Path("/")
	public List<DataElement> getAll(@Context HttpServletRequest request,
			@DefaultValue("false") @QueryParam("summarize") boolean summarize) {
		User user = this.userDao.getByName(request.getUserPrincipal().getName());
		List<DataElement> result = new ArrayList<>();
		List<DataElementEntity> dataElementEntities = 
				this.propositionDao.getByUserId(user.getId());
		for (DataElementEntity dataElementEntity : dataElementEntities) {
			result.add(convertToDataElement(dataElementEntity, summarize));
		}

		return result;
	}

	@GET
	@Path("/{key}")
	public DataElement get(@Context HttpServletRequest request,
			@PathParam("key") String inKey, 
			@DefaultValue("false") @QueryParam("summarize") boolean summarize) {
		DataElement result = readDataElement(request, inKey, summarize);
		if (result == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		} else {
			return result;
		}
	}

	@POST
	public void create(DataElement inElement) {
		if (inElement.getId() != null) {
			throw new HttpStatusException(
					Response.Status.PRECONDITION_FAILED, "Data element to "
					+ "be created should not have an identifier.");
		}

		if (inElement.getUserId() == null) {
			throw new HttpStatusException(
					Response.Status.PRECONDITION_FAILED, "Data element to "
					+ "be created should have a user identifier.");
		}
		
		if (inElement.isSummarized()) {
			throw new HttpStatusException(
					Response.Status.PRECONDITION_FAILED, "Data element to "
					+ "be created cannot be summarized.");
		}

		try {
			inElement.accept(this.dataElementTranslatorVisitor);
		} catch (DataElementHandlingException ex) {
			throw new HttpStatusException(ex.getStatus(), ex);
		}
		DataElementEntity dataElementEntity = this.dataElementTranslatorVisitor
				.getDataElementEntity();
		
		if (this.propositionDao.getByUserAndKey(
				inElement.getUserId(), dataElementEntity.getKey()) != null) {
			String msg = this.messages.getString(
					"dataElementResource.create.error.duplicate");
			throw new HttpStatusException(Status.CONFLICT, msg);
		}
		
		Date now = new Date();
		dataElementEntity.setCreated(now);
		dataElementEntity.setLastModified(now);

		this.propositionDao.create(dataElementEntity);
	}

	@PUT
	public void update(@Context HttpServletRequest request, DataElement inElement) {
		if (inElement.getId() == null) {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"The data element to be updated must "
					+ "have a unique identifier.");
		}

		if (inElement.getUserId() == null) {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED,
					"The data element to be updated must "
					+ "have a user identifier");
		}
		
		if (inElement.isSummarized()) {
			throw new HttpStatusException(
					Response.Status.PRECONDITION_FAILED, "Data element to "
					+ "be updated cannot be summarized.");
		}
		
		try {
			inElement.accept(this.dataElementTranslatorVisitor);
		} catch (DataElementHandlingException ex) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
		DataElementEntity dataElementEntity = this.dataElementTranslatorVisitor
				.getDataElementEntity();
		DataElementEntity oldDataElementEntity = this.propositionDao.retrieve(inElement
				.getId());
		
		if (oldDataElementEntity == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		} else if (!oldDataElementEntity.getUserId().equals(inElement.getUserId())) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		} else {
			User user = this.userDao.getByName(request.getUserPrincipal().getName());
			if (!user.getId().equals(oldDataElementEntity.getUserId())) {
				throw new HttpStatusException(Response.Status.NOT_FOUND);
			}
		}

		Date now = new Date();
		dataElementEntity.setLastModified(now);
		dataElementEntity.setCreated(oldDataElementEntity.getCreated());
		dataElementEntity.setId(oldDataElementEntity.getId());
		this.propositionDao.update(dataElementEntity);
	}

	@DELETE
	@Path("/{userId}/{key}")
	public void delete(@PathParam("userId") Long inUserId,
			@PathParam("key") String inKey) {
		DataElementEntity dataElementEntity =
				this.propositionDao.getByUserAndKey(inUserId, inKey);
		if (dataElementEntity == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		}
		List<String> dataElementsUsedIn = 
				getDataElementsUsedIn(inUserId, dataElementEntity);
		if (!dataElementsUsedIn.isEmpty()) {
			deleteFailed(dataElementsUsedIn, dataElementEntity);
		}

		this.propositionDao.remove(dataElementEntity);
	}

	private void deleteFailed(List<String> dataElementsUsedIn, DataElementEntity proposition) throws HttpStatusException {
		String dataElementList;
		int size = dataElementsUsedIn.size();
		if (size > 1) {
			List<String> subList =
					dataElementsUsedIn.subList(0,
							dataElementsUsedIn.size() - 1);
			dataElementList = StringUtils.join(subList, ", ")
					+ " and "
					+ dataElementsUsedIn.get(size - 1);
		} else {
			dataElementList = dataElementsUsedIn.get(0);
		}
		MessageFormat usedByOtherDataElements =
				new MessageFormat(messages.getString(
						"dataElementResource.delete.error.usedByOtherDataElements"));
		String msg = usedByOtherDataElements.format(
				new Object[]{
					proposition.getDisplayName(),
					dataElementsUsedIn.size(),
					dataElementList
				});
		throw new HttpStatusException(
				Response.Status.PRECONDITION_FAILED, msg);
	}

	private List<String> getDataElementsUsedIn(Long inUserId, DataElementEntity proposition) {
		List<DataElementEntity> others =
				this.propositionDao.getByUserId(inUserId);
		List<String> dataElementsUsedIn = new ArrayList<>();
		for (DataElementEntity other : others) {
			if (!other.getId().equals(proposition.getId())) {
				PropositionChildrenVisitor visitor =
						new PropositionChildrenVisitor();
				other.accept(visitor);
				for (DataElementEntity child : visitor.getChildren()) {
					if (child.getId().equals(proposition.getId())) {
						dataElementsUsedIn.add(other.getDisplayName());
					}
				}
			}
		}
		return dataElementsUsedIn;
	}
	
	private DataElement readDataElement(HttpServletRequest request, String inKey, boolean summarize) {
		User user = this.userDao.getByName(request.getUserPrincipal().getName());
		DataElement result;
		DataElementEntity dataElementEntity =
				this.propositionDao.getByUserAndKey(user.getId(), inKey);
		result = convertToDataElement(dataElementEntity, summarize);
		return result;
	}

	private DataElement convertToDataElement(DataElementEntity dataElementEntity, boolean summarize) {
		DataElement result;
		if (dataElementEntity == null) {
			result = null;
		} else if (summarize) {
			result = toSummarizedDataElement(dataElementEntity);
		} else {
			result = toDataElement(dataElementEntity);
		}
		return result;
	}
	
	private DataElement toDataElement(DataElementEntity dataElementEntity) {
		dataElementEntity.accept(this.dataElementEntityTranslatorVisitor);
		return this.dataElementEntityTranslatorVisitor.getDataElement();
	}
	
	private DataElement toSummarizedDataElement(DataElementEntity dataElementEntity) {
		dataElementEntity.accept(this.summarizingDataElementEntityTranslatorVisitor);
		return this.summarizingDataElementEntityTranslatorVisitor.getDataElement();
	}
}
