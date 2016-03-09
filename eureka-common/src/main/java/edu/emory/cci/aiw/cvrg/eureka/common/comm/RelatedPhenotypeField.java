/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author Andrew Post
 */
public final class RelatedDataElementField {
	private DataElementField dataElementField;
	private Long relationOperator;
	private String sequentialDataElement;
	private Long sequentialDataElementSource;
	private Integer relationMinCount;
	private Long relationMinUnits;
	private Integer relationMaxCount;
	private Long relationMaxUnits;

	public DataElementField getDataElementField() {
		return dataElementField;
	}

	public void setDataElementField(DataElementField dataElement) {
		this.dataElementField = dataElement;
	}

	public Long getRelationOperator() {
		return relationOperator;
	}

	public void setRelationOperator(Long relationOperator) {
		this.relationOperator = relationOperator;
	}

	public String getSequentialDataElement() {
		return sequentialDataElement;
	}

	public void setSequentialDataElement(String rhsDataElement) {
		this.sequentialDataElement = rhsDataElement;
	}

	public Integer getRelationMinCount() {
		return relationMinCount;
	}

	public Long getSequentialDataElementSource() {
		return sequentialDataElementSource;
	}

	public void setSequentialDataElementSource(Long inSequentialDataElementSource) {
		sequentialDataElementSource = inSequentialDataElementSource;
	}

	public void setRelationMinCount(Integer relationMinCount) {
		this.relationMinCount = relationMinCount;
	}

	public Long getRelationMinUnits() {
		return relationMinUnits;
	}

	public void setRelationMinUnits(Long relationMinUnits) {
		this.relationMinUnits = relationMinUnits;
	}

	public Integer getRelationMaxCount() {
		return relationMaxCount;
	}

	public void setRelationMaxCount(Integer relationMaxCount) {
		this.relationMaxCount = relationMaxCount;
	}

	public Long getRelationMaxUnits() {
		return relationMaxUnits;
	}

	public void setRelationMaxUnits(Long relationMaxUnits) {
		this.relationMaxUnits = relationMaxUnits;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
    
}
