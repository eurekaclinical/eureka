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
@Table(name = "relations")
public class Relation {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	private Long lhsExtendedProposition;

	private Long rhsExtendedProposition;

	private Long minf1s2;

	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private TimeUnit minf1s2TimeUnit;

	private Long maxf1s2;

	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private TimeUnit maxf1s2TimeUnit;

	private Long mins1f2;

	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	TimeUnit mins1f2TimeUnit;

	private Long maxs1f2;

	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	TimeUnit maxs1f2TimeUnit;

	public Long getId() {
		return id;
	}

	public void setId(Long inId) {
		id = inId;
	}

	public Long getLhsExtendedProposition() {
		return lhsExtendedProposition;
	}

	public void setLhsExtendedProposition(Long inLhsExtendedProposition) {
		lhsExtendedProposition = inLhsExtendedProposition;
	}

	public Long getRhsExtendedProposition() {
		return rhsExtendedProposition;
	}

	public void setRhsExtendedProposition(Long inRhsExtendedProposition) {
		rhsExtendedProposition = inRhsExtendedProposition;
	}

	public Long getMinf1s2() {
		return minf1s2;
	}

	public void setMinf1s2(Long inMinf1s2) {
		minf1s2 = inMinf1s2;
	}

	public TimeUnit getMinf1s2TimeUnit() {
		return minf1s2TimeUnit;
	}

	public void setMinf1s2TimeUnit(TimeUnit inMinf1s2TimeUnit) {
		minf1s2TimeUnit = inMinf1s2TimeUnit;
	}

	public Long getMaxf1s2() {
		return maxf1s2;
	}

	public void setMaxf1s2(Long inMaxf1s2) {
		maxf1s2 = inMaxf1s2;
	}

	public TimeUnit getMaxf1s2TimeUnit() {
		return maxf1s2TimeUnit;
	}

	public void setMaxf1s2TimeUnit(TimeUnit inMaxf1s2TimeUnit) {
		maxf1s2TimeUnit = inMaxf1s2TimeUnit;
	}

	public Long getMins1f2() {
		return mins1f2;
	}

	public void setMins1f2(Long inMins1f2) {
		mins1f2 = inMins1f2;
	}

	public TimeUnit getMins1f2TimeUnit() {
		return mins1f2TimeUnit;
	}

	public void setMins1f2TimeUnit(TimeUnit inMins1f2TimeUnit) {
		mins1f2TimeUnit = inMins1f2TimeUnit;
	}

	public Long getMaxs1f2() {
		return maxs1f2;
	}

	public void setMaxs1f2(Long inMaxs1f2) {
		maxs1f2 = inMaxs1f2;
	}

	public TimeUnit getMaxs1f2TimeUnit() {
		return maxs1f2TimeUnit;
	}

	public void setMaxs1f2TimeUnit(TimeUnit inMaxs1f2TimeUnit) {
		maxs1f2TimeUnit = inMaxs1f2TimeUnit;
	}
}
