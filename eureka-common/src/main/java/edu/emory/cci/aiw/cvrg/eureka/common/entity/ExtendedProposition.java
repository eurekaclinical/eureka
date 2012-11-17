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
 * @author hrathod
 */
@Entity
@Table(name = "extended_propositions")
public class ExtendedProposition {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String Id;

	private Integer minDuration;

	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private TimeUnit minDurationTimeUnit;

	private Integer maxDuration;

	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private TimeUnit maxDurationTimeUnit;

	@OneToOne
	private PropertyConstraint propertyConstraint;

	@OneToOne
	private Proposition proposition;

	public String getId() {
		return Id;
	}

	public void setId(String inId) {
		Id = inId;
	}

	public Integer getMinDuration() {
		return minDuration;
	}

	public void setMinDuration(Integer minDuration) {
		this.minDuration = minDuration;
	}

	public TimeUnit getMinDurationTimeUnit() {
		return minDurationTimeUnit;
	}

	public void setMinDurationTimeUnit(TimeUnit minDurationTimeUnit) {
		this.minDurationTimeUnit = minDurationTimeUnit;
	}

	public Integer getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(Integer maxDuration) {
		this.maxDuration = maxDuration;
	}

	public TimeUnit getMaxDurationTimeUnit() {
		return maxDurationTimeUnit;
	}

	public void setMaxDurationTimeUnit(TimeUnit maxDurationTimeUnit) {
		this.maxDurationTimeUnit = maxDurationTimeUnit;
	}

	public PropertyConstraint getPropertyConstraint() {
		return propertyConstraint;
	}

	public void setPropertyConstraint(PropertyConstraint inPropertyConstraint) {
		propertyConstraint = inPropertyConstraint;
	}

	public Proposition getProposition() {
		return proposition;
	}

	public void setProposition(Proposition inProposition) {
		proposition = inProposition;
	}
}
