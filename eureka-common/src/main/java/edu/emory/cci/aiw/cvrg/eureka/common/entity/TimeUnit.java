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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author hrathod
 */
@Entity
@Table(name = "time_units")
public class TimeUnit {

	@Id
	@SequenceGenerator(name = "TIME_SEQ_GENERATOR",
		sequenceName = "TIME_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
		generator = "TIME_SEQ_GENERATOR")
	private Long id;

	@Column(unique=true, nullable=false)
	private String name;

	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long inId) {
		id = inId;
	}

	public String getName() {
		return name;
	}

	public void setName(String inName) {
		name = inName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String inDescription) {
		description = inDescription;
	}
}
