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

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @author hrathod
 */
@Entity
@Table(name = "categorizations")
public class Categorization extends Proposition {

	public enum CategorizationType {
		CONSTANT, EVENT, PRIMITIVE_PARAMETER, ABSTRACTION, MIXED, UNKNOWN
	}

	/**
	 * The "children" of of this proposition.
	 */
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH,
	        CascadeType.PERSIST })
	@JoinTable(name = "inverse_is_a")
	private List<Proposition> inverseIsA;

	private CategorizationType categorizationType;

	public List<Proposition> getInverseIsA() {
		return inverseIsA;
	}

	public void setInverseIsA(List<Proposition> inverseIsA) {
		this.inverseIsA = inverseIsA;
	}

	public CategorizationType getCategorizationType() {
		return categorizationType;
	}

	public void setCategorizationType(CategorizationType categorizationType) {
		this.categorizationType = categorizationType;
	}

	@Override
	public void accept(PropositionEntityVisitor visitor) {
		visitor.visit(this);
	}

}
