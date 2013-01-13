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
package edu.emory.cci.aiw.cvrg.eureka.services.transformation;

import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionValueDefinition;
import org.protempa.proposition.value.ValueComparator;
import org.protempa.proposition.value.ValueType;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;

public final class ValueThresholdsLowLevelAbstractionPackager
        implements
        PropositionDefinitionPackager<ValueThresholdGroupEntity, LowLevelAbstractionDefinition> {

	private final ValueComparatorDao valueCompDao;

	@Inject
	public ValueThresholdsLowLevelAbstractionPackager(
	        ValueComparatorDao inValueCompDao) {
		valueCompDao = inValueCompDao;
	}

	@Override
	public LowLevelAbstractionDefinition pack(ValueThresholdGroupEntity entity) {
		LowLevelAbstractionDefinition result = new LowLevelAbstractionDefinition(
		        entity.getKey());

		result.setAlgorithmId("stateDetector");

		// low-level abstractions can be created only from singleton value
		// thresholds
		ValueThresholdEntity threshold = entity.getValueThresholds().get(0);
		result.addPrimitiveParameterId(threshold.getAbstractedFrom().getKey());
		thresholdToValueDefinitions(entity.getKey() + "_VALUE", threshold, result);
		result.setMinimumNumberOfValues(1);

		return result;
	}

	static void thresholdToValueDefinitions(String name,
	        ValueThresholdEntity threshold, LowLevelAbstractionDefinition def) {
		LowLevelAbstractionValueDefinition valueDef = new LowLevelAbstractionValueDefinition(
		        def, name);
		LowLevelAbstractionValueDefinition compValueDef = new LowLevelAbstractionValueDefinition(
		        def, name + "_COMP");
		if (threshold.getMinValueThreshold() != null
		        && threshold.getMinValueComp() != null) {
			valueDef.setParameterValue("minThreshold", ValueType.VALUE
			        .parse(threshold.getMinValueThreshold().toString()));
			valueDef.setParameterComp("minThreshold", ValueComparator
			        .parse(threshold.getMinValueComp().getName()));
			compValueDef.setParameterValue("maxThreshold", ValueType.VALUE
			        .parse(threshold.getMinValueThreshold().toString()));
			compValueDef.setParameterComp(
			        "maxThreshold",
			        ValueComparator.parse(threshold.getMinValueComp()
			                .getComplement().getName()));
		}
		if (threshold.getMaxValueThreshold() != null
		        && threshold.getMaxValueComp() != null) {
			valueDef.setParameterValue("maxThreshold", ValueType.VALUE
			        .parse(threshold.getMaxValueThreshold().toString()));
			valueDef.setParameterComp("maxThreshold", ValueComparator
			        .parse(threshold.getMaxValueComp().getName()));
			compValueDef.setParameterValue("minThreshold", ValueType.VALUE
			        .parse(threshold.getMaxValueThreshold().toString()));
			compValueDef.setParameterComp(
			        "minThreshold",
			        ValueComparator.parse(threshold.getMaxValueComp()
			                .getComplement().getName()));
		}
	}
}