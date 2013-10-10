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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
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

public final class CategorizationConverter implements
		PropositionDefinitionConverter<CategoryEntity, PropositionDefinition> {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private PropositionDefinition primary;
	private String primaryPropId;

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
				new ArrayList<PropositionDefinition>();
		String id = category.getKey()
				+ ConversionUtil.PRIMARY_PROP_ID_SUFFIX;
		this.primaryPropId = id;
		if (this.converterVisitor.addPropositionId(id)) {
			PropositionDefinition primary;
			List<String> inverseIsADefs =
					new ArrayList<String>();
			List<PropositionDefinition> inverseIsADefsIncludingSecondaries =
					new ArrayList<PropositionDefinition>();
			for (DataElementEntity e : category.getMembers()) {
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
					primary = event;
					break;
				case CONSTANT:
					ConstantDefinition constant = new ConstantDefinition(id);
					constant.setDescription(category.getDescription());
					constant.setDisplayName(category.getDisplayName());
					constant.setInverseIsA(inverseIsA);
					primary = constant;
					break;
				case PRIMITIVE_PARAMETER:
					PrimitiveParameterDefinition primParam =
							new PrimitiveParameterDefinition(id);
					primParam.setDescription(category.getDescription());
					primParam.setDisplayName(category.getDisplayName());
					primParam.setInverseIsA(inverseIsA);
					primary = primParam;
					break;
				case HIGH_LEVEL_ABSTRACTION:
					HighLevelAbstractionDefinition hla =
							new HighLevelAbstractionDefinition(id);
					hla.setDescription(category.getDescription());
					hla.setDisplayName(category.getDisplayName());
					hla.setInverseIsA(inverseIsA);
					hla.setGapFunction(new SimpleGapFunction(0, null));
					primary = hla;
					break;
				case SEQUENTIAL_TEMPORAL_PATTERN_ABSTRACTION:
					SequentialTemporalPatternDefinition stpa =
							new SequentialTemporalPatternDefinition(id);
					stpa.setDescription(category.getDescription());
					stpa.setDisplayName(category.getDisplayName());
					stpa.setInverseIsA(inverseIsA);
					stpa.setGapFunction(new SimpleGapFunction(0, null));
					primary = stpa;
					break;
				case SLICE_ABSTRACTION:
					SliceDefinition sla = new SliceDefinition(id);
					sla.setDescription(category.getDescription());
					sla.setDisplayName(category.getDisplayName());
					sla.setInverseIsA(inverseIsA);
					primary = sla;
					break;
				case LOW_LEVEL_ABSTRACTION:
					// fall through
				case COMPOUND_LOW_LEVEL_ABSTRACTION:
					HighLevelAbstractionDefinition llad =
							new HighLevelAbstractionDefinition(id);
					llad.setDescription(category.getDescription());
					llad.setDisplayName(category.getDisplayName());

					// before low-level abstractions are sent to Protempa,
					// they will be wrapped in high-level abstractions to
					// actually match the values the user specified
					// we want these wrapper abstractions to be the
					// inverse-is-a elements
					String[] wrappedIia = new String[inverseIsA.length];
					for (int i = 0; i < inverseIsA.length; i++) {
						wrappedIia[i] = inverseIsA[i] + "_WRAPPER";
					}

					llad.setInverseIsA(wrappedIia);
//					llad.setConcatenable(false);
					llad.setGapFunction(new SimpleGapFunction(0, null));
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
