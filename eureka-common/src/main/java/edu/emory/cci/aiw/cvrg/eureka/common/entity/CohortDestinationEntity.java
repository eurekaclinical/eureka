package edu.emory.cci.aiw.cvrg.eureka.common.entity;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "cohort_destinations")
public class CohortDestinationEntity extends DestinationEntity {
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(nullable = false)
	private CohortEntity cohort;

	public CohortEntity getCohort() {
		return cohort;
	}

	public void setCohort(CohortEntity cohort) {
		this.cohort = cohort;
	}

	@Override
	public void accept(DestinationEntityVisitor visitor) {
		visitor.visit(this);
	}
	
}
