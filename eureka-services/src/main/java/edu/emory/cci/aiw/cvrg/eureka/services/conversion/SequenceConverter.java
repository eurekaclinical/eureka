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
import edu.emory.cci.aiw.cvrg.eureka.services.entity.Relation;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.SequenceEntity;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;

import java.util.ArrayList;
import java.util.List;

import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.ConversionUtil.unit;
import java.util.HashMap;
import java.util.Map;
import org.protempa.proposition.interval.Interval.Side;
import org.protempa.SimpleGapFunction;
import org.protempa.TemporalPatternOffset;

final class SequenceConverter extends AbstractConverter
		implements
		PropositionDefinitionConverter<SequenceEntity, HighLevelAbstractionDefinition> {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private HighLevelAbstractionDefinition primary;
	private String primaryPropId;
	private final Map<Long, TemporalExtendedPropositionDefinition> extendedProps;

	public SequenceConverter() {
		this.extendedProps = new HashMap<>();
	}

	@Override
	public HighLevelAbstractionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}
	
	@Override
	public String getPrimaryPropositionId() {
		return primaryPropId;
	}

	public void setConverterVisitor(PropositionDefinitionConverterVisitor inVisitor) {
		converterVisitor = inVisitor;
	}

	@Override
	public List<PropositionDefinition> convert(SequenceEntity sequenceEntity) {
		List<PropositionDefinition> result = new ArrayList<>();
		String propId = toPropositionId(sequenceEntity);
		this.primaryPropId = propId;
		if (this.converterVisitor.addPropositionId(propId)) {
			HighLevelAbstractionDefinition p = new HighLevelAbstractionDefinition(
					propId);
			TemporalExtendedPropositionDefinition primaryEP = 
					buildExtendedProposition(
					sequenceEntity.getPrimaryExtendedPhenotype());
			if (sequenceEntity.getRelations() != null) {
				for (Relation rel : sequenceEntity.getRelations()) {
					PhenotypeEntity lhs =
							rel.getLhsExtendedPhenotype().getPhenotypeEntity();
					lhs.accept(converterVisitor);
					result.addAll(
							converterVisitor.getPropositionDefinitions());
					TemporalExtendedPropositionDefinition tepdLhs = buildExtendedProposition(rel
							.getLhsExtendedPhenotype());

					PhenotypeEntity rhs =
							rel.getRhsExtendedPhenotype().getPhenotypeEntity();
					rhs.accept(converterVisitor);
					result.addAll(converterVisitor.getPropositionDefinitions());
					TemporalExtendedPropositionDefinition tepdRhs = buildExtendedProposition(rel
							.getRhsExtendedPhenotype());

					p.add(tepdLhs);
					p.add(tepdRhs);
					p.setRelation(tepdLhs, tepdRhs, buildRelation(rel));
				}
			}
			
			p.setConcatenable(false);
			p.setSolid(false);
			p.setGapFunction(new SimpleGapFunction(0, null));
			
			TemporalPatternOffset temporalOffsets = new TemporalPatternOffset();
			temporalOffsets.setStartTemporalExtendedPropositionDefinition(primaryEP);
			temporalOffsets.setStartIntervalSide(Side.START);
			temporalOffsets.setStartOffset(0);
			temporalOffsets.setStartOffsetUnits(null);
			temporalOffsets.setFinishTemporalExtendedPropositionDefinition(primaryEP);
			temporalOffsets.setFinishIntervalSide(Side.FINISH);
			temporalOffsets.setFinishOffset(0);
			temporalOffsets.setFinishOffsetUnits(null);
			p.setTemporalOffset(temporalOffsets);
			p.setDisplayName(sequenceEntity.getDisplayName());
			p.setDescription(sequenceEntity.getDescription());
			p.setSourceId(sourceId(sequenceEntity));
			this.primary = p;
			result.add(p);
		}
		
		
		return result;
	}

	private TemporalExtendedPropositionDefinition buildExtendedProposition(
			ExtendedPhenotype ep) {
		TemporalExtendedPropositionDefinition tepd =
				this.extendedProps.get(ep.getId());
		if (tepd == null) {
			tepd = 
					ConversionUtil.buildExtendedPropositionDefinition(
					ep);

			this.extendedProps.put(ep.getId(), tepd);
		}

		return tepd;
	}

	private org.protempa.proposition.interval.Relation buildRelation(
			Relation rel) {
		return new org.protempa.proposition.interval.Relation(
				null, null, null, null, 
				rel.getMins1f2(), 
				unit(rel.getMins1f2TimeUnit()),
				rel.getMaxs1f2(), 
				unit(rel.getMaxs1f2TimeUnit()),
				rel.getMinf1s2(), 
				unit(rel.getMinf1s2TimeUnit()),
				rel.getMaxf1s2(), 
				unit(rel.getMaxf1s2TimeUnit()), 
				null, null, null, null);
	}
}
