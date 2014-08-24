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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Category.CategoricalType;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement.Type;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author mmansour
 * @author hrathod
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataElementField {

	private Long id;
	private String dataElementKey;
	private String dataElementDescription;
	private String dataElementDisplayName;
	private Boolean hasDuration;
	private Integer minDuration;
	private Long minDurationUnits;
	private Integer maxDuration;
	private Long maxDurationUnits;
	private Boolean hasPropertyConstraint;
	private String property;
	private String propertyValue;
	private Type type;
	private CategoricalType categoricalType;

	public DataElementField() {

	}

	public DataElementField(DataElement dataElement) {
		this.id = dataElement.getId();
		this.dataElementKey = dataElement.getKey();
		this.dataElementDescription = dataElement.getDescription();
		this.dataElementDisplayName = dataElement.getDisplayName();
		this.type = dataElement.getType();
		if (dataElement instanceof Category) {
			this.categoricalType = ((Category) dataElement).getCategoricalType();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long inId) {
		id = inId;
	}

	public String getDataElementKey() {
		return dataElementKey;
	}

	public void setDataElementKey(String dataElement) {
		this.dataElementKey = dataElement;
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

	public String getDataElementDescription() {
		return dataElementDescription;
	}

	public void setDataElementDescription(String inDataElementDescription) {
		dataElementDescription = inDataElementDescription;
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

	public CategoricalType getCategoricalType() {
		return categoricalType;
	}

	public void setCategoricalType(CategoricalType categoricalType) {
		this.categoricalType = categoricalType;
	}

	public boolean isInSystem() {
		return this.type == Type.SYSTEM;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
