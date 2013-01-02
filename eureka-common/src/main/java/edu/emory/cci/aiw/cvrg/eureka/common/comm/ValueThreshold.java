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

public final class ValueThreshold {

	private ShortDataElementField dataElement;
	private Long lowerComp;
	private Long upperComp;
	private Number lowerValue;
	private Number upperValue;
	private String lowerUnits;
	private String upperUnits;
	private Boolean isBeforeOrAfter;
	private Long relationOperator;
	private List<DataElement> relatedDataElements;
	private Integer atLeastCount;
	private Long atLeastTimeUnit;
	private Integer atMostCount;
	private Long atMostTimeUnit;

	public ShortDataElementField getDataElement() {
		return dataElement;
	}

	public void setDataElement(ShortDataElementField dataElement) {
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

	public Number getLowerValue() {
		return lowerValue;
	}

	public void setLowerValue(Number lowerValue) {
		this.lowerValue = lowerValue;
	}

	public Number getUpperValue() {
		return upperValue;
	}

	public void setUpperValue(Number upperValue) {
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

	public Boolean getIsBeforeOrAfter() {
		return isBeforeOrAfter;
	}

	public void setIsBeforeOrAfter(Boolean isBeforeOrAfter) {
		this.isBeforeOrAfter = isBeforeOrAfter;
	}

	public Long getRelationOperator() {
		return relationOperator;
	}

	public void setRelationOperator(Long relationOperator) {
		this.relationOperator = relationOperator;
	}

	public List<DataElement> getRelatedDataElements() {
		return relatedDataElements;
	}

	public void setRelatedDataElements(List<DataElement> relatedDataElements) {
		this.relatedDataElements = relatedDataElements;
	}

	public Integer getAtLeastCount() {
		return atLeastCount;
	}

	public void setAtLeastCount(Integer atLeastCount) {
		this.atLeastCount = atLeastCount;
	}

	public Long getAtLeastTimeUnit() {
		return atLeastTimeUnit;
	}

	public void setAtLeastTimeUnit(Long atLeastTimeUnit) {
		this.atLeastTimeUnit = atLeastTimeUnit;
	}

	public Integer getAtMostCount() {
		return atMostCount;
	}

	public void setAtMostCount(Integer atMostCount) {
		this.atMostCount = atMostCount;
	}

	public Long getAtMostTimeUnit() {
		return atMostTimeUnit;
	}

	public void setAtMostTimeUnit(Long atMostTimeUnit) {
		this.atMostTimeUnit = atMostTimeUnit;
	}

}
