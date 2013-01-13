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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author hrathod
 */
@Entity
@Table(name = "categorizations")
public class CategoryEntity extends DataElementEntity {

	public enum CategorizationType {
		CONSTANT, EVENT, PRIMITIVE_PARAMETER, LOW_LEVEL_ABSTRACTION, 
		COMPOUND_LOW_LEVEL_ABSTRACTION, HIGH_LEVEL_ABSTRACTION, SLICE_ABSTRACTION,
		VALUE_THRESHOLD, MIXED, UNKNOWN
	}

	/**
	 * The "children" of of this proposition.
	 */
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH,
	        CascadeType.PERSIST })
	@JoinTable(name = "cat_inverse_is_a", joinColumns = { @JoinColumn(name = "target_proposition_id") })
	private List<DataElementEntity> inverseIsA;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CategorizationType categorizationType;
	
	public CategoryEntity() {
		super(CategorizationType.EVENT);
	}

	public List<DataElementEntity> getInverseIsA() {
		return inverseIsA;
	}

	public void setInverseIsA(List<DataElementEntity> inverseIsA) {
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
