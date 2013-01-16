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
import org.protempa.MinMaxGapFunction;
import org.protempa.PropositionDefinition;
import org.protempa.SliceDefinition;
import org.protempa.proposition.value.Unit;

import java.util.ArrayList;
import java.util.List;

import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.PropositionDefinitionConverterUtil.unit;

public final class FrequencySliceAbstractionConverter implements
		PropositionDefinitionConverter<FrequencyEntity, SliceDefinition> {

	private PropositionDefinitionConverterVisitor converterVisitor;

	private SliceDefinition primary;

//	@Inject
//	public FrequencySliceAbstractionConverter(
//			SystemPropositionConverter inSystemPropositionConverter,
//			CategorizationConverter inCategorizationConverter,
//			SequenceConverter inSequenceConverter,
//			FrequencyHighLevelAbstractionConverter
//					inFrequencyHighLevelAbstractionConverter,
//			ValueThresholdsLowLevelAbstractionConverter
//					inValueThresholdsLowLevelAbstractionConverter,
//			ValueThresholdsCompoundLowLevelAbstractionConverter
//					inValueThresholdsCompoundLowLevelAbstractionConverter) {
//		this.converterVisitor = new PropositionDefinitionConverterVisitor
//				(inSystemPropositionConverter, inCategorizationConverter,
//						inSequenceConverter,
//						inValueThresholdsLowLevelAbstractionConverter,
//						inValueThresholdsCompoundLowLevelAbstractionConverter, this,
//						inFrequencyHighLevelAbstractionConverter);
//	}

	@Override
	public SliceDefinition getPrimaryPropositionDefinition() {
		return primary;
	}

	public void setVisitor(PropositionDefinitionConverterVisitor inVisitor) {
		converterVisitor = inVisitor;
	}

	@Override
	public List<PropositionDefinition> convert(FrequencyEntity entity) {
		List<PropositionDefinition> result = new
				ArrayList<PropositionDefinition>();
		SliceDefinition primary = new SliceDefinition(entity.getKey());
		primary.setDisplayName(entity.getDisplayName());
		primary.setAbbreviatedDisplayName(entity.getAbbrevDisplayName());
		if (entity.getAbstractedFrom() != null) {
			entity.getAbstractedFrom().accept(converterVisitor);
			result.addAll(converterVisitor.getPropositionDefinitions());
			primary.addAbstractedFrom(entity.getAbstractedFrom().getKey());
		}

		if (entity.getAtLeastCount() != null) {
			primary.setMinIndex(entity.getAtLeastCount());
		}

		Integer atLeast = null;
		Unit atLeastUnits = null;
		Integer atMost = null;
		Unit atMostUnits = null;

		if (entity.getWithinAtLeast() != null && entity.getWithinAtLeastUnits
				() != null) {
			atLeast = entity.getWithinAtLeast();
			atLeastUnits = unit(entity.getWithinAtLeastUnits());
		}
		if (entity.getWithinAtMost() != null && entity.getWithinAtMostUnits()
				!= null) {
			atMost = entity.getWithinAtMost();
			atMostUnits = unit(entity.getWithinAtMostUnits());
		}

		primary.setGapFunction(new MinMaxGapFunction(atLeast, atLeastUnits,
				atMost, atMostUnits));

		this.primary = primary;
		result.add(primary);

		return result;
	}
}
