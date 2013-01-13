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

import java.util.ArrayList;
import java.util.List;

import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;

public final class ValueThresholdsCompoundLowLevelAbstractionPackager
        implements
        PropositionDefinitionPackager<ValueThresholdGroupEntity, CompoundLowLevelAbstractionDefinition> {

	@Override
	public CompoundLowLevelAbstractionDefinition pack(
	        ValueThresholdGroupEntity entity) {
		CompoundLowLevelAbstractionDefinition result = new CompoundLowLevelAbstractionDefinition(
		        entity.getKey());

		if (entity.getThresholdsOperator().getName().equalsIgnoreCase("any")) {
			result.setValueDefinitionMatchOperator(CompoundLowLevelAbstractionDefinition.ValueDefinitionMatchOperator.ANY);
		} else if (entity.getThresholdsOperator().getName()
		        .equalsIgnoreCase("all")) {
			result.setValueDefinitionMatchOperator(CompoundLowLevelAbstractionDefinition.ValueDefinitionMatchOperator.ALL);
		} else {
			throw new IllegalStateException("valueDefinitionMatchOperator"
			        + " can only be ANY or ALL");
		}

		List<LowLevelAbstractionDefinition> llas = new ArrayList<LowLevelAbstractionDefinition>();
		for (ValueThresholdEntity v : entity.getValueThresholds()) {
			LowLevelAbstractionDefinition def = new LowLevelAbstractionDefinition(
			        v.getAbstractedFrom().getKey() + "_CLASSIFICATION");
			def.addPrimitiveParameterId(v.getAbstractedFrom().getKey());
			def.setMinimumNumberOfValues(1);
			ValueThresholdsLowLevelAbstractionPackager
			        .thresholdToValueDefinitions(def.getId(), v, def);
			llas.add(def);
		}

		for (LowLevelAbstractionDefinition def : llas) {
			result.addValueClassification(entity.getKey() + "_VALUE",
			        def.getId(), def.getValueDefinitions().get(0).getId());
			result.addValueClassification(entity.getKey() + "_VALUE_COMP",
			        def.getId(), def.getValueDefinitions().get(1).getId());
		}

		return result;
	}
}
