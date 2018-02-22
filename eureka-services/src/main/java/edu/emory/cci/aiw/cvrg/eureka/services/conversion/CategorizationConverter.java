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

import edu.emory.cci.aiw.cvrg.eureka.services.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.PhenotypeEntity;
import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.SimpleGapFunction;
import org.protempa.SliceDefinition;

import java.util.ArrayList;
import java.util.List;
import org.protempa.SequentialTemporalPatternDefinition;

public final class CategorizationConverter extends AbstractConverter implements
		PropositionDefinitionConverter<CategoryEntity, PropositionDefinition> {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private PropositionDefinition primary;
	private String primaryPropId;
	private final PhenotypeConversionSupport conversionSupport;

	public CategorizationConverter() {
		this.conversionSupport = new PhenotypeConversionSupport();
	}
	
	@Override
	public PropositionDefinition getPrimaryPropositionDefinition() {
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
	public List<PropositionDefinition> convert(CategoryEntity category) {
		List<PropositionDefinition> result =
				new ArrayList<>();
		String id = this.conversionSupport.toPropositionId(category);
		this.primaryPropId = id;
		if (this.converterVisitor.addPropositionId(id)) {
			PropositionDefinition primary;
			List<String> inverseIsADefs =
					new ArrayList<>();
			List<PropositionDefinition> inverseIsADefsIncludingSecondaries =
					new ArrayList<>();
			for (PhenotypeEntity e : category.getMembers()) {
				e.accept(this.converterVisitor);
				inverseIsADefsIncludingSecondaries.addAll(this.converterVisitor
						.getPropositionDefinitions());
				String primaryPropositionId =
						this.converterVisitor.getPrimaryPropositionId();
				inverseIsADefs.add(primaryPropositionId);
			}
			result.addAll(inverseIsADefsIncludingSecondaries);
			String[] inverseIsA = inverseIsADefs.toArray(
					new String[inverseIsADefs.size()]);
			switch (category.getCategoryType()) {
				case EVENT:
					EventDefinition event = new EventDefinition(id);
					event.setDescription(category.getDescription());
					event.setDisplayName(category.getDisplayName());
					event.setInverseIsA(inverseIsA);
					event.setSourceId(sourceId(category));
					primary = event;
					break;
				case CONSTANT:
					ConstantDefinition constant = new ConstantDefinition(id);
					constant.setDescription(category.getDescription());
					constant.setDisplayName(category.getDisplayName());
					constant.setInverseIsA(inverseIsA);
					constant.setSourceId(sourceId(category));
					primary = constant;
					break;
				case PRIMITIVE_PARAMETER:
					PrimitiveParameterDefinition primParam =
							new PrimitiveParameterDefinition(id);
					primParam.setDescription(category.getDescription());
					primParam.setDisplayName(category.getDisplayName());
					primParam.setInverseIsA(inverseIsA);
					primParam.setSourceId(sourceId(category));
					primary = primParam;
					break;
				case HIGH_LEVEL_ABSTRACTION:
					HighLevelAbstractionDefinition hla =
							new HighLevelAbstractionDefinition(id);
					hla.setDescription(category.getDescription());
					hla.setDisplayName(category.getDisplayName());
					hla.setInverseIsA(inverseIsA);
					hla.setGapFunction(new SimpleGapFunction(0, null));
					hla.setSourceId(sourceId(category));
					primary = hla;
					break;
				case SEQUENTIAL_TEMPORAL_PATTERN_ABSTRACTION:
					SequentialTemporalPatternDefinition stpa =
							new SequentialTemporalPatternDefinition(id);
					stpa.setDescription(category.getDescription());
					stpa.setDisplayName(category.getDisplayName());
					stpa.setInverseIsA(inverseIsA);
					stpa.setGapFunction(new SimpleGapFunction(0, null));
					stpa.setSourceId(sourceId(category));
					primary = stpa;
					break;
				case SLICE_ABSTRACTION:
					SliceDefinition sla = new SliceDefinition(id);
					sla.setDescription(category.getDescription());
					sla.setDisplayName(category.getDisplayName());
					sla.setInverseIsA(inverseIsA);
					sla.setSourceId(sourceId(category));
					primary = sla;
					break;
				case LOW_LEVEL_ABSTRACTION:
					// fall through
				case COMPOUND_LOW_LEVEL_ABSTRACTION:
					HighLevelAbstractionDefinition llad =
							new HighLevelAbstractionDefinition(id);
					llad.setDescription(category.getDescription());
					llad.setDisplayName(category.getDisplayName());
					llad.setInverseIsA(inverseIsA);
					llad.setGapFunction(new SimpleGapFunction(0, null));
					llad.setSourceId(sourceId(category));
					primary = llad;
					break;
				default:
					throw new AssertionError("Invalid category type "
							+ category.getCategoryType());
			}

			result.add(primary);
			this.primary = primary;
		}
		return result;
	}
}
