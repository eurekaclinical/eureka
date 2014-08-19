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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedDataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;

import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.MinMaxGapFunction;
import org.protempa.PropositionDefinition;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.proposition.interval.Relation;

import java.util.ArrayList;
import java.util.List;

import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.ConversionUtil.unit;
import java.util.Collection;
import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.SimpleGapFunction;
import org.protempa.SliceDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.ValueClassification;

public final class FrequencyValueThresholdConverter extends AbstractConverter 
		implements FrequencyConverter {

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
				new ArrayList<>();

		String propId = toPropositionId(entity);
		this.primaryPropId = propId;

		if (this.converterVisitor.addPropositionId(propId)) {
			ExtendedDataElement extendedProposition =
					entity.getExtendedProposition();
			DataElementEntity abstractedFrom =
					extendedProposition.getDataElementEntity();
			abstractedFrom.accept(this.converterVisitor);
			Collection<PropositionDefinition> intermediates =
					this.converterVisitor.getPropositionDefinitions();
			result.addAll(intermediates);
			String abstractedFromPrimaryPropId =
					this.converterVisitor.getPrimaryPropositionId();
			String wrapperPropId = entity.getKey() + "_SUB";

			if (entity.isConsecutive()) {
				CompoundLowLevelAbstractionDefinition frequencyWrapper =
						new CompoundLowLevelAbstractionDefinition(
						wrapperPropId);
				frequencyWrapper.setMinimumNumberOfValues(
						entity.getCount());
				frequencyWrapper.setGapFunction(
						new SimpleGapFunction(Integer.valueOf(0), null));
				ValueClassification vc = new ValueClassification(
						asValueString(entity),
						abstractedFromPrimaryPropId,
						asValueString(abstractedFrom));
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
				
				TemporalExtendedPropositionDefinition tepdOuter;
				if (entity.getFrequencyType().getName().equals("at least")) {
					TemporalExtendedParameterDefinition tepd =
							new TemporalExtendedParameterDefinition(wrapperPropId);
					tepd.setValue(asValue(entity));
					tepdOuter = tepd;
				} else if (entity.getFrequencyType().getName().equals("first")) {
					frequencyWrapper.setSkip(1);
					String subWrapperPropId = wrapperPropId + "SUB";
					SliceDefinition sp = new SliceDefinition(subWrapperPropId);
					sp.setMinIndex(0);
					sp.setMaxIndex(1);
					TemporalExtendedParameterDefinition tepd2 =
							new TemporalExtendedParameterDefinition(wrapperPropId);
					tepd2.setValue(asValue(entity));
					sp.add(tepd2);
					result.add(sp);

					TemporalExtendedPropositionDefinition tepd =
							new TemporalExtendedPropositionDefinition(subWrapperPropId);

					tepdOuter = tepd;
				} else {
					throw new IllegalStateException("invalid frequency type: " + entity.getFrequencyType().getName());
				}
				HighLevelAbstractionDefinition hlad =
						new HighLevelAbstractionDefinition(propId);
				hlad.add(tepdOuter);
				hlad.setRelation(tepdOuter, tepdOuter, new Relation());

				hlad.setDisplayName(entity.getDisplayName());
				hlad.setDescription(entity.getDescription());
				hlad.setGapFunction(
						new SimpleGapFunction(Integer.valueOf(0), null));
				result.add(hlad);
				this.primary = hlad;
			} else {
				HighLevelAbstractionDefinition p =
						new HighLevelAbstractionDefinition(propId);
				p.setDisplayName(entity.getDisplayName());
				p.setDescription(entity.getDescription());
				if (entity.getFrequencyType().getName().equals("at least")) {
					TemporalExtendedPropositionDefinition[] tepds =
							new TemporalExtendedPropositionDefinition[entity.getCount()];
					for (int i = 0; i < entity.getCount(); i++) {
						TemporalExtendedPropositionDefinition tepd =
								ConversionUtil.buildExtendedPropositionDefinition(
								extendedProposition);
						tepds[i] = tepd;
						p.add(tepd);
					}
					if (tepds.length > 1) {
						for (int i = 0; i < tepds.length - 1; i++) {
							Relation rel = new Relation(null, null, null, null, null,
									null, null, null, entity.getWithinAtLeast(),
									unit(entity.getWithinAtLeastUnits()),
									entity.getWithinAtMost(),
									unit(entity.getWithinAtMostUnits()), null, null,
									null, null);
							p.setRelation(tepds[i], tepds[i + 1], rel);
						}
					} else {
						p.setRelation(tepds[0], tepds[0], new Relation());
					}
				} else if (entity.getFrequencyType().getName().equals("first")) {
					SliceDefinition sp = new SliceDefinition(wrapperPropId);
					sp.setDisplayName(entity.getDisplayName());
					sp.setDescription(entity.getDescription());
					sp.setMergedInterval(true);
					sp.setGapFunction(new SimpleGapFunction(0, null));
					TemporalExtendedPropositionDefinition tepd =
							ConversionUtil.buildExtendedPropositionDefinition(
							extendedProposition);
					sp.add(tepd);
					sp.setMinIndex(0);
					sp.setMaxIndex(entity.getCount());
					result.add(sp);
					TemporalExtendedPropositionDefinition tepdForSlice =
							new TemporalExtendedPropositionDefinition(wrapperPropId);
					p.add(tepdForSlice);
					p.setRelation(tepdForSlice, tepdForSlice, new Relation());
				} else {
					throw new IllegalStateException("invalid frequency type: " + entity.getFrequencyType().getName());
				}
				p.setGapFunction(new SimpleGapFunction(0, null));
				this.primary = p;
				result.add(p);
			}
			this.primaryPropId = this.primary.getPropositionId();
		}


		return result;
	}
	
}
