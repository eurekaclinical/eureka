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

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * An ExtendedPhenotype represents a phenotype with constraints on its
 * duration and one of its property values.
 * 
 * @author hrathod, Miao Ai
 */
@Entity
@Table(name = "extended_phenotypes")
@TableGenerator(name = "EXT_DE_GENERATOR")
public class ExtendedPhenotype {

	@Id
	@SequenceGenerator(name = "EXT_DE_SEQ_GENERATOR",
		sequenceName = "EXT_DE_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
		generator = "EXT_DE_SEQ_GENERATOR")
	private Long id;

	private Integer minDuration;

        @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "lhsExtendedPhenotype")   
        private List<Relation> lhsExtendedPhenotype_relations;
        
        @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "rhsExtendedPhenotype")        
        private List<Relation> rhsExtendedPhenotype_relations;
                
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
	@JoinColumn(nullable = false, name = "phenotype_id")
	private PhenotypeEntity phenotypeEntity;

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

	public PhenotypeEntity getPhenotypeEntity() {
		return phenotypeEntity;
	}

	public void setPhenotypeEntity(PhenotypeEntity inPhenotypeEntity) {
		phenotypeEntity = inPhenotypeEntity;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
