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
        DataElementEntityVisitor {

	private List<? extends DataElementEntity> children;

	public List<? extends DataElementEntity> getChildren() {
		return children;
	}

	@Override
	public void visit(SystemProposition proposition) {
		this.children = new ArrayList<DataElementEntity>();
	}

	@Override
	public void visit(CategoryEntity categorization) {
		this.children = categorization.getMembers();
	}

	@Override
	public void visit(SequenceEntity highLevelAbstraction) {
		this.children = highLevelAbstraction.getAbstractedFrom();
	}

	@Override
	public void visit(ValueThresholdGroupEntity lowLevelAbstraction) {
		List<DataElementEntity> cs = new ArrayList<DataElementEntity>();
		for (ValueThresholdEntity v : lowLevelAbstraction.getValueThresholds()) {
			cs.add(v.getAbstractedFrom());
		}
		this.children = cs;
	}

	@Override
	public void visit(FrequencyEntity sliceAbstraction) {
		this.children = Collections.singletonList(sliceAbstraction
		        .getAbstractedFrom());
	}

}
