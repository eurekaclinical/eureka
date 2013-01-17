/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 Emory University
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
import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.SliceDefinition;

import java.util.ArrayList;
import java.util.List;

public final class CategorizationConverter implements
		PropositionDefinitionConverter<CategoryEntity, PropositionDefinition> {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private PropositionDefinition primary;

	@Override
	public PropositionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}

	public void setConverterVisitor(PropositionDefinitionConverterVisitor inVisitor) {
		converterVisitor = inVisitor;
	}

	@Override
	public List<PropositionDefinition> convert(CategoryEntity proposition) {
		List<PropositionDefinition> result = new ArrayList<PropositionDefinition>();
		String id = proposition.getKey();
		List<PropositionDefinition> inverseIsADefs = new ArrayList<PropositionDefinition>();
		for (DataElementEntity e : proposition.getInverseIsA()) {
			e.accept(this.converterVisitor);
			if (this.converterVisitor.getPropositionDefinitions() != null) {
				inverseIsADefs.addAll(this.converterVisitor
						.getPropositionDefinitions());
			}
		}
		result.addAll(inverseIsADefs);
		String[] inverseIsA = inverseIsA(proposition);
		PropositionDefinition primary;
		switch (proposition.getCategorizationType()) {
			case EVENT:
				EventDefinition event = new EventDefinition(id);
				event.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				event.setDisplayName(proposition.getDisplayName());
				event.setInverseIsA(inverseIsA);
				primary = event;
				break;
			case CONSTANT:
				ConstantDefinition constant = new ConstantDefinition(id);
				constant.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				constant.setDisplayName(proposition.getDisplayName());
				constant.setInverseIsA(inverseIsA);
				primary = constant;
				break;
			case PRIMITIVE_PARAMETER:
				PrimitiveParameterDefinition primParam = new PrimitiveParameterDefinition(
						id);
				primParam.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				primParam.setDisplayName(proposition.getDisplayName());
				primParam.setInverseIsA(inverseIsA);
				primary = primParam;
				break;
			case HIGH_LEVEL_ABSTRACTION:
				HighLevelAbstractionDefinition hla = new HighLevelAbstractionDefinition(
						id);
				hla.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				hla.setDisplayName(proposition.getDisplayName());
				hla.setInverseIsA(inverseIsA);
				primary = hla;
				break;
			case SLICE_ABSTRACTION:
				SliceDefinition sla = new SliceDefinition(id);
				sla.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				sla.setDisplayName(proposition.getDisplayName());
				sla.setInverseIsA(inverseIsA);
				primary = sla;
				break;
			case LOW_LEVEL_ABSTRACTION:
				LowLevelAbstractionDefinition llad = new LowLevelAbstractionDefinition(id);
				llad.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				llad.setDisplayName(proposition.getDisplayName());
				llad.setInverseIsA(inverseIsA);
				primary = llad;
				break;
			case COMPOUND_LOW_LEVEL_ABSTRACTION:
				CompoundLowLevelAbstractionDefinition cllad = new CompoundLowLevelAbstractionDefinition(id);
				cllad.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				cllad.setDisplayName(proposition.getDisplayName());
				cllad.setInverseIsA(inverseIsA);
				primary = cllad;
				break;
			default:
				HighLevelAbstractionDefinition defaultDef = new HighLevelAbstractionDefinition(
						id);
				defaultDef.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				defaultDef.setDisplayName(proposition.getDisplayName());
				defaultDef.setInverseIsA(inverseIsA);
				primary = defaultDef;
				break;
		}

		result.add(primary);
		this.primary = primary;
		return result;
	}

	private static String[] inverseIsA(CategoryEntity proposition) {
		String[] result = new String[proposition.getInverseIsA().size()];

		for (int i = 0; i < proposition.getInverseIsA().size(); i++) {
			result[i] = proposition.getInverseIsA().get(i).getKey();
		}

		return result;
	}
}
