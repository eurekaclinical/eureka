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
import org.protempa.SequentialTemporalPatternDefinition;
import org.protempa.SequentialTemporalPatternDefinition.SubsequentTemporalExtendedPropositionDefinition;
import org.protempa.SimpleGapFunction;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.ValueClassification;

public final class FrequencyConsecutiveConverter implements
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
		List<PropositionDefinition> result =
				new ArrayList<PropositionDefinition>();

		String propId = entity.getKey()
				+ ConversionUtil.PRIMARY_PROP_ID_SUFFIX;
		this.primaryPropId = propId;

		if (this.converterVisitor.addPropositionId(propId)) {
			DataElementEntity abstractedFrom = entity.getAbstractedFrom();
			abstractedFrom.accept(this.converterVisitor);
			Collection<PropositionDefinition> intermediates =
					this.converterVisitor.getPropositionDefinitions();
			result.addAll(intermediates);
			String abstractedFromPrimaryPropId =
					this.converterVisitor.getPrimaryPropositionId();
			String wrapperPropId = entity.getKey() + "_SUB";
			HighLevelAbstractionDefinition hlad =
					new HighLevelAbstractionDefinition(propId);
			if (abstractedFrom instanceof ValueThresholdGroupEntity) {
				CompoundLowLevelAbstractionDefinition frequencyWrapper =
						new CompoundLowLevelAbstractionDefinition(
						wrapperPropId);
				frequencyWrapper.setMinimumNumberOfValues(
						entity.getAtLeastCount());
				frequencyWrapper.setGapFunction(
						new SimpleGapFunction(Integer.valueOf(0), null));
				ValueClassification vc = new ValueClassification(
						entity.getKey() + "_VALUE",
						abstractedFromPrimaryPropId,
						abstractedFrom.getKey() + "_VALUE");
				frequencyWrapper.addValueClassification(vc);
				ValueClassification vcComp = new ValueClassification(
						entity.getKey() + "_VALUE_COMP",
						abstractedFromPrimaryPropId,
						abstractedFrom.getKey() + "_VALUE_COMP");
				frequencyWrapper.addValueClassification(vcComp);
				frequencyWrapper.setGapFunctionBetweenValues(
						new MinMaxGapFunction(entity.getWithinAtLeast(),
						unit(entity.getWithinAtLeastUnits()),
						entity.getWithinAtMost(),
						unit(entity.getWithinAtMostUnits())));
				result.add(frequencyWrapper);
			} else {
				SequentialTemporalPatternDefinition stpd =
						new SequentialTemporalPatternDefinition(propId);
				stpd.setDisplayName(entity.getDisplayName());
				stpd.setDescription(entity.getDescription());
				stpd.setGapFunction(
						new SimpleGapFunction(Integer.valueOf(0), null));
				TemporalExtendedPropositionDefinition tepd =
						new TemporalExtendedPropositionDefinition(
						abstractedFrom.getKey());
				stpd.setFirstTemporalExtendedPropositionDefinition(tepd);
				int n = entity.getAtLeastCount() - 1;
				SubsequentTemporalExtendedPropositionDefinition[] rtepds =
						new SubsequentTemporalExtendedPropositionDefinition[n];
				for (int i = 0; i < n; i++) {
					TemporalExtendedPropositionDefinition tepd2 =
							new TemporalExtendedPropositionDefinition(
							abstractedFrom.getKey());
					Relation r = new Relation(null, null, null, null, null,
							null, null, null,
							entity.getWithinAtLeast(),
							unit(entity.getWithinAtLeastUnits()),
							entity.getWithinAtMost(),
							unit(entity.getWithinAtMostUnits()),
							null, null, null, null);
					rtepds[i] =
							new SubsequentTemporalExtendedPropositionDefinition(r,
							tepd2);
				}
				stpd.setSubsequentTemporalExtendedPropositionDefinitions(rtepds);
				result.add(stpd);
			}
			TemporalExtendedParameterDefinition tepd =
					new TemporalExtendedParameterDefinition(wrapperPropId,
					NominalValue.getInstance(entity.getKey() + "_VALUE"));
			hlad.add(tepd);
			hlad.setRelation(tepd, tepd, new Relation());
			hlad.setDisplayName(entity.getDisplayName());
			hlad.setDescription(entity.getDescription());
			hlad.setGapFunction(
					new SimpleGapFunction(Integer.valueOf(0), null));
			result.add(hlad);
			this.primary = hlad;
		}


		return result;
	}
}
