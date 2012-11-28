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

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Frequency;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SliceAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

public final class FrequencySliceTranslator implements
        PropositionTranslator<Frequency, SliceAbstraction> {

	private Long userId;
	private final PropositionDao propositionDao;
	private final TimeUnitDao timeUnitDao;
	private final SystemPropositionFinder finder;
	
	@Inject
	public FrequencySliceTranslator(PropositionDao inPropositionDao,
	        TimeUnitDao inTimeUnitDao, SystemPropositionFinder inFinder) {
		propositionDao = inPropositionDao;
		timeUnitDao = inTimeUnitDao;
		finder = inFinder;
	}

	public void setUserId(Long inUserId) {
		this.userId = inUserId;
	}

	@Override
	public SliceAbstraction translateFromElement(Frequency element) {
		SliceAbstraction result = new SliceAbstraction();

		PropositionTranslatorUtil.populateCommonPropositionFields(result,
		        element);
		result.setMinIndex(element.getAtLeast());
		result.setExtendedProposition(PropositionTranslatorUtil
		        .createExtendedProposition(element.getDataElement(), userId,
		                propositionDao, timeUnitDao));
		result.setInSystem(false);

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

		return result;
	}

}
