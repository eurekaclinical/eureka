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
package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import java.util.Date;

import javax.ws.rs.core.Response;

import org.protempa.PropositionDefinition;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PropositionUtil;

/**
 *
 * @author Andrew Post
 */
final class TranslatorSupport {

	private final PropositionDao propositionDao;
	private final SystemPropositionFinder finder;

	@Inject
	public TranslatorSupport(PropositionDao propositionDao,
			SystemPropositionFinder finder) {
		this.propositionDao = propositionDao;
		this.finder = finder;
	}

	/**
	 * Retrieves the user or system entity if it exists, otherwise creates an
	 * entity from a system proposition with with the given user id and key.
	 *
	 * @param userId
	 * @param key
	 * @return
	 * @throws DataElementHandlingException
	 */
	DataElementEntity getSystemEntityInstance(Long userId, String key)
			throws DataElementHandlingException {
		DataElementEntity abstractedFrom =
				propositionDao.getByUserAndKey(userId, key);
		if (abstractedFrom == null) {
			try {
				PropositionDefinition propDef = this.finder.find(userId, key);
				abstractedFrom = PropositionUtil.toSystemProposition(propDef,
						userId);
			} catch (PropositionFindException ex) {
				throw new DataElementHandlingException(
						Response.Status.PRECONDITION_FAILED,
						"No system data element with name " + key, ex);
			}
		}
		return abstractedFrom;
	}

	/**
	 * Creates a new user entity if one with the provided data element's id
	 * does not already exist.
	 *
	 * @param <P>
	 * @param element
	 * @param cls
	 * @return
	 * @throws DataElementHandlingException if there already exists a system
	 * data element with the same key as the candidate user data element.
	 */
	<P extends DataElementEntity> P getUserEntityInstance(DataElement element,
			Class<P> cls) throws DataElementHandlingException {
		String key;
		if (element.getKey() != null) {
			key = element.getKey();
		} else {
			key = element.getDisplayName();
		}

		Date now = new Date();

		P result;
		DataElementEntity oldEntity;
		if (element.getId() != null) {
			oldEntity = propositionDao.retrieve(element.getId());
		} else {
			oldEntity = null;
		}
		if (cls.isInstance(oldEntity)) {
			result = cls.cast(oldEntity);
		} else {
			try {
				result = cls.newInstance();
			} catch (InstantiationException ex) {
				throw new AssertionError("Could not instantiate entity " + key
						+ ": " + ex.getMessage());
			} catch (IllegalAccessException ex) {
				throw new AssertionError("Could not instantiate entity " + key
						+ ": " + ex.getMessage());
			}
			result.setCreated(now);
		}

		result.setInSystem(false);
		result.setLastModified(now);

		populateCommonEntityFields(result, element);

		return result;
	}

	/**
	 * Populates the fields common to all propositions based on the given
	 * proposition.
	 *
	 * @param entity the {@link DataElementEntity} to populate. Modified as a
	 * result of calling this method.
	 * @param dataElement the {@link DataElement} to get the data from
	 */
	private static void populateCommonEntityFields(DataElementEntity entity,
			DataElement dataElement) {
		entity.setId(dataElement.getId());
		entity.setDisplayName(dataElement.getDisplayName());
		entity.setDescription(dataElement.getDescription());
		//proposition.setCreated(dataElement.getCreated());
		//proposition.setLastModified(dataElement.getLastModified());
		entity.setUserId(dataElement.getUserId());

		if (dataElement.getKey() != null) {
			entity.setKey(dataElement.getKey());
		} else {
			entity.setKey(dataElement.getDisplayName());
		}
	}
}
