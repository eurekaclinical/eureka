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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author mmansour
 * @author hrathod
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataElementField {

	public enum Type {

		CATEGORIZATION, SEQUENCE, FREQUENCY, VALUE_THRESHOLD, SYSTEM
	}
	
	private String dataElementKey;
	private String dataElementAbbrevDisplayName;
	private String dataElementDisplayName;
	private String withValue;
	private Boolean hasDuration;
	private Integer minDuration;
	private Long minDurationUnits;
	private Integer maxDuration;
	private Long maxDurationUnits;
	private Boolean hasPropertyConstraint;
	private String property;
	private String propertyValue;
	private Type type;

	public String getDataElementKey() {
		return dataElementKey;
	}

	public void setDataElementKey(String dataElement) {
		this.dataElementKey = dataElement;
	}

	public String getWithValue() {
		return withValue;
	}

	public void setWithValue(String withValue) {
		this.withValue = withValue;
	}

	public Boolean getHasDuration() {
		return hasDuration;
	}

	public void setHasDuration(Boolean hasDuration) {
		this.hasDuration = hasDuration;
	}

	public Integer getMinDuration() {
		return minDuration;
	}

	public void setMinDuration(Integer minDuration) {
		this.minDuration = minDuration;
	}

	public Long getMinDurationUnits() {
		return minDurationUnits;
	}

	public void setMinDurationUnits(Long minDurationUnits) {
		this.minDurationUnits = minDurationUnits;
	}

	public Integer getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(Integer maxDuration) {
		this.maxDuration = maxDuration;
	}

	public Long getMaxDurationUnits() {
		return maxDurationUnits;
	}

	public void setMaxDurationUnits(Long maxDurationUnits) {
		this.maxDurationUnits = maxDurationUnits;
	}

	public Boolean getHasPropertyConstraint() {
		return hasPropertyConstraint;
	}

	public void setHasPropertyConstraint(Boolean hasPropertyConstraint) {
		this.hasPropertyConstraint = hasPropertyConstraint;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String getDataElementAbbrevDisplayName() {
		return dataElementAbbrevDisplayName;
	}

	public void setDataElementAbbrevDisplayName(String inDataElementAbbrevDisplayName) {
		dataElementAbbrevDisplayName = inDataElementAbbrevDisplayName;
	}

	public String getDataElementDisplayName() {
		return dataElementDisplayName;
	}

	public void setDataElementDisplayName(String inDataElementDisplayName) {
		dataElementDisplayName = inDataElementDisplayName;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isInSystem() {
		return this.type == Type.SYSTEM;
	}

	@Override
	public String toString() {
		return "DataElementField{"
				+ "dataElementKey='" + dataElementKey + '\''
				+ ", dataElementAbbrevDisplayName='"
				+ dataElementAbbrevDisplayName + '\''
				+ ", dataElementDisplayName='" + dataElementDisplayName + '\''
				+ ", withValue='" + withValue + '\''
				+ ", hasDuration=" + hasDuration
				+ ", minDuration=" + minDuration
				+ ", minDurationUnits=" + minDurationUnits
				+ ", maxDuration=" + maxDuration
				+ ", maxDurationUnits=" + maxDurationUnits
				+ ", hasPropertyConstraint=" + hasPropertyConstraint
				+ ", property='" + property + '\''
				+ ", propertyValue='" + propertyValue + '\''
				+ '}';
	}
}
