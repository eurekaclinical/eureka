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
package edu.emory.cci.aiw.cvrg.eureka.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;



import javax.persistence.Lob;
import javax.persistence.Temporal;
import org.apache.commons.lang3.builder.ToStringBuilder;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.CategoryEntity.CategoryType;

/**
 * Holds information about a user-defined ontological concept.
 * 
 * @author hrathod
 */
@Entity
@Table(name = "phenotypes", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "key", "user_id" }) })
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PhenotypeEntity implements PropositionEntityVisitable, Serializable {

	/**
	 * Needed for the Serializable implementation.
	 */
	private static final long serialVersionUID = -4972778121056149836L;
	/**
	 * The unique identifier for the Proposition.
	 */
	@Id
	@SequenceGenerator(name = "PROP_SEQ_GENERATOR", sequenceName = "PROP_SEQ", allocationSize = 1)
	@GeneratedValue(generator = "PROP_SEQ_GENERATOR")
	private Long id;
	/**
	 * The user to which this proposition belongs.
	 */
	@Column(nullable = false, name="user_id")
	private Long userId;
	/**
	 * If proposition is system-level, this key identifies the phenotype in
	 * the system ontology
	 */
	@Column(nullable = false)
	private String key;
	/**
	 * The display name for the phenotype.
	 */
	private String displayName;
	/**
	 * A description of the phenotype.
	 */
	
	/**
	 * The phenotype's description. The name is prefixed with a z to force
	 * hibernate to populate this field last in insert and update statements
	 * to avoid the dreaded 
	 * <code>ORA-24816: Expanded non LONG bind data supplied after actual LONG or LOB column</code>
	 * error message from Oracle. Hibernate apparently orders fields 
	 * alphabetically.
	 */
	@Lob
	@Column(name="description")
	private String zDescription;
	/**
	 * Is the proposition a system level phenotype?
	 */
	private boolean inSystem;
	/**
	 * The date the phenotype was created.
	 */
	/*@Column(nullable = false)*/
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date created;
	/**
	 * The date the phenotype was last modified.
	 */
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date lastModified;
	
	@Column(nullable = false)
	private CategoryType categoryType;
	
	protected PhenotypeEntity() {
		this(null);
	}

	protected PhenotypeEntity(CategoryType categoryType) {
		this.categoryType = categoryType;
	}
	
	/**
	 * Gets the phenotype's description.
	 * 
	 * @return The phenotype's description.
	 */
	public String getDescription() {
		return zDescription;
	}

	/**
	 * Sets the phenotype's description.
	 * 
	 * @param inDescription
	 *            The phenotype's description to set.
	 */
	public void setDescription(String inDescription) {
		this.zDescription = inDescription;
	}

	/**
	 * Gets the display name of the phenotype.
	 * 
	 * @return The display name of the phenotype.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name of the phenotype.
	 * 
	 * @param inDisplayName
	 *            The display name of the phenotype.
	 */
	public void setDisplayName(String inDisplayName) {
		this.displayName = inDisplayName;
	}

	/**
	 * Gets the user to which this phenotype belongs.
	 * 
	 * @return The user to which this phenotype belongs.
	 */
	public Long getUserId() {
		return this.userId;
	}

	/**
	 * Sets the user to which this phenotype belongs.
	 * 
	 * @param inUserId
	 *            The user to which this phenotype belongs.
	 */
	public void setUserId(Long inUserId) {
		this.userId = inUserId;
	}

	/**
	 * Gets the unique identifier for the phenotype.
	 * 
	 * @return The unique identifier for the phenotype.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the unique identifier for the phenotype.
	 * 
	 * @param inId
	 *            The unique identifier for the phenotype.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Is the proposition a system level element?
	 * 
	 * @return True if system level, false otherwise.
	 */
	public boolean isInSystem() {
		return inSystem;
	}

	/**
	 * Sets whether the proposition is a system level element.
	 * 
	 * @param inSystem
	 *            The value of the inSystem property to set.
	 */
	public void setInSystem(boolean inSystem) {
		this.inSystem = inSystem;
	}

	/**
	 * Gets the key for the phenotype.
	 * 
	 * @return Null if the phenotype is not system level, id in the system
	 *         ontology otherwise.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the key for the phenotype.
	 * 
	 * @param inKey
	 *            The id of the phenotype in the system ontology.
	 */
	public void setKey(String inKey) {
		key = inKey;
	}

	/**
	 * Gets the creation date.
	 * 
	 * @return The creation date.
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * Sets the creation time.
	 * 
	 * @param inCreated
	 *            The creation time.
	 */
	public void setCreated(Date inCreated) {
		created = inCreated;
	}

	/**
	 * Gets the last modification time.
	 * 
	 * @return The last modification time.
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * Sets the last modification time.
	 * 
	 * @param inLastModified
	 *            The last modification time.
	 */
	public void setLastModified(Date inLastModified) {
		lastModified = inLastModified;
	}

	protected void setCatType(CategoryType categoryType) {
		this.categoryType = categoryType;
	}
	
	public CategoryType getCategoryType() {
		return this.categoryType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
