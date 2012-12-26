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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ResultThresholds;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThreshold;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CompoundLowLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CompoundLowLevelAbstraction.CreatedFrom;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LowLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class ResultThresholdsCompoundLowLevelAbstractionTranslator implements
		PropositionTranslator<ResultThresholds, CompoundLowLevelAbstraction> {

	private static final String SYNTH_LLA_SUFFIX = "_CLASSIFICATION";
	public static final String COMP_DEF_SUFFIX = "_COMP";

	private Long userId;
	private final PropositionDao propositionDao;
	private final TimeUnitDao timeUnitDao;
	private final ValueComparatorDao valueCompDao;

	@Inject
	public ResultThresholdsCompoundLowLevelAbstractionTranslator(
			PropositionDao inPropositionDao,
			TimeUnitDao inTimeUnitDao,
			ValueComparatorDao inValueComparatorDao) {

		propositionDao = inPropositionDao;
		timeUnitDao = inTimeUnitDao;
		valueCompDao = inValueComparatorDao;
	}

	public void setUserId(Long inUserId) {
		userId = inUserId;
	}

	@Override
	public CompoundLowLevelAbstraction translateFromElement(ResultThresholds
																	element)
			throws DataElementHandlingException {
		CompoundLowLevelAbstraction result = new CompoundLowLevelAbstraction();
		result.setInSystem(false);

		PropositionTranslatorUtil.populateCommonPropositionFields(result,
				element);
		List<LowLevelAbstraction> abstractedFrom = new ArrayList<LowLevelAbstraction>();

		result.setUserValueDefinitionName(element.getName());
		result.setComplementValueDefinitionName(element.getName() +
				COMP_DEF_SUFFIX);

		for (ValueThreshold vt : element.getValueThresholds()) {
			abstractedFrom.add(createIntermediateAbstraction(vt));
		}

		result.setAbstractedFrom(abstractedFrom);
		result.setCreatedFrom(CreatedFrom.RESULTS_THRESHOLD);

		return result;
	}

	private LowLevelAbstraction createIntermediateAbstraction
			(ValueThreshold threshold) throws DataElementHandlingException {
		LowLevelAbstraction result = new LowLevelAbstraction();
		Date now = Calendar.getInstance().getTime();
		result.setDisplayName(threshold.getDataElementKey() + SYNTH_LLA_SUFFIX);
		result.setAbbrevDisplayName(result.getDisplayName());
		result.setCreated(now);
		result.setLastModified(now);
		result.setUserId(userId);
		result.setKey(result.getDisplayName());
		result.setCreatedFrom(LowLevelAbstraction.CreatedFrom.VALUE_THRESHOLD);

		result.setAbstractedFrom(Collections.singletonList(propositionDao.getByUserAndKey
				(userId,
						threshold.getDataElementKey())));

		ResultThresholdsLowLevelAbstractionTranslator.createAndSetConstraints
				(result, threshold, valueCompDao);

		this.propositionDao.create(result);
		this.propositionDao.refresh(result);

		return result;
	}


	@Override
	public ResultThresholds translateFromProposition(
			CompoundLowLevelAbstraction proposition) {
		// TODO Auto-generated method stub
		return null;
	}

}
