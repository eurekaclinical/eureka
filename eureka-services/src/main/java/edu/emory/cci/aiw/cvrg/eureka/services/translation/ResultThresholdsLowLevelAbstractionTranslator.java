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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ShortDataElementField;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThresholds;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThreshold;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SimpleParameterConstraint;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ThresholdsOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PropositionUtil;
import org.protempa.PropositionDefinition;

/**
 *
 */
public class ResultThresholdsLowLevelAbstractionTranslator implements
		PropositionTranslator<ValueThresholds, ValueThresholdEntity> {

	private Long userId;
	private final PropositionDao propositionDao;
	private final TimeUnitDao timeUnitDao;
	private final ValueComparatorDao valueCompDao;
	private final ThresholdsOperatorDao thresholdsOpDao;
	private final SystemPropositionFinder finder;

	@Inject
	public ResultThresholdsLowLevelAbstractionTranslator(
			PropositionDao inPropositionDao, TimeUnitDao inTimeUnitDao,
			ValueComparatorDao inValueComparatorDao,
			ThresholdsOperatorDao thresholdsOperatorDao,
			SystemPropositionFinder inFinder) {
		propositionDao = inPropositionDao;
		timeUnitDao = inTimeUnitDao;
		valueCompDao = inValueComparatorDao;
		thresholdsOpDao = thresholdsOperatorDao;
		finder = inFinder;
	}

	public void setUserId(Long inUserId) {
		userId = inUserId;
	}

	@Override
	public ValueThresholdEntity translateFromElement(ValueThresholds element)
			throws DataElementHandlingException {
		if (element == null) {
			throw new IllegalArgumentException("element cannot be null");
		}
		
		ValueThresholdEntity result = new ValueThresholdEntity();
		result.setInSystem(false);

		PropositionTranslatorUtil.populateCommonPropositionFields(result,
				element);
		String elementName = element.getName();
		if (elementName != null) {
			result.setName(elementName);
		} else {
			throw new DataElementHandlingException(
					"Could not translate data element "
					+ element.getKey()
					+ ": no name was specified");
		}
		// low-level abstractions created from results thresholds are based on
		// only one element
		ValueThreshold threshold = element.getValueThresholds().get(0);
		createAndSetConstraints(result, threshold, valueCompDao);

		Proposition abstractedFrom = propositionDao.getByUserAndKey(element.getUserId(),
				threshold.getDataElement().getDataElementKey());
		if (abstractedFrom == null) {
			try {
				PropositionDefinition propDef = this.finder
						.find(element.getUserId(), threshold.getDataElement()
						.getDataElementKey());
				abstractedFrom = PropositionUtil.toSystemProposition(propDef, element
						.getUserId());
			} catch (PropositionFindException ex) {
				throw new DataElementHandlingException(
						"Could not translate data element "
						+ element.getKey(), ex);
			}
		}
		result.setAbstractedFrom(abstractedFrom);
		Long thresholdsOpId = element.getThresholdsOperator();
		if (thresholdsOpId != null) {
			result.setThresholdsOperator(
					thresholdsOpDao.retrieve(thresholdsOpId));
		} else {
			throw new DataElementHandlingException(
					"Could not translate data element "
					+ element.getKey()
					+ ": no thresholds operator was specified");
		}
		result.setCreatedFrom(ValueThresholdEntity.CreatedFrom.VALUE_THRESHOLD);

		return result;
	}

	static void createAndSetConstraints(ValueThresholdEntity abstraction,
			ValueThreshold threshold, ValueComparatorDao valueCompDao) {
		SimpleParameterConstraint userConstraint = new SimpleParameterConstraint();
		SimpleParameterConstraint complementConstraint = new SimpleParameterConstraint();

		if (threshold.getLowerValue() != null
				&& threshold.getLowerComp() != null) {
			ValueComparator lowerComp = valueCompDao.retrieve(threshold
					.getLowerComp());

			userConstraint.setMinValueThreshold(threshold.getLowerValue());
			userConstraint.setMinValueComp(lowerComp);
			userConstraint.setMinUnits(threshold.getLowerUnits());

			complementConstraint
					.setMinValueThreshold(threshold.getLowerValue());
			complementConstraint.setMinValueComp(lowerComp.getComplement());
			complementConstraint.setMinUnits(threshold.getLowerUnits());
		}

		if (threshold.getUpperValue() != null
				&& threshold.getUpperComp() != null) {
			ValueComparator upperComp = valueCompDao.retrieve(threshold
					.getUpperComp());

			userConstraint.setMaxValueThreshold(threshold.getUpperValue());
			userConstraint.setMaxValueComp(upperComp);
			userConstraint.setMaxUnits(threshold.getUpperUnits());

			complementConstraint
					.setMaxValueThreshold(threshold.getUpperValue());
			complementConstraint.setMaxValueComp(upperComp.getComplement());
			complementConstraint.setMaxUnits(threshold.getUpperUnits());

		}
		abstraction.setUserConstraint(userConstraint);
		abstraction.setComplementConstraint(complementConstraint);
	}

	@Override
	public ValueThresholds translateFromProposition(
			ValueThresholdEntity valThresholdEntity) {
		ValueThreshold threshold = new ValueThreshold();
		ShortDataElementField de = new ShortDataElementField();
		de.setDataElementKey(valThresholdEntity.getAbstractedFrom().getKey());
		de.setDataElementDisplayName(valThresholdEntity.getAbstractedFrom()
				.getDisplayName());
		de.setDataElementAbbrevDisplayName(valThresholdEntity
				.getAbstractedFrom().getAbbrevDisplayName());
		threshold.setDataElement(de);
		List<ValueThreshold> thresholds = new ArrayList<ValueThreshold>();
		SimpleParameterConstraint userConstraint = valThresholdEntity
				.getUserConstraint();
		if (userConstraint != null) {
			if (userConstraint.getMaxValueThreshold() != null
					&& userConstraint.getMaxValueComp() != null) {
				threshold.setUpperValue(userConstraint.getMaxValueThreshold());
				// threshold.setUpperUnits(userConstraint.getMaxUnits());
				threshold
						.setUpperComp(userConstraint.getMaxValueComp().getId());
			}
			if (userConstraint.getMinValueThreshold() != null
					&& userConstraint.getMinValueComp() != null) {
				threshold.setLowerValue(userConstraint.getMinValueThreshold());
				// threshold.setLowerUnits(userConstraint.getMinUnits());
				threshold
						.setLowerComp(userConstraint.getMinValueComp().getId());
			}
		}
		thresholds.add(threshold);

		ValueThresholds result = new ValueThresholds();
		PropositionTranslatorUtil.populateCommonDataElementFields(result,
				valThresholdEntity);
		result.setName(valThresholdEntity.getName());
		result.setThresholdsOperator(
				valThresholdEntity.getThresholdsOperator().getId());
		result.setValueThresholds(thresholds);
		return result;
	}
}
