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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Contains attributes which describe a Protempa low-level abstraction in the
 * context of the Eureka! UI.
 */
@Entity
@Table(name = "value_thresholds")
public class ValueThresholdEntity extends Proposition {

	@Column(nullable = false)
	private String name;

	/*
	 * Minimum number of values to match. Default value is 1.
	 */
	private int minValues = 1;

	/*
	 * Minimum allowed time gap between values
	 */
	private Integer minGapValues;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private TimeUnit minGapValuesUnits;

	/*
	 * Maximum allowed time gap between values
	 */
	private Integer maxGapValues;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private TimeUnit maxGapValuesUnits;

	/*
	 * The propositions that this low-level abstraction is abstracted from.
	 */
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH,
	        CascadeType.PERSIST }, optional = false)
	@JoinTable(name = "abstracted_from", joinColumns = { @JoinColumn(name = "target_proposition_id") })
	private Proposition abstractedFrom;

	/*
	 * The allowed values of the low-level abstraction
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userConstraint", referencedColumnName = "id", nullable = false)
	private SimpleParameterConstraint userConstraint;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "complementConstraint", referencedColumnName = "id", nullable = false)
	private SimpleParameterConstraint complementConstraint;
	
	@OneToOne
	@JoinColumn(name = "thresholdsOp", referencedColumnName = "id", nullable = false)
	private ThresholdsOperator thresholdsOperator;

	public enum CreatedFrom {
		FREQUENCY, VALUE_THRESHOLD
	}

	@Column(nullable = false)
	private CreatedFrom createdFrom;

	public String getName() {
		return name;
	}

	public void setName(String inName) {
		name = inName;
	}

	public int getMinValues() {
		return minValues;
	}
	
	public void setMinValues(int minValues) {
		this.minValues = minValues;
	}

	public Integer getMinGapValues() {
		return minGapValues;
	}

	public void setMinGapValues(Integer minGapValues) {
		this.minGapValues = minGapValues;
	}

	public TimeUnit getMinGapValuesUnits() {
		return minGapValuesUnits;
	}

	public void setMinGapValuesUnits(TimeUnit minGapValuesUnits) {
		this.minGapValuesUnits = minGapValuesUnits;
	}

	public Integer getMaxGapValues() {
		return maxGapValues;
	}

	public void setMaxGapValues(Integer maxGapValues) {
		this.maxGapValues = maxGapValues;
	}

	public TimeUnit getMaxGapValuesUnits() {
		return maxGapValuesUnits;
	}

	public void setMaxGapValuesUnits(TimeUnit maxGapValuesUnits) {
		this.maxGapValuesUnits = maxGapValuesUnits;
	}

	public Proposition getAbstractedFrom() {
		return abstractedFrom;
	}

	public void setAbstractedFrom(Proposition abstractedFrom) {
		this.abstractedFrom = abstractedFrom;
	}

	public SimpleParameterConstraint getUserConstraint() {
		return userConstraint;
	}

	public void setUserConstraint(SimpleParameterConstraint userConstraint) {
		this.userConstraint = userConstraint;
	}

	public SimpleParameterConstraint getComplementConstraint() {
		return complementConstraint;
	}

	public void setComplementConstraint(
	        SimpleParameterConstraint complementConstraint) {
		this.complementConstraint = complementConstraint;
	}

	public ThresholdsOperator getThresholdsOperator() {
		return thresholdsOperator;
	}

	public void setThresholdsOperator(ThresholdsOperator thresholdsOp) {
		this.thresholdsOperator = thresholdsOp;
	}

	public CreatedFrom getCreatedFrom() {
		return createdFrom;
	}

	public void setCreatedFrom(CreatedFrom createdFrom) {
		this.createdFrom = createdFrom;
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
