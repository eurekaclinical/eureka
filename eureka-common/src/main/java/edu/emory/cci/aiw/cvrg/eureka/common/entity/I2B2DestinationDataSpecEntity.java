package edu.emory.cci.aiw.cvrg.eureka.common.entity;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "i2b2_dest_dataspecs", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "i2b2destinations_id"})})
public class I2B2DestinationDataSpecEntity {
	@Id
	@SequenceGenerator(name = "I2B2_DS_SEQ_GENERATOR",
		sequenceName = "I2B2_DS_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
		generator = "I2B2_DS_SEQ_GENERATOR")
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	private String reference;
	
	private String property;
	
	private String conceptCodePrefix;
	
	@ManyToOne
	@JoinColumn(name="i2b2destinations_id")
	private I2B2DestinationEntity destination;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private I2B2DestinationIntervalSide start;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private I2B2DestinationIntervalSide finish;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getConceptCodePrefix() {
		return conceptCodePrefix;
	}

	public void setConceptCodePrefix(String conceptCodePrefix) {
		this.conceptCodePrefix = conceptCodePrefix;
	}

	public I2B2DestinationIntervalSide getStart() {
		return start;
	}

	public void setStart(I2B2DestinationIntervalSide start) {
		this.start = start;
	}

	public I2B2DestinationIntervalSide getFinish() {
		return finish;
	}

	public void setFinish(I2B2DestinationIntervalSide finish) {
		this.finish = finish;
	}

	public I2B2DestinationEntity getDestination() {
		return destination;
	}

	public void setDestination(I2B2DestinationEntity destination) {
		this.destination = destination;
	}
	
	
}
