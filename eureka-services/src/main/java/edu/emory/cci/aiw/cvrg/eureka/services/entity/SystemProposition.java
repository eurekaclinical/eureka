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

import org.eurekaclinical.eureka.client.comm.SystemType;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity.CategoryType;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.CategoryEntity.CategoryType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "system_phenotypes")
public class SystemProposition extends PhenotypeEntity {

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SystemType systemType;

	public SystemType getSystemType() {
		return systemType;
	}

	public void setSystemType(SystemType systemType) {
		this.systemType = systemType;
		setCatType(inferCategoryType());
	}

	@Override
	public void accept(PhenotypeEntityVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean isInSystem() {
		return true;
	}
	
	private CategoryType inferCategoryType() {
		if (systemType != null) {
			switch (systemType) {
				case HIGH_LEVEL_ABSTRACTION:
					return CategoryType.HIGH_LEVEL_ABSTRACTION;
				case CONSTANT:
					return CategoryType.CONSTANT;
				case EVENT:
					return CategoryType.EVENT;
				case PRIMITIVE_PARAMETER:
					return CategoryType.PRIMITIVE_PARAMETER;
				case LOW_LEVEL_ABSTRACTION:
					return CategoryType.LOW_LEVEL_ABSTRACTION;
				case SLICE_ABSTRACTION:
					return CategoryType.SLICE_ABSTRACTION;
				case SEQUENTIAL_TEMPORAL_PATTERN_ABSTRACTION:
					return CategoryType.SEQUENTIAL_TEMPORAL_PATTERN_ABSTRACTION;
				case CONTEXT:
					return CategoryType.CONTEXT;
				default:
					throw new AssertionError(
							"Invalid system type: " + systemType);
			}
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
