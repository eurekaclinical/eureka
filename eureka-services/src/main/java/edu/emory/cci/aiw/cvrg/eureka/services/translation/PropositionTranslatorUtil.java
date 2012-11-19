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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;

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
		proposition.setKey(dataElement.getKey());
		proposition.setDisplayName(dataElement.getDisplayName());
		proposition.setAbbrevDisplayName(dataElement.getAbbrevDisplayName());
		proposition.setCreated(dataElement.getCreated());
		proposition.setLastModified(dataElement.getLastModified());
		proposition.setUserId(dataElement.getUserId());
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
}
