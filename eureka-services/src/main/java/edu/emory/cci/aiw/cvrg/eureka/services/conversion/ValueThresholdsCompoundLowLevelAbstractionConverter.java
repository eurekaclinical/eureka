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

import java.util.ArrayList;
import java.util.List;

import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.CompoundLowLevelAbstractionDefinition.ValueClassification;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PropositionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import org.protempa.SimpleGapFunction;

public final class ValueThresholdsCompoundLowLevelAbstractionConverter
		implements
		PropositionDefinitionConverter<ValueThresholdGroupEntity, CompoundLowLevelAbstractionDefinition> {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private CompoundLowLevelAbstractionDefinition primary;
	private String primaryPropId;

	public void setConverterVisitor(PropositionDefinitionConverterVisitor inConverterVisitor) {
		converterVisitor = inConverterVisitor;
	}

	@Override
	public CompoundLowLevelAbstractionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}
	
	@Override
	public String getPrimaryPropositionId() {
		return primaryPropId;
	}

	@Override
	public List<PropositionDefinition> convert(
			ValueThresholdGroupEntity entity) {
		List<PropositionDefinition> result = new ArrayList<PropositionDefinition>();
		String propId = entity.getKey() + ConversionUtil.PRIMARY_PROP_ID_SUFFIX;
		this.primaryPropId = propId;
		if (this.converterVisitor.addPropositionId(propId)) {
			CompoundLowLevelAbstractionDefinition primary = new CompoundLowLevelAbstractionDefinition(
					propId);
			primary.setDisplayName(entity.getDisplayName());
			primary.setDescription(entity.getDescription());

			if (entity.getThresholdsOperator().getName().equalsIgnoreCase("any")) {
				primary.setValueDefinitionMatchOperator(CompoundLowLevelAbstractionDefinition.ValueDefinitionMatchOperator.ANY);
			} else if (entity.getThresholdsOperator().getName()
					.equalsIgnoreCase("all")) {
				primary.setValueDefinitionMatchOperator(CompoundLowLevelAbstractionDefinition.ValueDefinitionMatchOperator.ALL);
			} else {
				throw new IllegalStateException("valueDefinitionMatchOperator"
						+ " can only be ANY or ALL");
			}
			
			primary.setGapFunction(new SimpleGapFunction(Integer.valueOf(0), null));
			primary.setConcatenable(false);

			List<LowLevelAbstractionDefinition> intermediates = new ArrayList<LowLevelAbstractionDefinition>();
			for (ValueThresholdEntity v : entity.getValueThresholds()) {
				v.getAbstractedFrom().accept(this.converterVisitor);
				result.addAll(this.converterVisitor.getPropositionDefinitions());
				LowLevelAbstractionDefinition def = new LowLevelAbstractionDefinition(
						v.getAbstractedFrom().getKey() + "_CLASSIFICATION");
				def.setConcatenable(false);
				def.addPrimitiveParameterId(this.converterVisitor.getPrimaryPropositionId());
				def.setMinimumNumberOfValues(1);
				def.setMaximumNumberOfValues(1);
				def.setAlgorithmId("stateDetector");
				def.setGapFunction(new SimpleGapFunction(Integer.valueOf(0), null));
				ValueThresholdsLowLevelAbstractionConverter
						.thresholdToValueDefinitions(def.getId() + "_VALUE", v, def);
				intermediates.add(def);
			}
			result.addAll(intermediates);

			for (LowLevelAbstractionDefinition def : intermediates) {
				primary.addValueClassification(new ValueClassification(entity.getKey() + "_VALUE",
						def.getId(), def.getValueDefinitions().get(0).getId()));
				primary.addValueClassification(new ValueClassification(entity.getKey() + "_VALUE_COMP",
						def.getId(), def.getValueDefinitions().get(1).getId()));
			}

			result.add(primary);
			this.primary = primary;
		}

		return result;
	}
}
