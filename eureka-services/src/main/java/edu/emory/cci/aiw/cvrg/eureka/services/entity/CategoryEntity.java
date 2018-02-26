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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author hrathod
 */
@Entity
@Table(name = "categories")
public class CategoryEntity extends PhenotypeEntity {

	public enum CategoryType {
		CONSTANT, EVENT, PRIMITIVE_PARAMETER, LOW_LEVEL_ABSTRACTION, 
		COMPOUND_LOW_LEVEL_ABSTRACTION, HIGH_LEVEL_ABSTRACTION, SLICE_ABSTRACTION,
		VALUE_THRESHOLD, SEQUENTIAL_TEMPORAL_PATTERN_ABSTRACTION, CONTEXT
	}

	/**
	 * The "children" of of this proposition.
	 */
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH,
	        CascadeType.PERSIST })
	@JoinTable(name = "category_members", 
			joinColumns = { @JoinColumn(name = "category_id")},
			inverseJoinColumns = { @JoinColumn(name = "member_id")})
	private List<PhenotypeEntity> members;
	
	public CategoryEntity() {
		super(CategoryType.EVENT);
	}

	public List<PhenotypeEntity> getMembers() {
		return members;
	}

	public void setMembers(List<PhenotypeEntity> members) {
		this.members = members;
	}
	
	public void setCategoryType(CategoryType categoryType) {
		setCatType(categoryType);
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
