/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2015 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThreshold;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThresholds;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.*;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ThresholdsOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;

public final class ValueThresholdsTranslator implements
		PropositionTranslator<ValueThresholds, ValueThresholdGroupEntity> {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			ValueThresholdsTranslator.class);
	private final TranslatorSupport translatorSupport;
	private final ValueComparatorDao valueCompDao;
	private final ThresholdsOperatorDao thresholdsOperatorDao;
	private final TimeUnitDao timeUnitDao;
	private final RelationOperatorDao relationOpDao;

	@Inject
	public ValueThresholdsTranslator(ValueComparatorDao inValueComparatorDao,
			ThresholdsOperatorDao inThresholdsOperatorDao,
			TranslatorSupport inSupport,
			TimeUnitDao inTimeUnitDao,
			RelationOperatorDao inRelationOpDao) {
		valueCompDao = inValueComparatorDao;
		thresholdsOperatorDao = inThresholdsOperatorDao;
		translatorSupport = inSupport;
		timeUnitDao = inTimeUnitDao;
		relationOpDao = inRelationOpDao;
	}

	@Override
	public ValueThresholdGroupEntity translateFromElement(
			ValueThresholds element) throws DataElementHandlingException {
		if (element == null) {
			throw new IllegalArgumentException("element cannot be null");
		}

		ValueThresholdGroupEntity result =
				this.translatorSupport.getUserEntityInstance(element,
				ValueThresholdGroupEntity.class);

		result.setThresholdsOperator(thresholdsOperatorDao.retrieve(element
				.getThresholdsOperator()));

		List<ValueThresholdEntity> thresholds = result.getValueThresholds();
		if (thresholds == null) {
			thresholds = new ArrayList<>();
			result.setValueThresholds(thresholds);
		}

		int i = 0;
		for (ValueThreshold vt : element.getValueThresholds()) {
			ValueThresholdEntity vte;
			if (thresholds.size() > i) {
				vte = thresholds.get(i);
			} else {
				vte = new ValueThresholdEntity();
				thresholds.add(vte);
			}
			vte.setAbstractedFrom(translatorSupport.getUserOrSystemEntityInstance(
					element.getUserId(), vt.getDataElement()
					.getDataElementKey()));

			String lowerValue = vt.getLowerValue();
			vte.setMinTValueThreshold(lowerValue);
			if (lowerValue != null && !lowerValue.isEmpty()) {
				try {
					vte.setMinValueThreshold(new BigDecimal(lowerValue));
				} catch (NumberFormatException ex) {
					LOGGER.debug("Could not parse " + lowerValue
							+ " as a BigDecimal", ex);
				}
			}
			vte.setMinValueComp(valueCompDao.retrieve(vt.getLowerComp()));
			// vte.setMinUnits(vt.getLowerUnits());

			String upperValue = vt.getUpperValue();
			vte.setMaxTValueThreshold(upperValue);
			if (upperValue != null && !upperValue.isEmpty()) {
				try {
					vte.setMaxValueThreshold(new BigDecimal(upperValue));
				} catch (NumberFormatException ex) {
					LOGGER.debug("Could not parse " + upperValue
							+ " as a BigDecimal", ex);
				}
			}
			vte.setMaxValueComp(valueCompDao.retrieve(vt.getUpperComp()));
			// vte.setMaxUnits(vt.getUpperUnits());
			List<ExtendedDataElement> extendedDataElements =
					vte.getExtendedDataElements();

			vte.setRelationOperator(
					relationOpDao.retrieve(vt.getRelationOperator()));
			vte.setWithinAtLeast(vt.getWithinAtLeast());
			vte.setWithinAtLeastUnits(
					timeUnitDao.retrieve(vt.getWithinAtLeastUnit()));
			vte.setWithinAtMost(vt.getWithinAtMost());
			vte.setWithinAtMostUnits(
					timeUnitDao.retrieve(vt.getWithinAtMostUnit()));

			if (extendedDataElements == null) {
				extendedDataElements = new ArrayList<>();
				vte.setExtendedDataElements(extendedDataElements);
			}
			int j = 0;
			for (DataElementField de : vt.getRelatedDataElements()) {
				ExtendedDataElement ede = null;
				if (extendedDataElements.size() > j) {
					ede = extendedDataElements.get(j);
					PropositionTranslatorUtil
							.createOrUpdateExtendedProposition(
							ede,
							de, element.getUserId(),
							timeUnitDao, translatorSupport,
							valueCompDao);
				} else {
					extendedDataElements.add(PropositionTranslatorUtil
							.createOrUpdateExtendedProposition(
							ede,
							de, element.getUserId(),
							timeUnitDao, translatorSupport,
							valueCompDao));
				}
				j++;
			}
			for (int k = extendedDataElements.size() - 1; k >= j; k--) {
				extendedDataElements.remove(k);
			}
			
			i++;
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

		List<ValueThreshold> thresholds = new ArrayList<>();
		for (ValueThresholdEntity vte : entity.getValueThresholds()) {
			ValueThreshold threshold = new ValueThreshold();
			threshold.setLowerValue(vte.getMinTValueThreshold());
			threshold.setLowerComp(vte.getMinValueComp().getId());
			// threshold.setLowerUnits(vte.getMinUnits());

			threshold.setUpperValue(vte.getMaxTValueThreshold());
			threshold.setUpperComp(vte.getMaxValueComp().getId());
			// threshold.setUpperUnits(vte.getMaxUnits());

			DataElementEntity dataElementEntity = vte.getAbstractedFrom();
			DataElementField elementField = new DataElementField();
			PropositionTypeVisitor visitor = new PropositionTypeVisitor();
			dataElementEntity.accept(visitor);
			elementField.setType(visitor.getType());
			elementField.setDataElementDescription(dataElementEntity
					.getDescription());
			elementField.setDataElementDisplayName(dataElementEntity
					.getDisplayName());
			elementField.setDataElementKey(dataElementEntity.getKey());
			threshold.setDataElement(elementField);
			
			List<DataElementField> relatedDataElements =
					new ArrayList<>();
			for (ExtendedDataElement elt : vte.getExtendedDataElements()) {
				DataElementField dataElementField =
						PropositionTranslatorUtil.createDataElementField(elt);
				relatedDataElements.add(dataElementField);
			}
			threshold.setRelatedDataElements(relatedDataElements);
			
			threshold.setWithinAtLeast(vte.getWithinAtLeast());
			threshold.setWithinAtLeastUnit(vte.getWithinAtLeastUnits().getId());
			threshold.setWithinAtMost(vte.getWithinAtMost());
			threshold.setWithinAtMostUnit(vte.getWithinAtMostUnits().getId());
			threshold.setRelationOperator(vte.getRelationOperator().getId());
			thresholds.add(threshold);
		}
		result.setValueThresholds(thresholds);

		return result;
	}
}
