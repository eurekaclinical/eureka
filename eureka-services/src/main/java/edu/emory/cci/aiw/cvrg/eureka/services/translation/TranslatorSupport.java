/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.protempa.PropositionDefinition;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfigParams;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.DataElementEntityDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.resource.SourceConfigResource;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PropositionUtil;

/**
 *
 * @author Andrew Post
 */
final class TranslatorSupport {

	private final SourceConfigResource sourceConfigResource;

	private static final class DataElementMapKey {

		private final Long userId;
		private final String key;

		DataElementMapKey(Long userId, String key) {
			this.userId = userId;
			this.key = key;
		}

		Long getUserId() {
			return userId;
		}

		String getKey() {
			return key;
		}

		@Override
		public int hashCode() {
			int hash = 5;
			hash = 97 * hash + (this.userId != null ? this.userId.hashCode() : 0);
			hash = 97 * hash + (this.key != null ? this.key.hashCode() : 0);
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final DataElementMapKey other = (DataElementMapKey) obj;
			if (this.userId != other.userId && (this.userId == null || !this.userId.equals(other.userId))) {
				return false;
			}
			if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
				return false;
			}
			return true;
		}
	}
	private final Map<DataElementMapKey, DataElementEntity> dataElementEntities;
	private final DataElementEntityDao dataElementEntityDao;
	private final SystemPropositionFinder finder;

	@Inject
	public TranslatorSupport(DataElementEntityDao dataElementEntityDao,
			SystemPropositionFinder finder,
			SourceConfigResource inSourceConfigResource) {
		this.dataElementEntityDao = dataElementEntityDao;
		this.finder = finder;
		this.dataElementEntities
				= new HashMap<>();
		this.sourceConfigResource = inSourceConfigResource;
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
	DataElementEntity getUserOrSystemEntityInstance(Long userId, String key)
			throws DataElementHandlingException {
		DataElementEntity dataElementEntity
				= dataElementEntityDao.getUserOrSystemByUserAndKey(userId, key);
		
		if (dataElementEntity == null) {
			/*
			 * Hack to get an ontology source that assumes all Protempa configurations
			 * for a user point to the same knowledge source backends. This will go away.
			 */
			List<SourceConfigParams> scps = this.sourceConfigResource.getParamsList();
			if (scps.isEmpty()) {
				throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, "No source configs");
			}
			String sourceConfigId = scps.get(0).getId();
			DataElementMapKey deMapKey = new DataElementMapKey(userId, key);
			dataElementEntity = this.dataElementEntities.get(deMapKey);
			if (dataElementEntity == null) {
				try {
					PropositionDefinition propDef
							= this.finder.find(sourceConfigId, key);
					dataElementEntity
							= PropositionUtil.toSystemProposition(propDef,
									userId);
					this.dataElementEntities.put(deMapKey, dataElementEntity);
				} catch (PropositionFindException ex) {
					throw new DataElementHandlingException(
							Response.Status.PRECONDITION_FAILED,
							"No system data element with name " + key, ex);
				}
			}
		}
		return dataElementEntity;
	}

	/**
	 * Creates a new user entity if one with the provided data element's id does
	 * not already exist.
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
			key = "USER:" + element.getDisplayName();
		}

		Date now = new Date();

		P result;
		DataElementEntity oldEntity = null;
		if (element.getId() != null) {
			oldEntity = dataElementEntityDao.retrieve(element.getId());
		}
		if (cls.isInstance(oldEntity)) {
			result = cls.cast(oldEntity);
		} else {
			try {
				result = cls.newInstance();
			} catch (InstantiationException | IllegalAccessException ex) {
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
			entity.setKey("USER:" + dataElement.getDisplayName());
		}
	}
}
