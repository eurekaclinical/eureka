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

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ValueThresholds extends DataElement {

	private String name;
	private Long thresholdsOperator;
	private List<ValueThreshold> valueThresholds;

	public ValueThresholds() {
		super(Type.VALUE_THRESHOLD);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getThresholdsOperator() {
		return thresholdsOperator;
	}

	public void setThresholdsOperator(Long thresholdsOperator) {
		this.thresholdsOperator = thresholdsOperator;
	}

	public List<ValueThreshold> getValueThresholds() {
		return valueThresholds;
	}

	public void setValueThresholds(List<ValueThreshold> valueThresholds) {
		this.valueThresholds = valueThresholds;
	}

	@Override
	public void accept(DataElementVisitor visitor) 
			throws DataElementHandlingException{
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}

}
