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
package edu.emory.cci.aiw.cvrg.eureka.services.util;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition.SystemType;
import java.util.Collection;
import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PairDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.PropositionDefinitionVisitor;
import org.protempa.SliceDefinition;

public class PropositionDefinitionTypeVisitor implements PropositionDefinitionVisitor {
	private SystemType systemType;

	public SystemType getSystemType() {
		return systemType;
	}

	@Override
	public void visit(Collection<? extends PropositionDefinition> arg0) {
		throw new UnsupportedOperationException("getting the type of a collection is not supported");
	}

	@Override
	public void visit(ConstantDefinition arg0) {
		systemType = SystemType.CONSTANT;
	}

	@Override
	public void visit(EventDefinition arg0) {
		systemType = SystemType.EVENT;
	}

	@Override
	public void visit(HighLevelAbstractionDefinition arg0) {
		systemType = SystemType.HIGH_LEVEL_ABSTRACTION;
	}

	@Override
	public void visit(LowLevelAbstractionDefinition arg0) {
		systemType = SystemType.LOW_LEVEL_ABSTRACTION;
	}

	@Override
	public void visit(PairDefinition arg0) {
		systemType = SystemType.HIGH_LEVEL_ABSTRACTION;
	}

	@Override
	public void visit(PrimitiveParameterDefinition arg0) {
		systemType = SystemType.PRIMITIVE_PARAMETER;
	}

	@Override
	public void visit(SliceDefinition arg0) {
		systemType = SystemType.SLICE_ABSTRACTION;
	}

	@Override
	public void visit(CompoundLowLevelAbstractionDefinition def) {
		systemType = SystemType.COMPOUND_LOW_LEVEL_ABSTRACTION;
	}
    
}
