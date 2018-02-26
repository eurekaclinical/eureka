/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.entity;

 
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.CategoryEntity.CategoryType;

/**
 * Contains attributes which describe a Protempa slice abstraction in the
 * context of the Eureka! UI.
 */
@Entity
@Table(name = "frequencies")
public class FrequencyEntity extends PhenotypeEntity {

	@Column(nullable = false)
	private Integer count;

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
	private ExtendedPhenotype extendedPhenotype;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private FrequencyType frequencyType;

	public FrequencyEntity() {
		super(CategoryType.SLICE_ABSTRACTION);
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public boolean isConsecutive() {
		return consecutive;
	}

	public void setConsecutive(boolean consecutive) {
		this.consecutive = consecutive;
	}

	public PhenotypeEntity getAbstractedFrom() {
		return extendedPhenotype != null ? extendedPhenotype
		        .getPhenotypeEntity() : null;
	}

	public ExtendedPhenotype getExtendedProposition() {
		return extendedPhenotype;
	}

	public void setExtendedProposition(ExtendedPhenotype extendedProposition) {
		this.extendedPhenotype = extendedProposition;
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

	public FrequencyType getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(FrequencyType frequencyType) {
		this.frequencyType = frequencyType;
	}
	
	@Override
	public void accept(PhenotypeEntityVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}

}
