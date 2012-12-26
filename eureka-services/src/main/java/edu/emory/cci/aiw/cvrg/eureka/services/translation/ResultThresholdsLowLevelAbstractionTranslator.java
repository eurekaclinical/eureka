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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LowLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SimpleParameterConstraint;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;

import java.util.Collections;

/**
 *
 */
public class ResultThresholdsLowLevelAbstractionTranslator implements
		PropositionTranslator<ResultThresholds, LowLevelAbstraction> {

	private Long userId;
	private final PropositionDao propositionDao;
	private final TimeUnitDao timeUnitDao;
	private final ValueComparatorDao valueCompDao;

	@Inject
	public ResultThresholdsLowLevelAbstractionTranslator(PropositionDao inPropositionDao,
														 TimeUnitDao inTimeUnitDao, ValueComparatorDao inValueComparatorDao) {
		propositionDao = inPropositionDao;
		timeUnitDao = inTimeUnitDao;
		valueCompDao = inValueComparatorDao;
	}

	public void setUserId(Long inUserId) {
		userId = inUserId;
	}

	@Override
	public LowLevelAbstraction translateFromElement(ResultThresholds element) throws DataElementHandlingException {
		LowLevelAbstraction result = new LowLevelAbstraction();
		result.setInSystem(false);

		PropositionTranslatorUtil.populateCommonPropositionFields(result,
				element);
		// low-level abstractions created from results thresholds are based on
		// only one element
		ValueThreshold threshold = element.getValueThresholds().get(0);
		createAndSetConstraints(result, threshold, valueCompDao);

		Proposition abstractedFrom = propositionDao.getByUserAndKey(userId,
				threshold.getDataElementKey());
		result.setAbstractedFrom(Collections.singletonList(abstractedFrom));
		result.setCreatedFrom(LowLevelAbstraction.CreatedFrom.VALUE_THRESHOLD);

		return result;
	}

	static void createAndSetConstraints(LowLevelAbstraction abstraction,
										ValueThreshold threshold,
										ValueComparatorDao valueCompDao) {
		SimpleParameterConstraint userConstraint = new SimpleParameterConstraint();
		SimpleParameterConstraint complementConstraint = new
				SimpleParameterConstraint();

		if (threshold.getLowerValue() != null && threshold.getLowerComp() !=
				null) {
			ValueComparator lowerComp = valueCompDao.retrieve(threshold
					.getLowerComp());

			userConstraint.setMinValueThreshold(threshold.getLowerValue());
			userConstraint.setMinValueComp(lowerComp);
			userConstraint.setMinUnits(threshold.getLowerUnits());

			complementConstraint.setMaxValueThreshold(threshold.getLowerValue());
			complementConstraint.setMaxValueComp(lowerComp.getComplement());
			complementConstraint.setMinUnits(threshold.getLowerUnits());
		}

		if (threshold.getUpperValue() != null && threshold.getUpperComp() !=
				null) {
			ValueComparator upperComp = valueCompDao.retrieve(threshold
					.getUpperComp());

			userConstraint.setMaxValueThreshold(threshold.getUpperValue());
			userConstraint.setMaxValueComp(upperComp);
			userConstraint.setMaxUnits(threshold.getUpperUnits());

			complementConstraint.setMaxValueThreshold(threshold.getUpperValue());
			complementConstraint.setMaxValueComp(upperComp.getComplement());
			complementConstraint.setMaxUnits(threshold.getUpperUnits());

		}
		abstraction.setUserConstraint(userConstraint);
		abstraction.setComplementConstraint(complementConstraint);
	}

		@Override
		public ResultThresholds translateFromProposition (LowLevelAbstraction
		proposition){
			return null;  //To change body of implemented methods use File | Settings | File Templates.
		}
	}
