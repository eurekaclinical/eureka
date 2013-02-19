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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import org.protempa.PropositionDefinition;

import java.util.ArrayList;
import java.util.List;

import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.ConversionUtil.unit;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.SimpleGapFunction;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.interval.Relation;
import org.protempa.proposition.value.NominalValue;

public final class FrequencyNonConsecutiveAbstractionConverter implements
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
			HighLevelAbstractionDefinition p =
					new HighLevelAbstractionDefinition(propId);
			p.setDisplayName(entity.getDisplayName());
			p.setDescription(entity.getDescription());
			ExtendedDataElement extendedProposition = entity.getExtendedProposition();
			DataElementEntity abstractedFrom = extendedProposition.getDataElementEntity();
			abstractedFrom.accept(converterVisitor);
			result.addAll(converterVisitor.getPropositionDefinitions());
			TemporalExtendedPropositionDefinition[] tepds =
					new TemporalExtendedPropositionDefinition[
							entity.getAtLeastCount()];
			for (int i = 0; i < entity.getAtLeastCount(); i++) {
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

			p.setGapFunction(new SimpleGapFunction(0, null));

			this.primary = p;
			result.add(p);
		}
		return result;
	}
}
