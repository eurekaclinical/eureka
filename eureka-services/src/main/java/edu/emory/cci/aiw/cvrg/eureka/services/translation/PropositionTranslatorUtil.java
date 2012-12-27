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

import org.protempa.PropositionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropertyConstraint;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.exception
		.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PropositionUtil;

/**
 * Contains common utility functions for all implementations of
 * {@link PropositionTranslator}.
 */
class PropositionTranslatorUtil {

	private PropositionTranslatorUtil() {
		// prevents instantiation
	}

	/**
	 * Populates the fields common to all propositions based on the given
	 * proposition.
	 * 
	 * @param proposition
	 *            the {@link Proposition} to populate. Modified as a result of
	 *            calling this method.
	 * @param dataElement
	 *            the {@link DataElement} to get the data from
	 */
	static void populateCommonPropositionFields(Proposition proposition,
	        DataElement dataElement) {
		proposition.setId(dataElement.getId());
		proposition.setDisplayName(dataElement.getDisplayName());
		proposition.setAbbrevDisplayName(dataElement.getAbbrevDisplayName());
		proposition.setCreated(dataElement.getCreated());
		proposition.setLastModified(dataElement.getLastModified());
		proposition.setUserId(dataElement.getUserId());

		if (dataElement.getKey() != null) {
			proposition.setKey(dataElement.getKey());
		} else {
			proposition.setKey(dataElement.getAbbrevDisplayName());
		}
	}

	/**
	 * Populates the fields common to all data elements based on the given
	 * proposition.
	 * 
	 * @param dataElement
	 *            the {@link DataElement} to populate. Modified as a result of
	 *            calling this method.
	 * @param proposition
	 *            the {@link Proposition} to get the data from
	 */
	static void populateCommonDataElementFields(DataElement dataElement,
	        Proposition proposition) {
		dataElement.setId(proposition.getId());
		dataElement.setKey(proposition.getKey());
		dataElement.setDisplayName(proposition.getDisplayName());
		dataElement.setAbbrevDisplayName(proposition.getAbbrevDisplayName());
		dataElement.setCreated(proposition.getCreated());
		dataElement.setLastModified(proposition.getLastModified());
		dataElement.setUserId(proposition.getUserId());
	}

	static ExtendedProposition createExtendedProposition(
	        DataElementField dataElement, Long userId, 
			PropositionDao propositionDao, TimeUnitDao timeUnitDao, 
			SystemPropositionFinder inFinder) throws 
			PropositionFindException, DataElementHandlingException {

		ExtendedProposition ep = new ExtendedProposition();
		Proposition proposition = propositionDao.getByUserAndKey(
				userId, dataElement.getDataElementKey());
		if (proposition == null) {
			PropositionDefinition definition = inFinder.find(userId, 
					dataElement.getDataElementKey
					());
			SystemElement element = PropositionUtil.wrap(definition, false, 
					userId, inFinder);
			SystemPropositionTranslator translator = new 
					SystemPropositionTranslator(inFinder);
			proposition = translator.translateFromElement(element);
		}
		ep.setProposition(proposition);
		if (dataElement.getHasDuration()) {
			ep.setMinDuration(dataElement.getMinDuration());
			ep.setMinDurationTimeUnit(timeUnitDao.retrieve(dataElement.getMinDurationUnits()));
			ep.setMaxDuration(dataElement.getMaxDuration());
			ep.setMaxDurationTimeUnit(timeUnitDao.retrieve(dataElement.getMaxDurationUnits()));
		} else {
			ep.setMinDuration(null);
			ep.setMinDurationTimeUnit(timeUnitDao.retrieve(dataElement.getMinDurationUnits()));
			ep.setMaxDuration(null);
			ep.setMaxDurationTimeUnit(timeUnitDao.retrieve(dataElement.getMaxDurationUnits()));
		}
		if (dataElement.getHasPropertyConstraint()) {
			PropertyConstraint pc = new PropertyConstraint();
			pc.setPropertyName(dataElement.getProperty());
			pc.setValue(dataElement.getPropertyValue());
			ValueComparator vc = new ValueComparator();
			vc.setName("EQUAL_TO");
			ep.setPropertyConstraint(pc);
		} else {
			ep.setPropertyConstraint(null);
		}

		return ep;
	}
	
	static DataElementField createDataElementField(ExtendedProposition ep) {
		DataElementField dataElement = new DataElementField();
		dataElement.setDataElementKey(ep.getProposition().getKey());
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
