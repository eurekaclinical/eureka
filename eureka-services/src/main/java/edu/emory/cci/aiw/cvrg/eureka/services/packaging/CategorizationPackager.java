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
package edu.emory.cci.aiw.cvrg.eureka.services.packaging;

import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization;

public final class CategorizationPackager implements
        PropositionDefinitionPackager<Categorization, PropositionDefinition> {

	@Override
	public PropositionDefinition pack(Categorization proposition) {
		String id = proposition.getId().toString();
		String[] inverseIsA = inverseIsA(proposition);
		switch (proposition.getCategorizationType()) {
			case EVENT:
				EventDefinition event = new EventDefinition(id);
				event.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				event.setDisplayName(proposition.getDisplayName());
				event.setInverseIsA(inverseIsA);
				return event;
			case CONSTANT:
				ConstantDefinition constant = new ConstantDefinition(id);
				constant.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				constant.setDisplayName(proposition.getDisplayName());
				constant.setInverseIsA(inverseIsA);
				return constant;
			case PRIMITIVE_PARAMETER:
				PrimitiveParameterDefinition primParam = new PrimitiveParameterDefinition(
				        id);
				primParam.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				primParam.setDisplayName(proposition.getDisplayName());
				primParam.setInverseIsA(inverseIsA);
				return primParam;
			case ABSTRACTION:
				HighLevelAbstractionDefinition hla = new HighLevelAbstractionDefinition(
				        id);
				hla.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				hla.setDisplayName(proposition.getDisplayName());
				hla.setInverseIsA(inverseIsA);
				return hla;
			default:
				HighLevelAbstractionDefinition defaultDef = new HighLevelAbstractionDefinition(
				        id);
				defaultDef.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
				defaultDef.setDisplayName(proposition.getDisplayName());
				defaultDef.setInverseIsA(inverseIsA);
				return defaultDef;
		}
	}

	private static String[] inverseIsA(Categorization proposition) {
		String[] result = new String[proposition.getInverseIsA().size()];

		for (int i = 0; i < proposition.getInverseIsA().size(); i++) {
			if (proposition.getInverseIsA().get(i).isInSystem()) {
				result[i] = proposition.getInverseIsA().get(i).getKey();
			} else {
				result[i] = proposition.getInverseIsA().get(i).getId()
				        .toString();
			}
		}

		return result;
	}
}
