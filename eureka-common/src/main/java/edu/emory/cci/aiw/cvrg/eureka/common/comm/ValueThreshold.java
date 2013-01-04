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

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

public final class ValueThreshold {

	/**
	 * Parses an empty string for a Number field as null.
	 */
	public static class NumberDeserializer extends JsonDeserializer<Number> {

		@Override
		public Number deserialize(JsonParser jp, DeserializationContext dc)
				throws IOException, JsonProcessingException {
			System.err.println("NumberSerializer: " + jp.getText());
			String strippedText = StringUtils.trimToNull(jp.getText());
			if (strippedText == null) {
				return null;
			} else {
				try {
					return NumberFormat.getInstance().parse(strippedText);
				} catch (ParseException ex) {
					throw new JsonMappingException("Not a number: " + 
							strippedText);
				}
			}
		}
	}
	
	private ShortDataElementField dataElement;
	private Long lowerComp;
	private Long upperComp;
	
	@JsonDeserialize(using = NumberDeserializer.class)
	private Number lowerValue;
	
	@JsonDeserialize(using = NumberDeserializer.class)
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
