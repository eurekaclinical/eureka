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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Frequency;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SliceAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.exception
		.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

public final class FrequencySliceTranslator implements
        PropositionTranslator<Frequency, SliceAbstraction> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger
			(FrequencySliceTranslator.class);
	
	private final PropositionDao propositionDao;
	private final TimeUnitDao timeUnitDao;
	private final SystemPropositionFinder systemPropositionFinder;

	@Inject
	public FrequencySliceTranslator(PropositionDao inPropositionDao,
	        TimeUnitDao inTimeUnitDao, SystemPropositionFinder inFinder) {
		this.propositionDao = inPropositionDao;
		this.timeUnitDao = inTimeUnitDao;
		this.systemPropositionFinder = inFinder;
	}

	@Override
	public SliceAbstraction translateFromElement(Frequency element) {
		SliceAbstraction result = new SliceAbstraction();

		PropositionTranslatorUtil.populateCommonPropositionFields(result,
		        element);
		result.setMinIndex(element.getAtLeast());
		try {
			result.setExtendedProposition(PropositionTranslatorUtil
			        .createExtendedProposition(
					        element.getDataElement(), element.getUserId(), 
					        propositionDao, timeUnitDao, 
					        this.systemPropositionFinder));
		} catch (PropositionFindException e) {
			LOGGER.error(e.getMessage(), e);
			throw new IllegalArgumentException("Frequency contains an " +
					"unknown data element", e);
		} catch (DataElementHandlingException e) {
			LOGGER.error(e.getMessage(), e);
			throw new IllegalArgumentException(e);
		}
		result.setInSystem(false);
		
		if (element.getIsWithin()) {
			result.setWithinAtLeast(element.getWithinAtLeast());
			result.setWithinAtLeastUnits(this.timeUnitDao.retrieve(element.getWithinAtLeastUnits()));
			result.setWithinAtMost(element.getWithinAtMost());
			result.setWithinAtMostUnits(this.timeUnitDao.retrieve(element.getWithinAtMostUnits()));
		} else {
			result.setWithinAtLeast(null);
			result.setWithinAtLeastUnits(this.timeUnitDao.retrieve(element.getWithinAtLeastUnits()));
			result.setWithinAtMost(null);
			result.setWithinAtMostUnits(this.timeUnitDao.retrieve(element.getWithinAtMostUnits()));
		}

		return result;
	}

	@Override
	public Frequency translateFromProposition(SliceAbstraction proposition) {
		Frequency result = new Frequency();

		PropositionTranslatorUtil.populateCommonDataElementFields(result,
		        proposition);
		result.setAtLeast(proposition.getMinIndex());
		result.setDataElement(PropositionTranslatorUtil
		        .createDataElementField(proposition.getExtendedProposition()));
		
		result.setWithinAtLeast(proposition.getWithinAtLeast());
		result.setWithinAtLeastUnits(proposition.getWithinAtLeastUnits().getId());
		result.setWithinAtMost(proposition.getWithinAtMost());
		result.setWithinAtMostUnits(proposition.getWithinAtMostUnits().getId());
		if (result.getWithinAtLeast() != null || result.getWithinAtMost() != null) {
			result.setIsWithin(true);
		}

		return result;
	}

}
