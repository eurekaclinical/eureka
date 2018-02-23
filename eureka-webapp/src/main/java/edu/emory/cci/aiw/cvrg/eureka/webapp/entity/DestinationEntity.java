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
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.ws.rs.core.MediaType;
import org.eurekaclinical.standardapis.entity.HistoricalEntity;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "destinations")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DestinationEntity implements ConfigEntity, HistoricalEntity<Long> {

	@Id
	@SequenceGenerator(name = "DEST_SEQ_GENERATOR", sequenceName = "DEST_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "DEST_SEQ_GENERATOR")
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Lob
	private String description;

	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;

	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name = "effective_at")
	private Date effectiveAt;

	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name = "expired_at")
	private Date expiredAt;

	@ManyToOne
	private AuthorizedUserEntity owner;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "destination")
	private List<DestinationGroupMembership> groups;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "destination")
	private List<LinkEntity> links;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "destination")
	private List<DeidPerPatientParams> offsets;

	private boolean deidentificationEnabled;

	@ManyToOne
	private EncryptionAlgorithm encryptionAlgorithm;

	private String outputName;

	private String outputType;

	public DestinationEntity() {
		this.outputType = MediaType.APPLICATION_OCTET_STREAM;
		this.groups = new ArrayList<>();
		this.offsets = new ArrayList<>();
		this.links = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public AuthorizedUserEntity getOwner() {
		return owner;
	}

	@Override
	public void setOwner(AuthorizedUserEntity owner) {
		this.owner = owner;
	}

	public List<DestinationGroupMembership> getGroups() {
		return new ArrayList<>(groups);
	}

	public void setGroups(List<DestinationGroupMembership> inGroups) {
		if (inGroups == null) {
			this.groups = new ArrayList<>();
		} else {
			this.groups = new ArrayList<>(inGroups);
			for (DestinationGroupMembership group : inGroups) {
				group.setDestination(this);
			}
		}
	}
	
	public void addGroup(DestinationGroupMembership inGroup) {
		if (!this.groups.contains(inGroup)) {
			this.groups.add(inGroup);
			inGroup.setDestination(this);
		}
	}

	public void removeGroup(DestinationGroupMembership inGroup) {
		if (this.groups.remove(inGroup)) {
			inGroup.setDestination(null);
		}
	}
	
	public List<LinkEntity> getLinks() {
		return new ArrayList<>(links);
	}

	public void setLinks(List<LinkEntity> inLinks) {
		if (inLinks == null) {
			this.links = new ArrayList<>();
		} else {
			this.links = new ArrayList<>(inLinks);
			for (LinkEntity group : inLinks) {
				group.setDestination(this);
			}
		}
	}
	
	public void addLink(LinkEntity inLink) {
		if (!this.links.contains(inLink)) {
			this.links.add(inLink);
			inLink.setDestination(this);
		}
	}
	
	public void removeLink(LinkEntity inLink) {
		if (this.links.remove(inLink)) {
			inLink.setDestination(null);
		}
	}

	@Override
	public Date getCreatedAt() {
		return createdAt;
	}

	@Override
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public Date getEffectiveAt() {
		return effectiveAt;
	}

	@Override
	public void setEffectiveAt(Date effectiveAt) {
		this.effectiveAt = effectiveAt;
	}

	@Override
	public Date getExpiredAt() {
		return expiredAt;
	}

	@Override
	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	public List<DeidPerPatientParams> getOffsets() {
		return new ArrayList<>(offsets);
	}

	public void setOffsets(List<DeidPerPatientParams> inOffsets) {
		if (inOffsets == null) {
			this.offsets = new ArrayList<>();
		} else {
			this.offsets = new ArrayList<>(inOffsets);
			for (DeidPerPatientParams offset : this.offsets) {
				offset.setDestination(this);
			}
		}
	}
	
	public void addOffset(DeidPerPatientParams inOffset) {
		if (!this.offsets.contains(inOffset)) {
			this.offsets.add(inOffset);
			inOffset.setDestination(this);
		}
	}
	
	public void removeOffset(DeidPerPatientParams inOffset) {
		if (this.offsets.remove(inOffset)) {
			inOffset.setDestination(null);
		}
	}

	public void setDeidentificationEnabled(boolean enabled) {
		this.deidentificationEnabled = enabled;
	}

	public boolean isDeidentificationEnabled() {
		return this.deidentificationEnabled;
	}

	public EncryptionAlgorithm getEncryptionAlgorithm() {
		return encryptionAlgorithm;
	}

	public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
		this.encryptionAlgorithm = encryptionAlgorithm;
	}

	public String getOutputName() {
		return this.outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}

	public String getOutputType() {
		return outputType;
	}

	public void setOutputType(String outputType) {
		if (outputType == null) {
			this.outputType = MediaType.APPLICATION_OCTET_STREAM;
		} else {
			this.outputType = outputType;
		}
	}

	public abstract boolean isGetStatisticsSupported();

	public abstract boolean isAllowingQueryPropositionIds();

	public abstract void accept(DestinationEntityVisitor visitor);

}
