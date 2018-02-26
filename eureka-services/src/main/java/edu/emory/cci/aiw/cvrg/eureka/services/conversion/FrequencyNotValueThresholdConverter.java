/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.PhenotypeEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.ExtendedPhenotype;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.FrequencyEntity;
import org.protempa.PropositionDefinition;

import java.util.ArrayList;
import java.util.List;

import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.ConversionUtil.unit;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.SimpleGapFunction;
import org.protempa.SliceDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.interval.Relation;

public final class FrequencyNotValueThresholdConverter 
		extends AbstractConverter implements FrequencyConverter {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private HighLevelAbstractionDefinition primary;
	private String primaryPropId;
	private final PhenotypeConversionSupport conversionSupport;

	public FrequencyNotValueThresholdConverter() {
		this.conversionSupport = new PhenotypeConversionSupport();
	}
	
	@Override
	public HighLevelAbstractionDefinition getPrimaryPropositionDefinition() {
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
		List<PropositionDefinition> result = new ArrayList<>();
		String propId = this.conversionSupport.toPropositionId(entity);
		this.primaryPropId = propId;
		if (this.converterVisitor.addPropositionId(propId)) {
			ExtendedPhenotype extendedProposition = entity.getExtendedProposition();
			HighLevelAbstractionDefinition p =
					new HighLevelAbstractionDefinition(propId);
			p.setDisplayName(entity.getDisplayName());
			p.setDescription(entity.getDescription());
			p.setGapFunction(new SimpleGapFunction(0, null));
			if (entity.getFrequencyType().getName().equals("at least")) {
				PhenotypeEntity abstractedFrom = extendedProposition.getPhenotypeEntity();
				abstractedFrom.accept(converterVisitor);
				result.addAll(converterVisitor.getPropositionDefinitions());
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
				String wrapperPropId = entity.getKey() + "_SUB";
				SliceDefinition sp = new SliceDefinition(wrapperPropId);
				sp.setDisplayName(entity.getDisplayName());
				sp.setDescription(entity.getDescription());
				PhenotypeEntity abstractedFrom = extendedProposition.getPhenotypeEntity();
				abstractedFrom.accept(converterVisitor);
				result.addAll(converterVisitor.getPropositionDefinitions());
				sp.setMergedInterval(true);
				sp.setGapFunction(new SimpleGapFunction(0, null));
				TemporalExtendedPropositionDefinition tepd =
						ConversionUtil.buildExtendedPropositionDefinition(
						extendedProposition);
				sp.add(tepd);
				sp.setMinIndex(0);
				sp.setMaxIndex(entity.getCount());
				sp.setSourceId(sourceId(entity));

				TemporalExtendedPropositionDefinition tepds =
						new TemporalExtendedPropositionDefinition(wrapperPropId);
				p.add(tepds);
				p.setRelation(tepds, tepds, new Relation());
				result.add(sp);
			} else {
				throw new IllegalStateException("invalid frequency type: " + entity.getFrequencyType().getName());
			}
			
			p.setSourceId(sourceId(entity));
			this.primary = p;
			result.add(p);
		}
		return result;
	}
}
