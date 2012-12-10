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

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Contains attributes which describe a Protempa slice abstraction in the
 * context of the Eureka! UI.
 */
@Entity
@Table(name = "slice_abstractions")
public class SliceAbstraction extends Proposition {

	private Integer minIndex;

	@OneToOne
	Proposition abstractedFrom;

	@OneToOne
	ExtendedProposition extendedProposition;

	public Integer getMinIndex() {
		return minIndex;
	}

	public void setMinIndex(Integer minIndex) {
		this.minIndex = minIndex;
	}

	public Proposition getAbstractedFrom() {
		return abstractedFrom;
	}

	public void setAbstractedFrom(Proposition abstractedFrom) {
		this.abstractedFrom = abstractedFrom;
	}

	public ExtendedProposition getExtendedProposition() {
		return extendedProposition;
	}

	public void setExtendedProposition(ExtendedProposition extendedProposition) {
		this.extendedProposition = extendedProposition;
	}

	@Override
	public void accept(PropositionEntityVisitor visitor) {
		visitor.visit(this);
	}

}
