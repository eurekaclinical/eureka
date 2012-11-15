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
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.SliceDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;

public final class SystemPropositionPackager implements
        PropositionDefinitionPackager<SystemProposition, PropositionDefinition> {

	@Override
	public PropositionDefinition pack(SystemProposition proposition) {
		switch (proposition.getSystemType()) {
			case EVENT:
				return new EventDefinition(proposition.getKey());
			case CONSTANT:
				return new ConstantDefinition(proposition.getKey());
			case PRIMITIVE_PARAMETER:
				return new PrimitiveParameterDefinition(proposition.getKey());
			case HIGH_LEVEL_ABSTRACTION:
				return new HighLevelAbstractionDefinition(proposition.getKey());
			case LOW_LEVEL_ABSTRACTION:
				return new LowLevelAbstractionDefinition(proposition.getKey());
			case SLICE_ABSTRACTION:
				return new SliceDefinition(proposition.getKey());
			default:
				return null;
		}
	}

}
