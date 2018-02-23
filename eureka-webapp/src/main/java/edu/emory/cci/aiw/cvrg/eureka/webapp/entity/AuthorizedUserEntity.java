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
package edu.emory.cci.aiw.cvrg.eureka.webapp.entity;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.eurekaclinical.standardapis.entity.UserEntity;

/**
 * A bean class to hold information about users in the system.
 *
 * @author Andrew Post
 *
 */
@Entity
@Table(name = "users")
public class AuthorizedUserEntity implements UserEntity<AuthorizedRoleEntity> {

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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<JobEntity> jobs;

	@ManyToMany
	private List<EtlGroup> groups;

	@OneToMany(mappedBy = "owner")
	private List<SourceConfigEntity> sourceConfigs;

	@OneToMany(mappedBy = "owner")
	private List<DestinationEntity> destinations;

	@ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
	@JoinTable(joinColumns=@JoinColumn(name="user_id"), inverseJoinColumns=@JoinColumn(name="role_id"))
	private List<AuthorizedRoleEntity> roles;

	/**
	 * Create an empty User object.
	 */
	public AuthorizedUserEntity() {
		this.jobs = new ArrayList<>();
		this.roles = new ArrayList<>();
		this.sourceConfigs = new ArrayList<>();
		this.destinations = new ArrayList<>();
	}

	/**
	 * Get the user's unique identifier.
	 *
	 * @return A {@link Long} representing the user's unique identifier.
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the user's unique identifier.
	 *
	 * @param inId A {@link Long} representing the user's unique identifier.
	 */
	@Override
	public void setId(final Long inId) {
		this.id = inId;
	}

	/**
	 * Get the user's email address.
	 *
	 * @return A String containing the user's email address.
	 */
	@Override
	public String getUsername() {
		return this.username;
	}

	/**
	 * Set the user's email address.
	 *
	 * @param inUsername A String containing the user's email address.
	 */
	@Override
	public void setUsername(final String inUsername) {
		this.username = inUsername;
	}

	public List<JobEntity> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobEntity> jobs) {
		if (jobs == null) {
			this.jobs = new ArrayList<>();
		} else {
			this.jobs = jobs;
		}
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
		if (sourceConfigs == null) {
			this.sourceConfigs = new ArrayList<>();
		} else {
			this.sourceConfigs = sourceConfigs;
		}
	}

	public List<DestinationEntity> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<DestinationEntity> destinations) {
		if (destinations == null) {
			this.destinations = new ArrayList<>();
		} else {
			this.destinations = destinations;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public List<AuthorizedRoleEntity> getRoles() {
		return this.roles;
	}

	@Override
	public void setRoles(List<AuthorizedRoleEntity> inRoles) {
		if (inRoles == null) {
			this.roles = new ArrayList<>();
		} else {
			this.roles = inRoles;
		}
	}

	@Override
	public void addRole(AuthorizedRoleEntity role) {
		this.roles.add(role);
	}

	@Override
	public void removeRole(AuthorizedRoleEntity role) {
		this.roles.remove(role);
	}
	
	
}
