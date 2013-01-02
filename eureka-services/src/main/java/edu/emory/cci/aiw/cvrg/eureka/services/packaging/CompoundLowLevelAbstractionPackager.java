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
package edu.emory.cci.aiw.cvrg.eureka.services.packaging;

import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.MinMaxGapFunction;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.CompoundValueThreshold;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;

import static edu.emory.cci.aiw.cvrg.eureka.services.packaging.PropositionDefinitionPackagerUtil.unit;

/**
 *
 */
public class CompoundLowLevelAbstractionPackager implements
		PropositionDefinitionPackager<CompoundValueThreshold,
				CompoundLowLevelAbstractionDefinition> {

	@Override
	public CompoundLowLevelAbstractionDefinition pack(
			CompoundValueThreshold proposition) {
		CompoundLowLevelAbstractionDefinition result = new
				CompoundLowLevelAbstractionDefinition(proposition.getKey());

		result.setMinimumNumberOfValues(proposition.getMinimumNumberOfValues());
		if (proposition.getThresholdsOperator().getName()
				.equalsIgnoreCase("any")) {
				result.setValueDefinitionMatchOperator
						(CompoundLowLevelAbstractionDefinition
								.ValueDefinitionMatchOperator.ANY);
		} else if (proposition.getThresholdsOperator().getName()
				.equalsIgnoreCase("all")) {
				result.setValueDefinitionMatchOperator(CompoundLowLevelAbstractionDefinition.ValueDefinitionMatchOperator.ALL);
		} else {
				throw new IllegalStateException("valueDefinitionMatchOperator" +
						" can only be ANY or ALL");
		}

		for (ValueThresholdEntity lla : proposition.getAbstractedFrom()) {
			result.addValueClassification(proposition
					.getUserValueDefinitionName(), lla.getKey(),
					lla.getUserConstraint().getName());
			result.addValueClassification(proposition
					.getComplementValueDefinitionName(), lla.getKey(),
					lla.getComplementConstraint().getName());
		}

		result.setGapFunction(new MinMaxGapFunction(proposition
				.getMinimumGapBetweenValues(), unit(proposition
				.getMinimumGapBetweenValuesUnits()),
				proposition.getMaximumGapBetweenValues(),
				unit(proposition.getMaximumGapBetweenValuesUnits())));

		return result;
	}
}
