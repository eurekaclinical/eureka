/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a Protempa allowed value for a <code>org.protempa
 * .LowLevelAbstraction</code>
 */
@Entity
@Table(name = "value_thresholds")
public class ValueThresholdEntity {

	@Id
	@SequenceGenerator(sequenceName = "CONSTRAINT_SEQ", name = "CONSTRAINT_SEQ_GENERATOR", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "CONSTRAINT_SEQ_GENERATOR")
	private Long id;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, 
			CascadeType.MERGE})
	@JoinColumn(nullable = false)
	private DataElementEntity abstractedFrom; 

	private BigDecimal minValueThreshold;
	private String minTValueThreshold;
	
	private String minUnits;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id", nullable = false)
	private ValueComparator minValueComp;

	private BigDecimal maxValueThreshold;
	private String maxTValueThreshold;
	
	private String maxUnits;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id", nullable = false)
	private ValueComparator maxValueComp;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="valuethreshold_id")
	private List<ExtendedDataElement> extendedDataElements;
	
	@ManyToOne
	@JoinColumn(name="relationop_id", referencedColumnName = "id", nullable = false)
	private RelationOperator relationOperator;
	
	private Integer withinAtLeast;
	private Integer withinAtMost;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "id", nullable = false)
	private TimeUnit withinAtMostUnits;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "id", nullable = false)
	private TimeUnit withinAtLeastUnits;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DataElementEntity getAbstractedFrom() {
		return abstractedFrom;
	}

	public void setAbstractedFrom(DataElementEntity abstractedFrom) {
		this.abstractedFrom = abstractedFrom;
	}

	public BigDecimal getMinValueThreshold() {
		return minValueThreshold;
	}

	public void setMinValueThreshold(BigDecimal minValueThreshold) {
		this.minValueThreshold = minValueThreshold;
	}
	
	public String getMinTValueThreshold() {
		return minTValueThreshold;
	}

	public void setMinTValueThreshold(String minTValueThreshold) {
		this.minTValueThreshold = minTValueThreshold;
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

	public BigDecimal getMaxValueThreshold() {
		return maxValueThreshold;
	}

	public void setMaxValueThreshold(BigDecimal maxValueThreshold) {
		this.maxValueThreshold = maxValueThreshold;
	}

	public String getMaxTValueThreshold() {
		return maxTValueThreshold;
	}

	public void setMaxTValueThreshold(String maxTValueThreshold) {
		this.maxTValueThreshold = maxTValueThreshold;
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

	public List<ExtendedDataElement> getExtendedDataElements() {
		return extendedDataElements;
	}

	public void setExtendedDataElements(List<ExtendedDataElement> extendedDataElements) {
		this.extendedDataElements = extendedDataElements;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public void setRelationOperator(RelationOperator relationOperator) {
		this.relationOperator = relationOperator;
	}

	public void setWithinAtLeast(Integer withinAtLeast) {
		this.withinAtLeast = withinAtLeast;
	}

	public void setWithinAtMost(Integer withinAtMost) {
		this.withinAtMost = withinAtMost;
	}

	public void setWithinAtMostUnits(TimeUnit withinAtMostUnits) {
		this.withinAtMostUnits = withinAtMostUnits;
	}

	public void setWithinAtLeastUnits(TimeUnit withinAtLeastUnits) {
		this.withinAtLeastUnits = withinAtLeastUnits;
	}

	public RelationOperator getRelationOperator() {
		return relationOperator;
	}

	public Integer getWithinAtLeast() {
		return withinAtLeast;
	}

	public Integer getWithinAtMost() {
		return withinAtMost;
	}

	public TimeUnit getWithinAtMostUnits() {
		return withinAtMostUnits;
	}

	public TimeUnit getWithinAtLeastUnits() {
		return withinAtLeastUnits;
	}
	
	
}
