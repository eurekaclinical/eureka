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
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.ShortDataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThreshold;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThresholds;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CompoundValueThreshold;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CompoundValueThreshold.CreatedFrom;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SimpleParameterConstraint;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ThresholdsOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ThresholdsOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PropositionUtil;

public final class ResultThresholdsCompoundLowLevelAbstractionTranslator
        implements
        PropositionTranslator<ValueThresholds, CompoundValueThreshold> {

	private static final String SYNTH_LLA_SUFFIX = "_CLASSIFICATION";
	public static final String COMP_DEF_SUFFIX = "_COMP";

	private Long userId;
	private final PropositionDao propositionDao;
	private final TimeUnitDao timeUnitDao;
	private final ValueComparatorDao valueCompDao;
	private final ThresholdsOperatorDao thresholdsOperatorDao;
	private final SystemPropositionFinder finder;

	@Inject
	public ResultThresholdsCompoundLowLevelAbstractionTranslator(
	        PropositionDao inPropositionDao, TimeUnitDao inTimeUnitDao,
	        ValueComparatorDao inValueComparatorDao,
	        ThresholdsOperatorDao inThresholdsOperatorDao,
	        SystemPropositionFinder inFinder) {

		propositionDao = inPropositionDao;
		timeUnitDao = inTimeUnitDao;
		valueCompDao = inValueComparatorDao;
		thresholdsOperatorDao = inThresholdsOperatorDao;
		finder = inFinder;
	}

	public void setUserId(Long inUserId) {
		userId = inUserId;
	}

	@Override
	public CompoundValueThreshold translateFromElement(ValueThresholds element)
	        throws DataElementHandlingException {
		CompoundValueThreshold result = new CompoundValueThreshold();
		result.setInSystem(false);

		PropositionTranslatorUtil.populateCommonPropositionFields(result,
		        element);
		List<ValueThresholdEntity> abstractedFrom = new ArrayList<ValueThresholdEntity>();

		result.setUserValueDefinitionName(element.getName());
		result.setComplementValueDefinitionName(element.getName()
		        + COMP_DEF_SUFFIX);

		result.setThresholdsOperator(thresholdsOperatorDao.retrieve(element
		        .getThresholdsOperator()));
		for (ValueThreshold vt : element.getValueThresholds()) {
			abstractedFrom.add(createIntermediateAbstraction(vt));
		}

		result.setAbstractedFrom(abstractedFrom);
		result.setCreatedFrom(CreatedFrom.RESULTS_THRESHOLD);

		return result;
	}

	private ValueThresholdEntity createIntermediateAbstraction(
	        ValueThreshold threshold) throws DataElementHandlingException {
		ValueThresholdEntity result = new ValueThresholdEntity();
		Date now = new Date();
		result.setDisplayName(threshold.getDataElement().getDataElementKey()
		        + SYNTH_LLA_SUFFIX);
		result.setAbbrevDisplayName(result.getDisplayName());
		result.setCreated(now);
		result.setLastModified(now);
		result.setUserId(userId);
		result.setKey(result.getDisplayName());
		result.setCreatedFrom(
				ValueThresholdEntity.CreatedFrom.VALUE_THRESHOLD);
		result.setThresholdsOperator(thresholdsOperatorDao.getByName("any"));
		result.setName(result.getKey());

		Proposition abstractedFrom = propositionDao.getByUserAndKey(userId,
		        threshold.getDataElement().getDataElementKey());
		if (abstractedFrom == null) {
			try {
				SystemElement element = PropositionUtil.wrap(
				        this.finder.find(userId, threshold.getDataElement()
				                .getDataElementKey()), true, userId,
				        this.finder);
				SystemPropositionTranslator translator = new SystemPropositionTranslator(finder);
				abstractedFrom = translator.translateFromElement(element);
			} catch (PropositionFindException ex) {
				throw new DataElementHandlingException(
				        "Could not translate data element "
				                + threshold.getDataElement()
				                        .getDataElementKey(), ex);
			}
		}

		result.setAbstractedFrom(abstractedFrom);

		ResultThresholdsLowLevelAbstractionTranslator.createAndSetConstraints(
		        result, threshold, valueCompDao);

		this.propositionDao.create(result);
		this.propositionDao.refresh(result);

		return result;
	}

	@Override
	public ValueThresholds translateFromProposition(
	        CompoundValueThreshold proposition) {
		ValueThresholds result = new ValueThresholds();
		PropositionTranslatorUtil.populateCommonDataElementFields(result,
		        proposition);
		result.setName(proposition.getUserValueDefinitionName());

		List<ValueThreshold> thresholds = new ArrayList<ValueThreshold>();
		for (ValueThresholdEntity vte : proposition.getAbstractedFrom()) {
			ValueThreshold threshold = new ValueThreshold();
			ShortDataElementField de = new ShortDataElementField();
			de.setDataElementKey(vte.getAbstractedFrom().getKey());
			de.setDataElementDisplayName(vte.getAbstractedFrom().getDisplayName());
			de.setDataElementAbbrevDisplayName(vte.getAbstractedFrom().getAbbrevDisplayName());
			threshold.setDataElement(de);
			SimpleParameterConstraint userConstraint = vte.getUserConstraint();
			if (userConstraint != null) {
				if (userConstraint.getMinValueThreshold() != null
				        && userConstraint.getMinValueComp() != null) {
//				        && userConstraint.getMinUnits() != null) {
//					units not in use yet
					threshold.setLowerValue(userConstraint
					        .getMinValueThreshold());
					threshold.setLowerComp(userConstraint.getMinValueComp()
					        .getId());
//					threshold.setLowerUnits(userConstraint.getMinUnits());
				}
				if (userConstraint.getMaxValueThreshold() != null
				        && userConstraint.getMaxValueComp() != null) {
//				        && userConstraint.getMaxUnits() != null) {
//					units not in use yet
					threshold.setUpperValue(userConstraint
					        .getMaxValueThreshold());
					threshold.setUpperComp(userConstraint.getMaxValueComp()
					        .getId());
//					threshold.setUpperUnits(userConstraint.getMaxUnits());
				}
			}
			thresholds.add(threshold);
		}
		result.setValueThresholds(thresholds);
		result.setThresholdsOperator(proposition.getThresholdsOperator()
		        .getId());

		return result;
	}

}
