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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Contains attributes which describe a Protempa compound low-level abstraction in the context of the Eureka! UI
 */
@Entity
@Table(name = "compound_value_threshold")
public class CompoundValueThreshold extends Proposition {
	
	
	private Integer minimumNumberOfValues;
	private Integer minimumGapBetweenValues;

	@ManyToOne
	@JoinColumn(name = "minGapBtwValuesUnits_id", referencedColumnName = "id")
	private TimeUnit minimumGapBetweenValuesUnits;

	private Integer maximumGapBetweenValues;

	@ManyToOne
	@JoinColumn(name = "maxGapBtwValuesUnits_id", referencedColumnName = "id")
	private TimeUnit maximumGapBetweenValuesUnits;

	private String userValueDefinitionName;
	private String complementValueDefinitionName;

	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private ThresholdsOperator thresholdsOperator;

	public enum CreatedFrom {
		FREQUENCY, RESULTS_THRESHOLD
	}

	private CreatedFrom createdFrom;

	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH,
			CascadeType.PERSIST})
	@JoinTable(name = "cvt_abstracted_from", joinColumns = {@JoinColumn(name =
			"target_proposition_id")})
	private List<ValueThresholdEntity> abstractedFrom;

	public Integer getMinimumNumberOfValues() {
		return minimumNumberOfValues;
	}

	public void setMinimumNumberOfValues(Integer minimumNumberOfValues) {
		this.minimumNumberOfValues = minimumNumberOfValues;
	}

	public Integer getMinimumGapBetweenValues() {
		return minimumGapBetweenValues;
	}

	public void setMinimumGapBetweenValues(Integer minimumGapBetweenValues) {
		this.minimumGapBetweenValues = minimumGapBetweenValues;
	}

	public TimeUnit getMinimumGapBetweenValuesUnits() {
		return minimumGapBetweenValuesUnits;
	}

	public void setMinimumGapBetweenValuesUnits(
			TimeUnit minimumGapBetweenValuesUnits) {
		this.minimumGapBetweenValuesUnits = minimumGapBetweenValuesUnits;
	}

	public Integer getMaximumGapBetweenValues() {
		return maximumGapBetweenValues;
	}

	public void setMaximumGapBetweenValues(Integer maximumGapBetweenValues) {
		this.maximumGapBetweenValues = maximumGapBetweenValues;
	}

	public TimeUnit getMaximumGapBetweenValuesUnits() {
		return maximumGapBetweenValuesUnits;
	}

	public void setMaximumGapBetweenValuesUnits(
			TimeUnit maximumGapBetweenValuesUnits) {
		this.maximumGapBetweenValuesUnits = maximumGapBetweenValuesUnits;
	}

	public String getUserValueDefinitionName() {
		return userValueDefinitionName;
	}

	public void setUserValueDefinitionName(String userValueDefinitionName) {
		this.userValueDefinitionName = userValueDefinitionName;
	}

	public String getComplementValueDefinitionName() {
		return complementValueDefinitionName;
	}

	public void setComplementValueDefinitionName(
			String complementValueDefinitionName) {
		this.complementValueDefinitionName = complementValueDefinitionName;
	}
	public ThresholdsOperator getThresholdsOperator() {
		return thresholdsOperator;
	}

	public void setThresholdsOperator(ThresholdsOperator thresholdsOperator) {
		this.thresholdsOperator = thresholdsOperator;
	}

	public CreatedFrom getCreatedFrom() {
		return createdFrom;
	}

	public void setCreatedFrom(CreatedFrom createdFrom) {
		this.createdFrom = createdFrom;
	}

	public List<ValueThresholdEntity> getAbstractedFrom() {
		return abstractedFrom;
	}

	public void setAbstractedFrom(List<ValueThresholdEntity> abstractedFrom) {
		this.abstractedFrom = abstractedFrom;
	}

	@Override
	public void accept(PropositionEntityVisitor visitor) {
		visitor.visit(this);
	}

}
