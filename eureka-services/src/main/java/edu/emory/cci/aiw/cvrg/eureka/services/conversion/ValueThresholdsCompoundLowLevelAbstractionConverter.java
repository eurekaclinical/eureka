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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PropositionDefinition;

import java.util.ArrayList;
import java.util.List;

public final class ValueThresholdsCompoundLowLevelAbstractionConverter
        implements
		PropositionDefinitionConverter<ValueThresholdGroupEntity> {

	private PropositionDefinition primary;

	@Override
	public PropositionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}

	@Override
	public List<PropositionDefinition> convert(
			ValueThresholdGroupEntity entity) {
		List<PropositionDefinition> result = new
				ArrayList<PropositionDefinition>();
		CompoundLowLevelAbstractionDefinition primary = new CompoundLowLevelAbstractionDefinition(
		        entity.getKey());
		primary.setDisplayName(entity.getDisplayName());
		primary.setAbbreviatedDisplayName(entity.getAbbrevDisplayName());

		if (entity.getThresholdsOperator().getName().equalsIgnoreCase("any")) {
			primary.setValueDefinitionMatchOperator(CompoundLowLevelAbstractionDefinition.ValueDefinitionMatchOperator.ANY);
		} else if (entity.getThresholdsOperator().getName()
		        .equalsIgnoreCase("all")) {
			primary.setValueDefinitionMatchOperator(CompoundLowLevelAbstractionDefinition.ValueDefinitionMatchOperator.ALL);
		} else {
			throw new IllegalStateException("valueDefinitionMatchOperator"
			        + " can only be ANY or ALL");
		}

		List<LowLevelAbstractionDefinition> llas = new ArrayList<LowLevelAbstractionDefinition>();
		for (ValueThresholdEntity v : entity.getValueThresholds()) {
			DataElementEntity abstractedFrom = v.getAbstractedFrom();
			LowLevelAbstractionDefinition def = new LowLevelAbstractionDefinition(
			        abstractedFrom.getKey() + "_CLASSIFICATION");
			def.setDisplayName(abstractedFrom.getDisplayName());
			def.setAbbreviatedDisplayName(
					abstractedFrom.getAbbrevDisplayName());
			def.addPrimitiveParameterId(abstractedFrom.getKey());
			def.setMinimumNumberOfValues(1);
			ValueThresholdsLowLevelAbstractionConverter
			        .thresholdToValueDefinitions(def.getId(), v, def);
			llas.add(def);
		}
		result.addAll(llas);

		for (LowLevelAbstractionDefinition def : llas) {
			primary.addValueClassification(entity.getKey() + "_VALUE",
					def.getId(), def.getValueDefinitions().get(0).getId());
			primary.addValueClassification(entity.getKey() + "_VALUE_COMP",
					def.getId(), def.getValueDefinitions().get(1).getId());
		}

		result.add(primary);
		this.primary = primary;

		return result;
	}
}
