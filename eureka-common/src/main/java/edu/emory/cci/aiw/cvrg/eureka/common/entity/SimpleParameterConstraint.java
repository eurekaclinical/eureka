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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Represents a Protempa allowed value for a {@link LowLevelAbstraction}
 */
@Entity
@Table(name = "simple_parameter_constraints")
public final class SimpleParameterConstraint {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;

	private Number minValueThreshold;

	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private ValueComparator minValueComp;

	private Number maxValueThreshold;

	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private ValueComparator maxValueComp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Number getMinValueThreshold() {
		return minValueThreshold;
	}

	public void setMinValueThreshold(int minValueThreshold) {
		this.minValueThreshold = minValueThreshold;
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

	public void setMaxValueThreshold(int maxValueThreshold) {
		this.maxValueThreshold = maxValueThreshold;
	}

	public ValueComparator getMaxValueComp() {
		return maxValueComp;
	}

	public void setMaxValueComp(ValueComparator maxValueComp) {
		this.maxValueComp = maxValueComp;
	}

}
