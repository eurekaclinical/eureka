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
package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A bean class to hold information about users in the system.
 *
 * @author Andrew Post
 *
 */
@Entity
@Table(name = "users")
public class EtlUserEntity {

	/**
	 * The user's unique identifier.
	 */
	@Id
	@SequenceGenerator(name = "USER_SEQ_GENERATOR", sequenceName = "USER_SEQ",
	allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator = "USER_SEQ_GENERATOR")
	private Long id;
	
	/**
	 * The user's email address.
	 */
	@Column(unique = true, nullable = false)
	private String username;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "id")
	private List<JobEntity> jobs;
	
	@ManyToMany
	private List<EtlGroup> groups;
	
	@OneToMany(mappedBy = "owner")
	private List<SourceConfigEntity> sourceConfigs;
	
	@OneToMany(mappedBy = "owner")
	private List<DestinationEntity> destinations;
	
	/**
	 * Create an empty User object.
	 */
	public EtlUserEntity() {
	}

	/**
	 * Get the user's unique identifier.
	 *
	 * @return A {@link Long} representing the user's unique identifier.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the user's unique identifier.
	 *
	 * @param inId A {@link Long} representing the user's unique identifier.
	 */
	public void setId(final Long inId) {
		this.id = inId;
	}

	/**
	 * Get the user's email address.
	 *
	 * @return A String containing the user's email address.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Set the user's email address.
	 *
	 * @param inUsername A String containing the user's email address.
	 */
	public void setUsername(final String inUsername) {
		this.username = inUsername;
	}

	public List<JobEntity> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobEntity> jobs) {
		this.jobs = jobs;
	}

	public List<EtlGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<EtlGroup> groups) {
		this.groups = groups;
	}

	public List<SourceConfigEntity> getSourceConfigs() {
		return sourceConfigs;
	}

	public void setSourceConfigs(List<SourceConfigEntity> sourceConfigs) {
		this.sourceConfigs = sourceConfigs;
	}

	public List<DestinationEntity> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<DestinationEntity> destinations) {
		this.destinations = destinations;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
