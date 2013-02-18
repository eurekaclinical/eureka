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

import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

public final class ValueThreshold {
	
	private DataElementField dataElement;
	private Long lowerComp;
	private Long upperComp;
	
	private String lowerValue;
	
	private String upperValue;
	
	private String lowerUnits;
	private String upperUnits;
	private Long relationOperator;
	private List<DataElementField> relatedDataElements;
	private Integer withinAtLeast;
	private Long withinAtLeastUnit;
	private Integer withinAtMost;
	private Long withinAtMostUnit;

	public DataElementField getDataElement() {
		return dataElement;
	}

	public void setDataElement(DataElementField dataElement) {
		this.dataElement = dataElement;
	}

	public Long getLowerComp() {
		return lowerComp;
	}

	public void setLowerComp(Long lowerComp) {
		this.lowerComp = lowerComp;
	}

	public Long getUpperComp() {
		return upperComp;
	}

	public void setUpperComp(Long upperComp) {
		this.upperComp = upperComp;
	}

	public String getLowerValue() {
		return lowerValue;
	}

	public void setLowerValue(String lowerValue) {
		this.lowerValue = lowerValue;
	}

	public String getUpperValue() {
		return upperValue;
	}

	public void setUpperValue(String upperValue) {
		this.upperValue = upperValue;
	}

	public String getLowerUnits() {
		return lowerUnits;
	}

	public void setLowerUnits(String lowerUnits) {
		this.lowerUnits = lowerUnits;
	}

	public String getUpperUnits() {
		return upperUnits;
	}

	public void setUpperUnits(String upperUnits) {
		this.upperUnits = upperUnits;
	}

	public Long getRelationOperator() {
		return relationOperator;
	}

	public void setRelationOperator(Long relationOperator) {
		this.relationOperator = relationOperator;
	}

	public List<DataElementField> getRelatedDataElements() {
		return relatedDataElements;
	}

	public void setRelatedDataElements(List<DataElementField> relatedDataElements) {
		this.relatedDataElements = relatedDataElements;
	}

	public Integer getWithinAtLeast() {
		return withinAtLeast;
	}

	public void setWithinAtLeast(Integer atLeastCount) {
		this.withinAtLeast = atLeastCount;
	}

	public Long getWithinAtLeastUnit() {
		return withinAtLeastUnit;
	}

	public void setWithinAtLeastUnit(Long atLeastTimeUnit) {
		this.withinAtLeastUnit = atLeastTimeUnit;
	}

	public Integer getWithinAtMost() {
		return withinAtMost;
	}

	public void setWithinAtMost(Integer atMostCount) {
		this.withinAtMost = atMostCount;
	}

	public Long getWithinAtMostUnit() {
		return withinAtMostUnit;
	}

	public void setWithinAtMostUnit(Long atMostTimeUnit) {
		this.withinAtMostUnit = atMostTimeUnit;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
