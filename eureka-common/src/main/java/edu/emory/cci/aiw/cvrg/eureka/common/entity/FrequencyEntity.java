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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity.CategorizationType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Contains attributes which describe a Protempa slice abstraction in the
 * context of the Eureka! UI.
 */
@Entity
@Table(name = "frequencies")
public class FrequencyEntity extends DataElementEntity {

	@Column(nullable = false)
	private Integer atLeastCount;

	private boolean consecutive;

	private Integer withinAtLeast;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private TimeUnit withinAtLeastUnits;

	private Integer withinAtMost;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private TimeUnit withinAtMostUnits;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false)
	private ExtendedProposition extendedProposition;

	public FrequencyEntity() {
		super(CategorizationType.SLICE_ABSTRACTION);
	}

	public Integer getAtLeastCount() {
		return atLeastCount;
	}

	public void setAtLeastCount(Integer atLeastCount) {
		this.atLeastCount = atLeastCount;
	}

	public boolean isConsecutive() {
		return consecutive;
	}

	public void setConsecutive(boolean consecutive) {
		this.consecutive = consecutive;
	}

	public DataElementEntity getAbstractedFrom() {
		return extendedProposition != null ? extendedProposition
		        .getProposition() : null;
	}

	public ExtendedProposition getExtendedProposition() {
		return extendedProposition;
	}

	public void setExtendedProposition(ExtendedProposition extendedProposition) {
		this.extendedProposition = extendedProposition;
	}

	public Integer getWithinAtLeast() {
		return this.withinAtLeast;
	}

	public void setWithinAtLeast(Integer duration) {
		this.withinAtLeast = duration;
	}

	public TimeUnit getWithinAtLeastUnits() {
		return withinAtLeastUnits;
	}

	public void setWithinAtLeastUnits(TimeUnit withinAtLeastUnits) {
		this.withinAtLeastUnits = withinAtLeastUnits;
	}

	public Integer getWithinAtMost() {
		return this.withinAtMost;
	}

	public void setWithinAtMost(Integer duration) {
		this.withinAtMost = duration;
	}

	public TimeUnit getWithinAtMostUnits() {
		return withinAtMostUnits;
	}

	public void setWithinAtMostUnits(TimeUnit withinAtMostUnits) {
		this.withinAtMostUnits = withinAtMostUnits;
	}

	@Override
	public void accept(PropositionEntityVisitor visitor) {
		visitor.visit(this);
	}

}
