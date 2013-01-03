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

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * A user-created data element from the UI. Contains fields common to all
 * user-created data elements.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SystemElement.class, name = "system"),
        @JsonSubTypes.Type(value = Category.class, name = "categorization"),
        @JsonSubTypes.Type(value = Sequence.class, name = "sequence"),
        @JsonSubTypes.Type(value = Frequency.class, name = "frequency"),
        @JsonSubTypes.Type(value = ValueThresholds.class, 
		        name = "value_threshold") })
public abstract class DataElement implements DataElementVisitable {

	public enum Type {
		CATEGORIZATION, SEQUENCE, FREQUENCY, VALUE_THRESHOLD, SYSTEM
	}

	private Long id;
	private String key;
	private Long userId;
	private String abbrevDisplayName;
	private String displayName;
	private boolean inSystem;
	private Date created;
	private Date lastModified;
	private boolean summarized;
	private Type type;

	protected DataElement(Type inType) {
		this.type = inType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAbbrevDisplayName() {
		return abbrevDisplayName;
	}

	public void setAbbrevDisplayName(String abbrevDisplayName) {
		this.abbrevDisplayName = abbrevDisplayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isInSystem() {
		return inSystem;
	}

	public void setInSystem(boolean inSystem) {
		this.inSystem = inSystem;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public boolean isSummarized() {
		return summarized;
	}

	public void setSummarized(boolean summarized) {
		this.summarized = summarized;
	}

	@JsonIgnore
	public Type getType() {
		return type;
	}
}
