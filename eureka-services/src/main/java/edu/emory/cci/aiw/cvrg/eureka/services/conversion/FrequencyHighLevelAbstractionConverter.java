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

import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.ConversionUtil.unit;
import java.util.Collection;
import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.CompoundLowLevelAbstractionDefinition.ValueClassification;
import org.protempa.SimpleGapFunction;

public final class FrequencyHighLevelAbstractionConverter
        implements
        PropositionDefinitionConverter<FrequencyEntity, HighLevelAbstractionDefinition> {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private HighLevelAbstractionDefinition primary;
	private String primaryPropId;

	@Override
	public HighLevelAbstractionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}
	
	@Override
	public String getPrimaryPropositionId() {
		return primaryPropId;
	}

	public void setConverterVisitor(
	        PropositionDefinitionConverterVisitor inVisitor) {
		converterVisitor = inVisitor;
	}

	@Override
	public List<PropositionDefinition> convert(FrequencyEntity entity) {
		List<PropositionDefinition> result = new ArrayList<PropositionDefinition>();

		if (!(entity.getAbstractedFrom() instanceof ValueThresholdGroupEntity)) {
			throw new IllegalArgumentException(
			        "frequency must be abstracted from value thresholds");
		}
		String propId = entity.getKey() + ConversionUtil.PRIMARY_PROP_ID_SUFFIX;
		this.primaryPropId = propId;
		if (this.converterVisitor.addPropositionId(propId)) {
			HighLevelAbstractionDefinition hlad = new HighLevelAbstractionDefinition(
					propId);

			ValueThresholdGroupEntity thresholds = (ValueThresholdGroupEntity) entity
					.getAbstractedFrom();
			thresholds.accept(converterVisitor);
			Collection<PropositionDefinition> intermediates = converterVisitor
					.getPropositionDefinitions();
			result.addAll(intermediates);
			
			String frequencyWrapperPropId = entity.getKey() + "_SUB";
			CompoundLowLevelAbstractionDefinition frequencyWrapper =
					new CompoundLowLevelAbstractionDefinition(
					frequencyWrapperPropId);
			frequencyWrapper.setMinimumNumberOfValues(entity.getAtLeastCount());
			frequencyWrapper.setGapFunction(new SimpleGapFunction(Integer.valueOf(0), null));
			ValueClassification vc = new ValueClassification(
					entity.getKey() + "_VALUE", 
					this.converterVisitor.getPrimaryPropositionId(), 
					thresholds.getKey() + "_VALUE");
			frequencyWrapper.addValueClassification(vc);
			ValueClassification vcComp = new ValueClassification(
					entity.getKey() + "_VALUE_COMP", 
					this.converterVisitor.getPrimaryPropositionId(), 
					thresholds.getKey() + "_VALUE_COMP");
			frequencyWrapper.addValueClassification(vcComp);
			result.add(frequencyWrapper);
			
			TemporalExtendedParameterDefinition tepd = 
					new TemporalExtendedParameterDefinition(
					frequencyWrapper.getId(), 
					NominalValue.getInstance(thresholds.getKey() + "_VALUE"));
			hlad.add(tepd);
			hlad.setRelation(tepd, tepd, new Relation());
			hlad.setGapFunction(new MinMaxGapFunction(entity.getWithinAtLeast(),
					unit(entity.getWithinAtLeastUnits()), entity.getWithinAtMost(),
					unit(entity.getWithinAtMostUnits())));
			hlad.setDisplayName(entity.getDisplayName());
			hlad.setDescription(entity.getDescription());

			result.add(hlad);
			this.primary = hlad;
		}
		

		return result;
	}
}
