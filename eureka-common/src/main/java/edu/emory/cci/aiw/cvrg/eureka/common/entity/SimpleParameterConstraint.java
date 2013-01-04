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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Represents a Protempa allowed value for a {@link LowLevelAbstraction}
 */
@Entity
@Table(name = "simple_parameter_constraints")
public class SimpleParameterConstraint {

	@Id
	@SequenceGenerator(sequenceName = "CONSTRAINT_SEQ", 
			name = "CONSTRAINT_SEQ_GENERATOR", allocationSize = 1, 
			initialValue = 1)
	@GeneratedValue(generator = "CONSTRAINT_SEQ_GENERATOR")
	private Long id;

	private Number minValueThreshold;
	private String minUnits;
	
	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private ValueComparator minValueComp;
	
	private Number maxValueThreshold;
	private String maxUnits;

	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private ValueComparator maxValueComp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Number getMinValueThreshold() {
		return minValueThreshold;
	}

	public void setMinValueThreshold(Number minValueThreshold) {
		this.minValueThreshold = minValueThreshold;
	}
	
	public String getMinUnits() {
		return minUnits;
	}
	
	public void setMinUnits(String minUnits) {
		this.minUnits = minUnits;
	}

	public ValueComparator getMinValueComp() {
		return minValueComp;
	}

	public void setMinValueComp(ValueComparator minValueComp) {
		this.minValueComp = minValueComp;
	}

	public Number getMaxValueThreshold() {
		return maxValueThreshold;
	}
	
	public void setMaxValueThreshold(Number maxValueThreshold) {
		this.maxValueThreshold = maxValueThreshold;
	}

	public String getMaxUnits() {
		return maxUnits;
	}
	
	public void setMaxUnits(String maxUnits) {
		this.maxUnits = maxUnits;
	}
	
	public ValueComparator getMaxValueComp() {
		return maxValueComp;
	}

	public void setMaxValueComp(ValueComparator maxValueComp) {
		this.maxValueComp = maxValueComp;
	}

}
