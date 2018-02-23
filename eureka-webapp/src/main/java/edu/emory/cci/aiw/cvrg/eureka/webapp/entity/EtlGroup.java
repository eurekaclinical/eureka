package edu.emory.cci.aiw.cvrg.eureka.webapp.entity;

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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "groups")
public class EtlGroup {
	@Id
	@SequenceGenerator(name = "GROUPS_SEQ_GENERATOR", sequenceName = "GROUPS_SEQ",
	allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator = "GROUPS_SEQ_GENERATOR")
	private Long id;
	
	private String name;
	
	@ManyToMany(mappedBy="groups")
	private List<AuthorizedUserEntity> users;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="group")
	private List<SourceConfigGroupMembership> sourceConfigs;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="destination")
	private List<DestinationGroupMembership> destinations;

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

	public List<AuthorizedUserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<AuthorizedUserEntity> users) {
		this.users = users;
	}

	public List<SourceConfigGroupMembership> getSourceConfigs() {
		return sourceConfigs;
	}

	public void setSourceConfigs(
			List<SourceConfigGroupMembership> sourceConfigs) {
		this.sourceConfigs = sourceConfigs;
	}

	public List<DestinationGroupMembership> getDestinations() {
		return new ArrayList<>(this.destinations);
	}

	public void setDestinations(List<DestinationGroupMembership> inDestinations) {
		if (inDestinations == null) {
			this.destinations = new ArrayList<>();
		} else {
			this.destinations = new ArrayList<>(inDestinations);
			for (DestinationGroupMembership destination : this.destinations) {
				destination.setGroup(this);
			}
		}
	}
	
	public void addDestination(DestinationGroupMembership inDestination) {
		if (!this.destinations.contains(inDestination)) {
			this.destinations.add(inDestination);
			inDestination.setGroup(this);
		}
	}
	
	public void removeDestination(DestinationGroupMembership inDestination) {
		if (this.destinations.remove(inDestination)) {
			inDestination.setGroup(null);
		}
	}
	
	
}
