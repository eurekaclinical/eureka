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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author hrathod
 */
@Entity
@Table(name = "value_comparators")
public class ValueComparator {
	
	public static enum Threshold {
		LOWER_ONLY,
		BOTH,
		UPPER_ONLY
	}

	@Id
	@SequenceGenerator(name = "COMP_SEQ_GENERATOR",
			sequenceName = "COMP_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "COMP_SEQ_GENERATOR")
	private Long id;

	@Column(nullable = false)
	private String name;

	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Threshold threshold;
	
	@Column(nullable = false)
	private Long rank;

	@OneToOne
	@JsonIgnore // Needed due to infinite recursion without it. Maybe there's another way?
	private ValueComparator complement;

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

	public ValueComparator getComplement() {
		return complement;
	}
	
	public void setComplement(ValueComparator inComplement) {
		complement = inComplement;
	}
	
	public Threshold getThreshold() {
		return threshold;
	}
	
	public void setThreshold(Threshold threshold) {
		this.threshold = threshold;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
