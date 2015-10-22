/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

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
		@JsonSubTypes.Type(value = EtlPatientSetExtractorDestination.class, name="PATIENTSETEXTRACTOR")
})
public abstract class EtlDestination {

	/**
	 * The unique identifier for the configuration.
	 */
	private Long id;
	private DestinationType type;
	private String name;
	private String description;
	private DataElementField[] dataElementFields;
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
			DataElementField[] etlDestDataElementFields = 
					dest.getDataElementFields();
			if (etlDestDataElementFields != null) {
				this.dataElementFields = etlDestDataElementFields.clone();
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

	public DataElementField[] getDataElementFields() {
		return dataElementFields;
	}

	public void setDataElementFields(DataElementField[] dataElementFields) {
		this.dataElementFields = dataElementFields;
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
