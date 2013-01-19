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
package edu.emory.cci.aiw.cvrg.eureka.services.translation;


import javax.ws.rs.core.Response;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedDataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropertyConstraint;
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
						Response.Status.PRECONDITION_FAILED, 
						"Min duration units must be specified");
			}
			ep.setMinDurationTimeUnit(timeUnitDao.retrieve(minDurationUnits));
			ep.setMaxDuration(dataElement.getMaxDuration());
			Long maxDurationUnits = dataElement.getMaxDurationUnits();
			if (maxDurationUnits == null) {
				throw new DataElementHandlingException(
						Response.Status.PRECONDITION_FAILED, 
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
				minDurationUnits = timeUnitDao.getByName(
						DataElement.DEFAULT_TIME_UNIT_NAME);
			}
			ep.setMinDurationTimeUnit(minDurationUnits);
			ep.setMaxDuration(null);
			Long maxDurationUnitsL = dataElement.getMaxDurationUnits();
			TimeUnit maxDurationUnits;
			if (maxDurationUnitsL != null) {
				maxDurationUnits = timeUnitDao.retrieve(maxDurationUnitsL);
			} else {
				maxDurationUnits = timeUnitDao.getByName(
						DataElement.DEFAULT_TIME_UNIT_NAME);
			}
			ep.setMaxDurationTimeUnit(maxDurationUnits);
		}
		if (dataElement.getHasPropertyConstraint() == Boolean.TRUE) {
			PropertyConstraint pc = ep.getPropertyConstraint();
			if (pc == null) {
				pc = new PropertyConstraint();
			}
			pc.setPropertyName(dataElement.getProperty());
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
		dataElement.setAbbrevDisplayName(proposition.getAbbrevDisplayName());
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
				translatorSupport.getSystemEntityInstance(userId, 
				dataElement.getDataElementKey());
		populateExtendedProposition(ep, proposition, dataElement, timeUnitDao, valCompDao);

		return ep;
	}
	
	static DataElementField createDataElementField(ExtendedDataElement ep) {
		DataElementField dataElement = new DataElementField();
		dataElement.setDataElementKey(ep.getDataElementEntity().getKey());
		dataElement.setDataElementAbbrevDisplayName(ep.getDataElementEntity()
				.getAbbrevDisplayName());
		dataElement.setDataElementDisplayName(ep.getDataElementEntity()
				.getDisplayName());
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
