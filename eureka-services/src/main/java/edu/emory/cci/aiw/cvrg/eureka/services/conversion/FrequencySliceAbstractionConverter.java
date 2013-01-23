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
import org.protempa.MinMaxGapFunction;
import org.protempa.PropositionDefinition;
import org.protempa.SliceDefinition;
import org.protempa.proposition.value.Unit;

import java.util.ArrayList;
import java.util.List;

import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.ConversionUtil.unit;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.value.NominalValue;

public final class FrequencySliceAbstractionConverter implements
		PropositionDefinitionConverter<FrequencyEntity, SliceDefinition> {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private SliceDefinition primary;
	private String primaryPropId;

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
	
	@Override
	public String getPrimaryPropositionId() {
		return primaryPropId;
	}

	public void setVisitor(PropositionDefinitionConverterVisitor inVisitor) {
		converterVisitor = inVisitor;
	}

	@Override
	public List<PropositionDefinition> convert(FrequencyEntity entity) {
		if (entity.getAbstractedFrom() == null) {
			throw new IllegalArgumentException("entity cannot have a null abstractedFrom field");
		}
		List<PropositionDefinition> result = new ArrayList<PropositionDefinition>();
		String propId = entity.getKey() + ConversionUtil.PRIMARY_PROP_ID_SUFFIX;
		this.primaryPropId = propId;
		if (this.converterVisitor.addPropositionId(propId)) {
			SliceDefinition primary = new SliceDefinition(propId);
			primary.setDisplayName(entity.getDisplayName());
			primary.setDescription(entity.getDescription());

			entity.getAbstractedFrom().accept(converterVisitor);
			result.addAll(converterVisitor.getPropositionDefinitions());
			TemporalExtendedPropositionDefinition tepd;
			if (entity.getExtendedProposition().getDataElementEntity() instanceof ValueThresholdGroupEntity) {
				tepd = new TemporalExtendedParameterDefinition(
						converterVisitor.getPrimaryPropositionId(),
						NominalValue.getInstance(
						entity.getExtendedProposition().getDataElementEntity().getKey() + "_VALUE"));
			} else {
				tepd = new TemporalExtendedPropositionDefinition(
					converterVisitor.getPrimaryPropositionId());
			}
			primary.add(tepd);

			if (entity.getAtLeastCount() != null) {
				primary.setMinIndex(entity.getAtLeastCount());
			}

			Integer atLeast = null;
			Unit atLeastUnits = null;
			Integer atMost = null;
			Unit atMostUnits = null;

			if (entity.getWithinAtLeast() != null && entity.getWithinAtLeastUnits() != null) {
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
		}
		return result;
	}
}
