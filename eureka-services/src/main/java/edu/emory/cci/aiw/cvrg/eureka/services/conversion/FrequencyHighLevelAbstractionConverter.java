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

import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.MinMaxGapFunction;
import org.protempa.PropositionDefinition;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.proposition.interval.Relation;
import org.protempa.proposition.value.NominalValue;

import java.util.ArrayList;
import java.util.List;

import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.ConversionUtil.unit;
import java.util.Collection;

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


			TemporalExtendedParameterDefinition tepd = 
					new TemporalExtendedParameterDefinition(
					converterVisitor.getPrimaryPropositionId());
			tepd.setValue(NominalValue.getInstance(converterVisitor
					.getPrimaryPropositionId() + "_VALUE"));
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
