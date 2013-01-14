/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThreshold;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThresholds;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ThresholdsOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;

public final class ValueThresholdsTranslator implements
        PropositionTranslator<ValueThresholds, ValueThresholdGroupEntity> {

	private final TranslatorSupport translatorSupport;
	private final ValueComparatorDao valueCompDao;
	private final ThresholdsOperatorDao thresholdsOperatorDao;

	@Inject
	public ValueThresholdsTranslator(ValueComparatorDao inValueComparatorDao,
	        ThresholdsOperatorDao inThresholdsOperatorDao,
	        TranslatorSupport inSupport) {
		valueCompDao = inValueComparatorDao;
		thresholdsOperatorDao = inThresholdsOperatorDao;
		translatorSupport = inSupport;
	}

	@Override
	public ValueThresholdGroupEntity translateFromElement(
	        ValueThresholds element) throws DataElementHandlingException {
		ValueThresholdGroupEntity result = new ValueThresholdGroupEntity();

		PropositionTranslatorUtil.populateCommonEntityFields(result, element);

		result.setThresholdsOperator(thresholdsOperatorDao.retrieve(element
		        .getThresholdsOperator()));
		List<ValueThresholdEntity> thresholds = new ArrayList<ValueThresholdEntity>();
		for (ValueThreshold vt : element.getValueThresholds()) {
			ValueThresholdEntity vte = new ValueThresholdEntity();
			vte.setAbstractedFrom(translatorSupport.getSystemEntityInstance(
			        element.getUserId(), vt.getDataElement()
			                .getDataElementKey()));

			vte.setMinValueThreshold(vt.getLowerValue());
			vte.setMinValueComp(valueCompDao.retrieve(vt.getLowerComp()));
			// vte.setMinUnits(vt.getLowerUnits());

			vte.setMaxValueThreshold(vt.getUpperValue());
			vte.setMaxValueComp(valueCompDao.retrieve(vt.getUpperComp()));
			// vte.setMaxUnits(vt.getUpperUnits());

			thresholds.add(vte);
		}
		result.setValueThresholds(thresholds);

		return result;
	}

	@Override
	public ValueThresholds translateFromProposition(
	        ValueThresholdGroupEntity entity) {
		ValueThresholds result = new ValueThresholds();

		PropositionTranslatorUtil.populateCommonDataElementFields(result,
		        entity);

		result.setThresholdsOperator(entity.getThresholdsOperator().getId());

		List<ValueThreshold> thresholds = new ArrayList<ValueThreshold>();
		for (ValueThresholdEntity vte : entity.getValueThresholds()) {
			ValueThreshold threshold = new ValueThreshold();
			threshold.setLowerValue(vte.getMinValueThreshold());
			threshold.setLowerComp(vte.getMinValueComp().getId());
			// threshold.setLowerUnits(vte.getMinUnits());

			threshold.setUpperValue(vte.getMaxValueThreshold());
			threshold.setUpperComp(vte.getMaxValueComp().getId());
			// threshold.setUpperUnits(vte.getMaxUnits());

			DataElementEntity dataElementEntity = vte.getAbstractedFrom();
			ShortDataElementField elementField = new ShortDataElementField();
			elementField.setDataElementAbbrevDisplayName(dataElementEntity
					.getAbbrevDisplayName());
			elementField.setDataElementDisplayName(dataElementEntity
					.getDisplayName());
			elementField.setDataElementKey(dataElementEntity.getKey());
			threshold.setDataElement(elementField);

			thresholds.add(threshold);
		}
		result.setValueThresholds(thresholds);

		return result;
	}

}
