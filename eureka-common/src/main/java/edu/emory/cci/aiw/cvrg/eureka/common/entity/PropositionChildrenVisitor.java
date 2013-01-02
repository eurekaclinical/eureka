/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PropositionChildrenVisitor implements
        PropositionEntityVisitor {

	private List<? extends Proposition> children;

	public List<? extends Proposition> getChildren() {
		return children;
	}

	@Override
	public void visit(SystemProposition proposition) {
		this.children = new ArrayList<Proposition>();
	}

	@Override
	public void visit(Categorization categorization) {
		this.children = categorization.getInverseIsA();
	}

	@Override
	public void visit(HighLevelAbstraction highLevelAbstraction) {
		this.children = highLevelAbstraction.getAbstractedFrom();
	}

	@Override
	public void visit(ValueThresholdEntity lowLevelAbstraction) {
		this.children = lowLevelAbstraction.getAbstractedFrom();
	}

	@Override
	public void visit(CompoundValueThreshold compoundLowLevelAbstraction) {
		this.children = compoundLowLevelAbstraction.getAbstractedFrom();
	}

	@Override
	public void visit(SliceAbstraction sliceAbstraction) {
		this.children = Collections.singletonList(sliceAbstraction
		        .getAbstractedFrom());
	}

}
