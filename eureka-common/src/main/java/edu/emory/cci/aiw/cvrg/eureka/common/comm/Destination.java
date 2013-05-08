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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The configuration for a Protempa run, including the input data store
 * information, the output i2b2 data store information, ontology information,
 * and other related items.
 *
 * @author hrathod
 *
 */
public class Destination {

	public static enum Type {
		I2B2;
	}
	/**
	 * The unique identifier for the configuration.
	 */
	private String id;
	private Type type;
	private String displayName;
	private DataElementField[] dataElementFields;
	/**
	 * The unique identifier for the owner of this configuration.
	 */
	private Long ownerUserId;

	/**
	 * Get the unique identifier for the configuration.
	 *
	 * @return The unique identifier for the configuration.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Set the unique identifier for the configuration.
	 *
	 * @param inId The unique identifier for the configuration.
	 */
	public void setId(String inId) {
		this.id = inId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Get the unique identifier for the owner of the configuration.
	 *
	 * @return The unique identifier for the owner of the configuration.
	 */
	public Long getOwnerUserId() {
		return this.ownerUserId;
	}

	/**
	 * Set the unique identifier for the owner of the configuration.
	 *
	 * @param inUserId The unique identifier for the owner of the configuration.
	 */
	public void setOwnerUserId(Long inUserId) {
		this.ownerUserId = inUserId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public DataElementField[] getDataElementFields() {
		return dataElementFields;
	}

	public void setDataElementFields(DataElementField[] dataElementFields) {
		this.dataElementFields = dataElementFields;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
