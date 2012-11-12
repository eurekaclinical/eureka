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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author hrathod
 */
@Entity
@Table(name = "extended_propositions")
public class ExtendedProposition {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long Id;

	@OneToOne
	private PropertyConstraint propertyConstraint;

	public Long getId() {
		return Id;
	}

	public void setId(Long inId) {
		Id = inId;
	}

	public PropertyConstraint getPropertyConstraint() {
		return propertyConstraint;
	}

	public void setPropertyConstraint(PropertyConstraint
		inPropertyConstraint) {
		propertyConstraint = inPropertyConstraint;
	}
}
