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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.LowLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SimpleParameterConstraint;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionValueDefinition;
import org.protempa.proposition.value.NumberValue;
import org.protempa.proposition.value.ValueComparator;

import static edu.emory.cci.aiw.cvrg.eureka.services.packaging.PropositionDefinitionPackagerUtil.unit;

public final class LowLevelAbstractionPackager
        implements
        PropositionDefinitionPackager<LowLevelAbstraction, LowLevelAbstractionDefinition> {

	static final String COMPLEMENT_SUFFIX = "_COMPLEMENT";


	@Override
	public LowLevelAbstractionDefinition pack(LowLevelAbstraction proposition) {
		LowLevelAbstractionDefinition result = new LowLevelAbstractionDefinition(
		        proposition.getId().toString());

		for (Proposition p : proposition.getAbstractedFrom()) {
			result.addPrimitiveParameterId(p.getKey());
		}

		constraintToValueDefinition(proposition.getKey(),
				proposition.getUserConstraint(), result);
		constraintToValueDefinition(proposition.getKey() + COMPLEMENT_SUFFIX,
				proposition.getComplementConstraint(), result);

		result.setMinimumNumberOfValues(proposition.getMinValues());
		result.setMinimumGapBetweenValues(proposition.getMinGapValues());
		result.setMinimumGapBetweenValuesUnits(unit(proposition
				.getMinGapValuesUnits()));
		result.setMaximumGapBetweenValues(proposition.getMaxGapValues());
		result.setMinimumGapBetweenValuesUnits(unit(proposition
				.getMaxGapValuesUnits()));

		return result;
	}

	private static void
			constraintToValueDefinition(String name,
												SimpleParameterConstraint
												constraint,
										LowLevelAbstractionDefinition def) {
		LowLevelAbstractionValueDefinition valueDef = new
				LowLevelAbstractionValueDefinition(def, name);
		if (constraint.getMinValueThreshold() != null && constraint
				.getMinValueComp() != null) {
			valueDef.setParameterValue("minThreshold",
					NumberValue.getInstance(constraint.getMinValueThreshold()
							.longValue()));
			valueDef.setParameterComp("minThreshold", ValueComparator.parse
					(constraint.getMinValueComp().getName()));
		}
		if (constraint.getMaxValueThreshold() != null && constraint
				.getMaxValueComp() != null) {
			valueDef.setParameterValue("maxThreshold",
					NumberValue.getInstance(constraint.getMaxValueThreshold()
							.longValue()));
			valueDef.setParameterComp("maxThreshold", ValueComparator.parse
					(constraint.getMaxValueComp().getName()));
		}
	}

}
