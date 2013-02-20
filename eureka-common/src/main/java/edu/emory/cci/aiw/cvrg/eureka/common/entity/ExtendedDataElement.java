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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An ExtendedDataElement represents a data element with constraints on its
 * duration and one of its property values.
 * 
 * @author hrathod
 */
@Entity
@Table(name = "extended_data_elements")
@TableGenerator(name = "EXT_DE_GENERATOR")
public class ExtendedDataElement {

	@Id
	@SequenceGenerator(name = "EXT_DE_SEQ_GENERATOR",
		sequenceName = "EXT_DE_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
		generator = "EXT_DE_SEQ_GENERATOR")
	private Long id;

	private Integer minDuration;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id", nullable=false)
	private TimeUnit minDurationTimeUnit;

	private Integer maxDuration;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id", nullable=false)
	private TimeUnit maxDurationTimeUnit;

	@OneToOne(cascade = CascadeType.ALL)
	private PropertyConstraint propertyConstraint;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH,
	        CascadeType.PERSIST })
	@JoinColumn(nullable = false, name = "dataelement_id")
	private DataElementEntity dataElementEntity;

	public Long getId() {
		return id;
	}

	public void setId(Long inId) {
		id = inId;
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

	public DataElementEntity getDataElementEntity() {
		return dataElementEntity;
	}

	public void setDataElementEntity(DataElementEntity inDataElementEntity) {
		dataElementEntity = inDataElementEntity;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
