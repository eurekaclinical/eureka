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

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.ResultThresholds;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThreshold;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LowLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LowLevelAbstraction.CreatedFrom;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SimpleParameterConstraint;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;

public final class ResultThresholdsTranslator implements
        PropositionTranslator<ResultThresholds, LowLevelAbstraction> {

	private Long userId;
	private final PropositionDao propositionDao;
	private final TimeUnitDao timeUnitDao;
	private final ValueComparatorDao valueCompDao;

	@Inject
	public ResultThresholdsTranslator(PropositionDao inPropositionDao,
	        TimeUnitDao inTimeUnitDao, ValueComparatorDao inValueComparatorDao) {
		propositionDao = inPropositionDao;
		timeUnitDao = inTimeUnitDao;
		valueCompDao = inValueComparatorDao;
	}

	public void setUserId(Long inUserId) {
		userId = inUserId;
	}

	@Override
	public LowLevelAbstraction translateFromElement(ResultThresholds element) {
		LowLevelAbstraction result = new LowLevelAbstraction();

		PropositionTranslatorUtil.populateCommonPropositionFields(result,
		        element);
		List<Proposition> abstractedFrom = new ArrayList<Proposition>();
		List<SimpleParameterConstraint> spcs = new ArrayList<SimpleParameterConstraint>();
		for (ValueThreshold vt : element.getValueThresholds()) {
			abstractedFrom.add(propositionDao.getByUserAndKey(userId, vt.getDataElementKey()));
			spcs.add(buildSimpleParameterConstraint(vt));
		}
		result.setAbstractedFrom(abstractedFrom);
		result.setSimpleParameterConstraints(spcs);
		result.setCreatedFrom(CreatedFrom.VALUE_THRESHOLD);

		return result;
	}

	private SimpleParameterConstraint buildSimpleParameterConstraint(ValueThreshold threshold) {
		SimpleParameterConstraint result = new SimpleParameterConstraint();

		result.setMinValueThreshold(threshold.getLowerValue());
		result.setMinValueComp(valueCompDao.retrieve(threshold.getLowerComp()));
		result.setMinUnits(threshold.getLowerUnits());
		
		result.setMaxValueThreshold(threshold.getUpperValue());
		result.setMaxValueComp(valueCompDao.retrieve(threshold.getUpperComp()));
		result.setMaxUnits(threshold.getUpperUnits());
		
		return result;
	}

	@Override
	public ResultThresholds translateFromProposition(
	        LowLevelAbstraction proposition) {
		// TODO Auto-generated method stub
		return null;
	}

}
