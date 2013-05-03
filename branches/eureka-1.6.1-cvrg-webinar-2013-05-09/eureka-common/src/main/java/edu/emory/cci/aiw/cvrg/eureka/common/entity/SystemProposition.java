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
 z * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity.CategoryType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "system_data_elements")
public class SystemProposition extends DataElementEntity {

	public enum SystemType {

		CONSTANT, EVENT, PRIMITIVE_PARAMETER, LOW_LEVEL_ABSTRACTION,
		COMPOUND_LOW_LEVEL_ABSTRACTION, HIGH_LEVEL_ABSTRACTION,
		SLICE_ABSTRACTION
	}
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
	public void accept(DataElementEntityVisitor visitor) {
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
