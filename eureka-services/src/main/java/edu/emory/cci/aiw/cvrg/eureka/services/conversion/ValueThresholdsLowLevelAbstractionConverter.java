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
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionValueDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.proposition.value.ValueComparator;
import org.protempa.proposition.value.ValueType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.protempa.proposition.value.NominalValue;

public final class ValueThresholdsLowLevelAbstractionConverter
		implements
		PropositionDefinitionConverter<ValueThresholdGroupEntity, LowLevelAbstractionDefinition> {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private LowLevelAbstractionDefinition primary;
	private String primaryPropId;

	@Override
	public LowLevelAbstractionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}
	
	@Override
	public String getPrimaryPropositionId() {
		return primaryPropId;
	}

	public void setConverterVisitor(PropositionDefinitionConverterVisitor inVisitor) {
		converterVisitor = inVisitor;
	}

	@Override
	public List<PropositionDefinition> convert(ValueThresholdGroupEntity entity) {
		if (entity.getValueThresholds().size() > 1) {
			throw new IllegalArgumentException(
					"Low-level abstraction definitions may be created only "
					+ "from singleton value thresholds.");
		}
		List<PropositionDefinition> result = new ArrayList<PropositionDefinition>();
		String propId = entity.getKey() + ConversionUtil.PRIMARY_PROP_ID_SUFFIX;
		this.primaryPropId = propId;
		if (this.converterVisitor.addPropositionId(propId)) {
			LowLevelAbstractionDefinition primary = new LowLevelAbstractionDefinition(
					propId);
			primary.setDisplayName(entity.getDisplayName());
			primary.setDescription(entity.getDescription());
			primary.setAlgorithmId("stateDetector");
			primary.setConcatenable(false);


			// low-level abstractions can be created only from singleton value
			// thresholds
			if (entity.getValueThresholds() != null && entity.getValueThresholds().size() == 1) {
				ValueThresholdEntity threshold = entity.getValueThresholds().get(0);
				threshold.getAbstractedFrom().accept(converterVisitor);
				Collection<PropositionDefinition> abstractedFrom = converterVisitor
						.getPropositionDefinitions();

				primary.addPrimitiveParameterId(
						converterVisitor.getPrimaryPropositionId());
				thresholdToValueDefinitions(entity.getKey() + "_VALUE", threshold, primary);
				result.addAll(abstractedFrom);
			}
			primary.setMinimumNumberOfValues(1);
			primary.setMaximumNumberOfValues(1);

			result.add(primary);
			this.primary = primary;
		}

		return result;
	}

	static void thresholdToValueDefinitions(String name,
			ValueThresholdEntity threshold,
			LowLevelAbstractionDefinition def) {
		LowLevelAbstractionValueDefinition valueDef = new LowLevelAbstractionValueDefinition(
				def, name);
		valueDef.setValue(NominalValue.getInstance(name));
		LowLevelAbstractionValueDefinition compValueDef = new LowLevelAbstractionValueDefinition(
				def, name + "_COMP");
		compValueDef.setValue(NominalValue.getInstance(name + "_COMP"));
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