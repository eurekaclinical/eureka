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

import org.protempa.SliceDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.SliceAbstraction;

public final class SliceAbstractionPackager implements
        PropositionDefinitionPackager<SliceAbstraction, SliceDefinition> {

	@Override
	public SliceDefinition pack(SliceAbstraction proposition) {
		SliceDefinition result = new SliceDefinition(proposition.getId().toString());
		
		result.setMinIndex(proposition.getMinIndex());
		result.addAbstractedFrom(proposition.getAbstractedFrom().getKey());
		
		return result;
	}

}
