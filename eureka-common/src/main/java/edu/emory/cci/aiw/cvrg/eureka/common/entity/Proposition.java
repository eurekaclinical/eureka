/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 Emory University
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
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.bind.CycleRecoverable;

/**
 * Holds information about a user-defined ontological concept.
 *
 * @author hrathod
 */
@XmlRootElement
@Entity
@Table(name = "propositions", uniqueConstraints = { @UniqueConstraint
	(columnNames = {"key", "userId"}) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Proposition implements CycleRecoverable,
        PropositionEntityVisitable, Serializable {

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
	private Long userId;
	/**
	 * If proposition is system-level, this key identifies the proposition in
	 * the system ontology
	 */
	@Column(nullable = false)
	private String key;
	/**
	 * The display name for the proposition.
	 */
	private String displayName;
	/**
	 * The abbreviated display name for the proposition.
	 */
	private String abbrevDisplayName;
	/**
	 * Is the proposition a system level proposition?
	 */
	private boolean inSystem;
	/**
	 * The date the proposition was created.
	 */
	private Date created;
	/**
	 * The date the proposition was last modified.
	 */
	private Date lastModified;

	/**
	 * Gets the abbreviated display name of the proposition.
	 *
	 * @return The abbreviated display name of the proposition.
	 */
	public String getAbbrevDisplayName() {
		return abbrevDisplayName;
	}

	/**
	 * Sets the abbreviated display name.
	 *
	 * @param inAbbrevDisplayName
	 *            The abbreviated display name to set.
	 */
	public void setAbbrevDisplayName(String inAbbrevDisplayName) {
		this.abbrevDisplayName = inAbbrevDisplayName;
	}

	/**
	 * Gets the display name of the proposition.
	 *
	 * @return The display name of the proposition.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name of the proposition.
	 *
	 * @param inDisplayName
	 *            The display name of the proposition.
	 */
	public void setDisplayName(String inDisplayName) {
		this.displayName = inDisplayName;
	}

	/**
	 * Gets the user to which this proposition belongs.
	 *
	 * @return The user to which this proposition belongs.
	 */
	public Long getUserId() {
		return this.userId;
	}

	/**
	 * Sets the user to which this proposition belongs.
	 *
	 * @param inUserId
	 *            The user to which this proposition belongs.
	 */
	public void setUserId(Long inUserId) {
		this.userId = inUserId;
	}

	/**
	 * Gets the unique identifier for the proposition.
	 *
	 * @return The unique identifier for the proposition.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the unique identifier for the proposition.
	 *
	 * @param inId
	 *            The unique identifier for the proposition.
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
	 * Gets the key for the proposition.
	 *
	 * @return Null if the proposition is not system level, id in the system
	 *         ontology otherwise.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the key for the proposition.
	 *
	 * @param inKey
	 *            The id of the proposition in the system ontology.
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

	@Override
	public Object onCycleDetected(Context context) {
		return null;
	}

	@Override
	public String toString() {
		return "Proposition{" + "id=" + id + ", user=" + userId
		        + ", displayName=" + displayName + ", abbrevDisplayName="
		        + abbrevDisplayName + '}';
	}
}
