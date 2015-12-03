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


import javax.ws.rs.core.Response;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedDataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropertyConstraint;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionTypeVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;

/**
 * Contains common utility functions for all implementations of
 * {@link PropositionTranslator}.
 */
class PropositionTranslatorUtil {

	static void populateExtendedProposition(ExtendedDataElement ep, 
			DataElementEntity proposition, DataElementField dataElement, 
			TimeUnitDao timeUnitDao, ValueComparatorDao valCompDao) 
			throws DataElementHandlingException {
		ep.setDataElementEntity(proposition);
		if (dataElement.getHasDuration() == Boolean.TRUE) {
			ep.setMinDuration(dataElement.getMinDuration());
			Long minDurationUnits = dataElement.getMinDurationUnits();
			if (minDurationUnits == null) {
				throw new DataElementHandlingException(
						Response.Status.BAD_REQUEST, 
						"Min duration units must be specified");
			}
			ep.setMinDurationTimeUnit(timeUnitDao.retrieve(minDurationUnits));
			ep.setMaxDuration(dataElement.getMaxDuration());
			Long maxDurationUnits = dataElement.getMaxDurationUnits();
			if (maxDurationUnits == null) {
				throw new DataElementHandlingException(
						Response.Status.BAD_REQUEST, 
						"Max duration units must be specified");
			}
			ep.setMaxDurationTimeUnit(timeUnitDao.retrieve(maxDurationUnits));
		} else {
			ep.setMinDuration(null);
			Long minDurationUnitsL = dataElement.getMinDurationUnits();
			TimeUnit minDurationUnits;
			if (minDurationUnitsL != null) {
				minDurationUnits = timeUnitDao.retrieve(minDurationUnitsL);
			} else {
				minDurationUnits = timeUnitDao.getDefault();
			}
			ep.setMinDurationTimeUnit(minDurationUnits);
			ep.setMaxDuration(null);
			Long maxDurationUnitsL = dataElement.getMaxDurationUnits();
			TimeUnit maxDurationUnits;
			if (maxDurationUnitsL != null) {
				maxDurationUnits = timeUnitDao.retrieve(maxDurationUnitsL);
			} else {
				maxDurationUnits = timeUnitDao.getDefault();
			}
			ep.setMaxDurationTimeUnit(maxDurationUnits);
		}
		if (dataElement.getHasPropertyConstraint() == Boolean.TRUE) {
			String propertyName = dataElement.getProperty();
			if (propertyName == null) {
				throw new DataElementHandlingException(
						Response.Status.BAD_REQUEST, 
						"A property name must be specified");
			}
			PropertyConstraint pc = ep.getPropertyConstraint();
			if (pc == null) {
				pc = new PropertyConstraint();
			}
			pc.setPropertyName(propertyName);
			pc.setValue(dataElement.getPropertyValue());
			ValueComparator valComp = valCompDao.getByName("=");
			if (valComp == null) {
				throw new AssertionError("Invalid value comparator: =");
			}
			pc.setValueComparator(valComp);
			ep.setPropertyConstraint(pc);
		} else {
			ep.setPropertyConstraint(null);
		}
	}

	private PropositionTranslatorUtil() {
		// prevents instantiation
	}

	/**
	 * Populates the fields common to all data elements based on the given
	 * proposition.
	 * 
	 * @param dataElement
	 *            the {@link DataElement} to populate. Modified as a result of
	 *            calling this method.
	 * @param proposition
	 *            the {@link DataElementEntity} to get the data from
	 */
	static void populateCommonDataElementFields(DataElement dataElement,
	        DataElementEntity proposition) {
		dataElement.setId(proposition.getId());
		dataElement.setKey(proposition.getKey());
		dataElement.setDisplayName(proposition.getDisplayName());
		dataElement.setDescription(proposition.getDescription());
		dataElement.setCreated(proposition.getCreated());
		dataElement.setLastModified(proposition.getLastModified());
		dataElement.setUserId(proposition.getUserId());
		dataElement.setInSystem(proposition.isInSystem());
	}

	static ExtendedDataElement createOrUpdateExtendedProposition(
			ExtendedDataElement origEP,
	        DataElementField dataElement, Long userId, 
			TimeUnitDao timeUnitDao, 
			TranslatorSupport translatorSupport,
			ValueComparatorDao valCompDao) throws DataElementHandlingException {

		ExtendedDataElement ep = origEP;
		if (ep == null) {
			ep = new ExtendedDataElement();
		}
		DataElementEntity proposition = 
				translatorSupport.getUserOrSystemEntityInstance(userId, 
				dataElement.getDataElementKey());
		populateExtendedProposition(ep, proposition, dataElement, timeUnitDao, valCompDao);

		return ep;
	}
	
	static DataElementField createDataElementField(ExtendedDataElement ep) {
		DataElementField dataElement = new DataElementField();
		dataElement.setDataElementKey(ep.getDataElementEntity().getKey());
		dataElement.setDataElementDescription(ep.getDataElementEntity()
				.getDescription());
		dataElement.setDataElementDisplayName(ep.getDataElementEntity()
				.getDisplayName());
		PropositionTypeVisitor v = new PropositionTypeVisitor();
		ep.getDataElementEntity().accept(v);
		dataElement.setType(v.getType());
		if (ep.getMinDuration() != null) {
			dataElement.setHasDuration(true);
			dataElement.setMinDuration(ep.getMinDuration());
			dataElement.setMinDurationUnits(ep.getMinDurationTimeUnit()
			        .getId());
		}
		if (ep.getMaxDuration() != null) {
			dataElement.setHasDuration(true);
			dataElement.setMaxDuration(ep.getMaxDuration());
			dataElement.setMaxDurationUnits(ep.getMaxDurationTimeUnit()
			        .getId());
		}
		if (ep.getPropertyConstraint() != null) {
			dataElement.setHasPropertyConstraint(true);
			dataElement.setProperty(ep.getPropertyConstraint()
			        .getPropertyName());
			dataElement.setPropertyValue(ep.getPropertyConstraint().getValue());
		}
		return dataElement;
	}
}
