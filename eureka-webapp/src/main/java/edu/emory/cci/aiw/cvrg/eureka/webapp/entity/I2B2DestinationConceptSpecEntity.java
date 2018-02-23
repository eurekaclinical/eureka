package edu.emory.cci.aiw.cvrg.eureka.webapp.entity;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "i2b2_dest_conceptspecs", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"proposition", "reference", "property", "i2b2destinations_id"})})
public class I2B2DestinationConceptSpecEntity {

	@Id
	@SequenceGenerator(name = "I2B2_CS_SEQ_GENERATOR",
			sequenceName = "I2B2_CS_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "I2B2_CS_SEQ_GENERATOR")
	private Long id;

	private String proposition;

	private String reference;

	private String property;

	private boolean alreadyLoaded;

	@ManyToOne
	@JoinColumn(name = "i2b2destvaluetypecds_id")
	private I2B2DestinationValueTypeCode valueTypeCode;

	@ManyToOne
	@JoinColumn(name = "i2b2destinations_id")
	private I2B2DestinationEntity destination;

	@OneToMany(cascade = CascadeType.ALL, targetEntity = I2B2DestinationModifierSpecEntity.class, mappedBy = "conceptSpec")
	private List<I2B2DestinationModifierSpecEntity> modifierSpecs;

	public I2B2DestinationConceptSpecEntity() {
		this.modifierSpecs = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getProposition() {
		return proposition;
	}

	public void setProposition(String proposition) {
		this.proposition = proposition;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public List<I2B2DestinationModifierSpecEntity> getModifierSpecs() {
		return new ArrayList<>(this.modifierSpecs);
	}

	public void setModifierSpecs(List<I2B2DestinationModifierSpecEntity> inModifierSpecs) {
		if (inModifierSpecs == null) {
			this.modifierSpecs = new ArrayList<>();
		} else {
			this.modifierSpecs = new ArrayList<>(inModifierSpecs);
		}
	}
	
	public void addModifierSpec(I2B2DestinationModifierSpecEntity inModifierSpec) {
		if (!this.modifierSpecs.contains(inModifierSpec)) {
			this.modifierSpecs.add(inModifierSpec);
			inModifierSpec.setConceptSpec(this);
		}
	}
	
	public void removeModifierSpec(I2B2DestinationModifierSpecEntity inModifierSpec) {
		if (this.modifierSpecs.remove(inModifierSpec)) {
			inModifierSpec.setConceptSpec(null);
		}
	}

	public I2B2DestinationEntity getDestination() {
		return destination;
	}

	public void setDestination(I2B2DestinationEntity destination) {
		this.destination = destination;
	}

	public I2B2DestinationValueTypeCode getValueTypeCode() {
		return valueTypeCode;
	}

	public void setValueTypeCode(I2B2DestinationValueTypeCode valueTypeCode) {
		this.valueTypeCode = valueTypeCode;
	}

	public boolean isAlreadyLoaded() {
		return alreadyLoaded;
	}

	public void setAlreadyLoaded(boolean alreadyLoaded) {
		this.alreadyLoaded = alreadyLoaded;
	}

}
