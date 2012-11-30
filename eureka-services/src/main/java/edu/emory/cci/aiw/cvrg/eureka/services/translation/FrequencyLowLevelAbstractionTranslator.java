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

import java.util.Collections;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Frequency;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LowLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;

public final class FrequencyLowLevelAbstractionTranslator implements
        PropositionTranslator<Frequency, LowLevelAbstraction> {

	private Long userId;
	private final PropositionDao propositionDao;

	@Inject
	public FrequencyLowLevelAbstractionTranslator(
	        PropositionDao inPropositionDao) {
		propositionDao = inPropositionDao;
	}

	public void setUserId(Long inUserId) {
		this.userId = inUserId;
	}

	@Override
	public LowLevelAbstraction translateFromElement(Frequency element) {
		LowLevelAbstraction result = new LowLevelAbstraction();

		PropositionTranslatorUtil.populateCommonPropositionFields(result,
		        element);
		result.setMinValues(element.getAtLeast());
		result.setMinGapValues(element.getWithinAtLeast());
		TimeUnit minGapValuesUnits = new TimeUnit();
		minGapValuesUnits.setName(element.getWithinAtLeastUnits());
		result.setMinGapValuesUnits(minGapValuesUnits);
		result.setMaxGapValues(element.getWithinAtMost());
		TimeUnit maxGapValuesUnits = new TimeUnit();
		maxGapValuesUnits.setName(element.getWithinAtMostUnits());
		result.setMaxGapValuesUnits(maxGapValuesUnits);

		result.setAbstractedFrom(Collections.singletonList(propositionDao.getByUserAndKey(
		        userId, element.getDataElement().getDataElementKey())));

		return result;
	}

	@Override
	public Frequency translateFromProposition(LowLevelAbstraction proposition) {
		Frequency result = new Frequency();

		PropositionTranslatorUtil.populateCommonDataElementFields(result, proposition);
		result.setAtLeast(proposition.getMinValues());
		result.setWithinAtLeast(proposition.getMinGapValues());
		result.setWithinAtLeastUnits(proposition.getMinGapValuesUnits().getName());
		result.setWithinAtMost(proposition.getMaxGapValues());
		result.setWithinAtMostUnits(proposition.getMaxGapValuesUnits().getName());

		DataElementField dataElement = new DataElementField();
		dataElement.setDataElementKey(proposition.getAbstractedFrom().get(0).getKey());
		result.setDataElement(dataElement);

		return result;
	}

}
