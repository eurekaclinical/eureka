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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents messages containing data element definitions.
 *
 * @author hrathod
 */
public class PropositionWrapper implements Serializable {

	public enum Type {
		CATEGORIZATION, SEQUENCE, FREQUENCY, VALUE_THRESHOLD, SYSTEM
	}

	private Long id;
	private String key;
	private Long userId;
	private String abbrevDisplayName;
	private String displayName;
	private Type type;
	private boolean inSystem;
	private List<PropositionWrapper> children;
	private List<String> properties;
	private Date created;
	private Date lastModified;
	private boolean summarized;
	private boolean parent;

	/**
	 * Creates the message object.
	 */
	public PropositionWrapper() {
		children = new ArrayList<PropositionWrapper>();
	}

	/**
	 * Returns a user data element definition's id.
	 *
	 * @return a {@link Long}. Will be <code>null</code> for a system data
	 *         element definition.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets a user data element definition's id.
	 *
	 * @param id
	 *            a {@link Long}.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns a system data element definition's id string.
	 *
	 * @return a {@link String}. Will be <code>null</code> for a user data
	 *         element definition.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets a system data element definition's id string.
	 *
	 * @param inKey
	 *            a {@link String}.
	 */
	public void setKey(String inKey) {
		key = inKey;
	}

	/**
	 * Returns a user data element definition's user id.
	 *
	 * @return a {@link Long}.
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Sets a user data element definition's user id.
	 *
	 * @param userId
	 *            a {@link Long}.
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the data element definition's abbreviated display name.
	 *
	 * @return a {@link String}. May be <code>null</code> if no abbreviated
	 *         display name is specified.
	 */
	public String getAbbrevDisplayName() {
		return abbrevDisplayName;
	}

	/**
	 * Sets the data element definition's abbreviated display name.
	 *
	 * @param abbrevDisplayName
	 *            a {@link String}. Passing in <code>null</code> will set the
	 *            abbreviated display name as unspecified.
	 */
	public void setAbbrevDisplayName(String abbrevDisplayName) {
		this.abbrevDisplayName = abbrevDisplayName;
	}

	/**
	 * Gets the data element definition's display name.
	 *
	 * @return a {@link String}. A value of <code>null</code> means that no
	 *         display name is specified.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the data element definition's display name.
	 *
	 * @param displayName
	 *            a {@link String}. Passing in <code>null</code> will set the
	 *            abbreviated display name as unspecified.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the type of a user data element definition.
	 *
	 * @return a {@link Type}. Will be <code>null</code> if a system data
	 *         element definition.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Sets the type of a user data element definition.
	 *
	 * @param type
	 *            a {@link Type}. Should be <code>null</code> for a system data
	 *            element definition.
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Gets whether this data element definition is a system or user data
	 * element definition.
	 *
	 * @return <code>true</code> if a system data element definition,
	 *         <code>false</code> if a user data element definition.
	 */
	public boolean isInSystem() {
		return inSystem;
	}

	/**
	 * Sets whether this data element definition is a aystem or user data
	 * element definition.
	 *
	 * @param inSystem
	 *            <code>true</code> if a system data element definition,
	 *            <code>false</code> if a user data element definition.
	 */
	public void setInSystem(boolean inSystem) {
		this.inSystem = inSystem;
	}

	/**
	 * Gets the creation date of this data element definition.
	 *
	 * @return the creation {@link Date}, or <code>null</code> if the creation
	 *         date is unspecified.
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * Sets the creation date of this data element definition.
	 *
	 * @param inCreated
	 *            the creation {@link Date}, or <code>null</code> if the
	 *            creation date is unspecified.
	 */
	public void setCreated(Date inCreated) {
		created = inCreated;
	}

	/**
	 * Gets the last modified date of this data element definition.
	 *
	 * @return the last modified {@link Date}, or <code>null</code> if the last
	 *         modified date is unspecified.
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * Sets the last modified date of this data element definition.
	 *
	 * @param inLastModified
	 *            the last modified {@link Date}, or <code>null</code> if the
	 *            last modified date is unspecified.
	 */
	public void setLastModified(Date inLastModified) {
		lastModified = inLastModified;
	}

	/**
	 * Gets whether or not the children of this proposition are included in the
	 * message.
	 *
	 * @return <code>true</code> if the children of this proposition are
	 *         included, <code>false</code> otherwise.
	 */
	public boolean isSummarized() {
		return summarized;
	}

	/**
	 * Sets whether or not the children of this proposition are included in the
	 * message.
	 *
	 * @param inSummarized
	 *            <code>true</code> if the children of this proposition are
	 *            included, <code>false</code> otherwise.
	 */
	public void setSummarized(boolean inSummarized) {
		summarized = inSummarized;
	}

	/**
	 * Gets this data element definition's children, if any.
	 *
	 * @return a {@link List} of this data element definition's children, if
	 *         any.
	 */
	public List<PropositionWrapper> getChildren() {
		return children;
	}

	/**
	 * Sets this data element definition's children.
	 *
	 * @param inChildren
	 *            a {@link List} of this data element definition's children.
	 */
	public void setChildren(List<PropositionWrapper> inChildren) {
		children = inChildren;
	}

	/**
	 * Gets this data element definition's properties, if any.
	 *
	 * @return a {@link List) of this data element definition's properties, if
	 *         any
	 */
	public List<String> getProperties() {
		return properties;
	}

	/**
	 * Sets this data element definition's properties.
	 *
	 * @param inProperties
	 *            a {@link List} of this data element definition's properties.
	 */
	public void setProperties(List<String> inProperties) {
		properties = inProperties;
	}

	/**
	 * Returns whether this data element definition has children.
	 *
	 * @return <code>true</code> if this data element definition has children,
	 *         <code>false</code> otherwise.
	 */
	public boolean isParent() {
		return parent;
	}

	/**
	 * Sets whether this data element definition has children.
	 *
	 * @param inParent
	 *            <code>true</code> if this data element definition has
	 *            children, <code>false</code> otherwise.
	 */
	public void setParent(boolean inParent) {
		parent = inParent;
	}

	/**
	 * Represents field values for debugging purposes.
	 *
	 * @return A string representing the state of the object.
	 */
	@Override
	public String toString() {
		return "PropositionWrapper{" + "id=" + id + ", key='" + key + '\''
		        + ", userId=" + userId + ", abbrevDisplayName='"
		        + abbrevDisplayName + '\'' + ", displayName='" + displayName
		        + '\'' + ", type=" + type + ", inSystem=" + inSystem
		        + ", children=" + children.size() + ", created=" + created
		        + ", lastModified=" + lastModified + ", summarized="
		        + summarized + ", parent=" + parent + '}';
	}
}
