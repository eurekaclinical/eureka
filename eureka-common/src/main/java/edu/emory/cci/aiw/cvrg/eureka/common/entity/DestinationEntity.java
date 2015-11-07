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

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "destinations")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DestinationEntity implements ConfigEntity {

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
		return groups;
	}

	public void setGroups(List<DestinationGroupMembership> groups) {
		this.groups = groups;
	}

	public List<LinkEntity> getLinks() {
		return links;
	}

	public void setLinks(List<LinkEntity> links) {
		this.links = links;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getEffectiveAt() {
		return effectiveAt;
	}

	public void setEffectiveAt(Date effectiveAt) {
		this.effectiveAt = effectiveAt;
	}

	public Date getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	public List<DeidPerPatientParams> getOffsets() {
		return offsets;
	}

	public void setOffsets(List<DeidPerPatientParams> offsets) {
		this.offsets = offsets;
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
