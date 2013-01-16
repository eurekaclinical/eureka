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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.MinMaxGapFunction;
import org.protempa.PropositionDefinition;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.proposition.interval.Relation;
import org.protempa.proposition.value.NominalValue;

import java.util.ArrayList;
import java.util.List;

import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.PropositionDefinitionConverterUtil.unit;

public final class FrequencyHighLevelAbstractionConverter
		implements
		PropositionDefinitionConverter<FrequencyEntity, HighLevelAbstractionDefinition> {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private HighLevelAbstractionDefinition primary;

	@Override
	public HighLevelAbstractionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}

	public void setConverterVisitor(PropositionDefinitionConverterVisitor
											inVisitor) {
		converterVisitor = inVisitor;
	}

	@Override
	public List<PropositionDefinition> convert(FrequencyEntity entity) {
		List<PropositionDefinition> result = new
				ArrayList<PropositionDefinition>();
		HighLevelAbstractionDefinition primary = new HighLevelAbstractionDefinition(
				entity.getKey());

		if (!(entity.getAbstractedFrom() instanceof ValueThresholdGroupEntity)) {
			throw new IllegalArgumentException(
					"frequency must be abstracted from value thresholds");
		}
		ValueThresholdGroupEntity thresholds = (ValueThresholdGroupEntity) entity
				.getAbstractedFrom();
		thresholds.accept(converterVisitor);
		List<PropositionDefinition> abstractedFrom = converterVisitor.getPropositionDefinitions();
		result.addAll(abstractedFrom);
		TemporalExtendedParameterDefinition tepd = new TemporalExtendedParameterDefinition(
				converterVisitor.getPrimaryProposition().getId());
		tepd.setValue(NominalValue.getInstance(converterVisitor
				.getPrimaryProposition().getId()
				+ "_VALUE"));
		primary.setRelation(tepd, tepd, new Relation());
		primary.setGapFunction(new MinMaxGapFunction(entity.getWithinAtLeast(),
				unit(entity.getWithinAtLeastUnits()), entity.getWithinAtMost(),
				unit(entity.getWithinAtMostUnits())));

		result.add(primary);
		this.primary = primary;

		return result;
	}
}
