package edu.emory.cci.aiw.cvrg.eureka.common.entity;

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
	private List<EtlUserEntity> users;
	
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

	public List<EtlUserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<EtlUserEntity> users) {
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
		return destinations;
	}

	public void setDestinations(List<DestinationGroupMembership> destinations) {
		this.destinations = destinations;
	}
	
	
}
