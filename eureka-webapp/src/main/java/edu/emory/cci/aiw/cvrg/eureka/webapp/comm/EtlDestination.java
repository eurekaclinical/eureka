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
package edu.emory.cci.aiw.cvrg.eureka.webapp.comm;

import org.eurekaclinical.eureka.client.comm.DestinationType;
import org.eurekaclinical.eureka.client.comm.Link;
import org.eurekaclinical.eureka.client.comm.PhenotypeField;
import org.eurekaclinical.eureka.client.comm.Destination;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * The configuration for a Protempa run, including the input data store
 * information, the output i2b2 data store information, ontology information,
 * and other related items.
 *
 * @author hrathod
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EtlCohortDestination.class, name = "COHORT"),
        @JsonSubTypes.Type(value = EtlI2B2Destination.class, name = "I2B2"),
		@JsonSubTypes.Type(value = EtlNeo4jDestination.class, name = "NEO4J"),
		@JsonSubTypes.Type(value = EtlPatientSetExtractorDestination.class, name="PATIENTSETEXTRACTOR"),
		@JsonSubTypes.Type(value = EtlPatientSetSenderDestination.class, name="PATIENTSETSENDER"),
		@JsonSubTypes.Type(value = EtlTabularFileDestination.class, name="TABULARFILE")
})
public abstract class EtlDestination {

	/**
	 * The unique identifier for the configuration.
	 */
	private Long id;
	private DestinationType type;
	private String name;
	private String description;
	private PhenotypeField[] phenotypeFields;
	/**
	 * The unique identifier for the owner of this configuration.
	 */
	private Long ownerUserId;
	
	private boolean read;
	private boolean write;
	private boolean execute;
	
	@JsonProperty("created_at")
	private Date createdAt;

	@JsonProperty("updated_at")
	private Date updatedAt;
	
	private List<Link> links;
	
	private boolean getStatisticsSupported;
	
	private boolean allowingQueryPropositionIds;
	
	private List<String> requiredPropositionIds;
	
	public EtlDestination() {
		
	}
	
	public EtlDestination(Destination dest) {
		if (dest != null) {
			this.id = dest.getId();
			this.type = dest.getType();
			this.name = dest.getName();
			PhenotypeField[] etlDestPhenotypeFields = 
					dest.getPhenotypeFields();
			if (etlDestPhenotypeFields != null) {
				this.phenotypeFields = etlDestPhenotypeFields.clone();
			}
			this.ownerUserId = dest.getOwnerUserId();
			this.read = dest.isRead();
			this.write = dest.isWrite();
			this.execute = dest.isExecute();
		}
	}

	/**
	 * Get the unique identifier for the configuration.
	 *
	 * @return The unique identifier for the configuration.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the unique identifier for the configuration.
	 *
	 * @param inId The unique identifier for the configuration.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	@JsonIgnore
	public DestinationType getType() {
		return type;
	}

	/**
	 * Get the unique identifier for the owner of the configuration.
	 *
	 * @return The unique identifier for the owner of the configuration.
	 */
	public Long getOwnerUserId() {
		return this.ownerUserId;
	}

	/**
	 * Set the unique identifier for the owner of the configuration.
	 *
	 * @param inUserId The unique identifier for the owner of the configuration.
	 */
	public void setOwnerUserId(Long inUserId) {
		this.ownerUserId = inUserId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PhenotypeField[] getPhenotypeFields() {
		return phenotypeFields;
	}

	public void setPhenotypeFields(PhenotypeField[] phenotypeFields) {
		this.phenotypeFields = phenotypeFields;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public boolean isExecute() {
		return execute;
	}

	public void setExecute(boolean execute) {
		this.execute = execute;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public boolean isGetStatisticsSupported() {
		return getStatisticsSupported;
	}

	public void setGetStatisticsSupported(boolean getStatisticsSupported) {
		this.getStatisticsSupported = getStatisticsSupported;
	}
	
	public abstract void accept(EtlDestinationVisitor etlDestinationVisitor);
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public void setAllowingQueryPropositionIds(boolean allowingQueryPropositionIds) {
		this.allowingQueryPropositionIds = allowingQueryPropositionIds;
	}
	
	public boolean isAllowingQueryPropositionIds() {
		return this.allowingQueryPropositionIds;
	}

	public List<String> getRequiredPropositionIds() {
		return requiredPropositionIds;
	}

	public void setRequiredPropositionIds(List<String> requiredPropositionIds) {
		this.requiredPropositionIds = requiredPropositionIds;
	}
	
}
