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

import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public final class Frequency extends DataElement {

	private Integer atLeast;
	private Boolean isConsecutive;
	private DataElementField dataElement;
	private Boolean isWithin;
	private Integer withinAtLeast;
	private Long withinAtLeastUnits;
	private Integer withinAtMost;
	private Long withinAtMostUnits;

	public Frequency() {
		super(Type.FREQUENCY);
	}

	public Integer getAtLeast() {
		return atLeast;
	}

	public void setAtLeast(Integer atLeast) {
		this.atLeast = atLeast;
	}

	public Boolean getIsConsecutive() {
		return isConsecutive;
	}

	public void setIsConsecutive(Boolean isConsecutive) {
		this.isConsecutive = isConsecutive;
	}

	public DataElementField getDataElement() {
		return dataElement;
	}

	public void setDataElement(DataElementField dataElement) {
		this.dataElement = dataElement;
	}

	public Boolean getIsWithin() {
		return isWithin;
	}

	public void setIsWithin(Boolean isWithin) {
		this.isWithin = isWithin;
	}

	public Integer getWithinAtLeast() {
		return withinAtLeast;
	}

	public void setWithinAtLeast(Integer withinAtLeast) {
		this.withinAtLeast = withinAtLeast;
	}

	public Long getWithinAtLeastUnits() {
		return withinAtLeastUnits;
	}

	public void setWithinAtLeastUnits(Long withinAtLeastUnits) {
		this.withinAtLeastUnits = withinAtLeastUnits;
	}

	public Integer getWithinAtMost() {
		return withinAtMost;
	}

	public void setWithinAtMost(Integer withinAtMost) {
		this.withinAtMost = withinAtMost;
	}

	public Long getWithinAtMostUnits() {
		return withinAtMostUnits;
	}

	public void setWithinAtMostUnits(Long withinAtMostUnits) {
		this.withinAtMostUnits = withinAtMostUnits;
	}

	@Override
	public void accept(DataElementVisitor visitor) 
			throws DataElementHandlingException {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}

}
