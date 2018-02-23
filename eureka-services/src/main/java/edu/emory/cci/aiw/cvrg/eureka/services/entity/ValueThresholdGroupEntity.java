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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;


//import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity.CategoryType;

import javax.persistence.ManyToOne;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.CategoryEntity.CategoryType;

/**
 * Contains attributes which describe a Protempa low-level abstraction in the
 * context of the Eureka! UI.
 */
@Entity
@Table(name = "value_threshold_groups")
public class ValueThresholdGroupEntity extends PhenotypeEntity {

	/*
	 * The allowed values of the low-level abstraction
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "valuethresholdsgroup_id")
	private List<ValueThresholdEntity> valueThresholds;
	
	@ManyToOne
	@JoinColumn(name = "thresholdsop_id", referencedColumnName = "id", nullable = false)
	private ThresholdsOperator thresholdsOperator;
	
	public ValueThresholdGroupEntity() {
		super(CategoryType.LOW_LEVEL_ABSTRACTION);
	}

	public List<ValueThresholdEntity> getValueThresholds() {
		return valueThresholds;
	}

	public void setValueThresholds(List<ValueThresholdEntity> valueThresholds) {
		this.valueThresholds = valueThresholds;
	}

	public ThresholdsOperator getThresholdsOperator() {
		return thresholdsOperator;
	}

	public void setThresholdsOperator(ThresholdsOperator thresholdsOp) {
		this.thresholdsOperator = thresholdsOp;
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
