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

/**
 * Container class for the sequence user-created data element from the UI.
 * Essentially a direct mapping from the sequence element form fields.
 */
public final class Sequence extends DataElement {

	private DataElementField primaryDataElement;
	private List<RelatedDataElementField> relatedDataElements;

	public DataElementField getPrimaryDataElement() {
		return primaryDataElement;
	}

	public void setPrimaryDataElement(DataElementField primaryDataElement) {
		this.primaryDataElement = primaryDataElement;
	}

	public List<RelatedDataElementField> getRelatedDataElements() {
		return relatedDataElements;
	}

	public void setRelatedDataElements(
	        List<RelatedDataElementField> relatedDataElements) {
		this.relatedDataElements = relatedDataElements;
	}

	public enum RelationOperator {
		BEFORE, AFTER, INVALID;
	}

	public static class RelatedDataElementField {
		private DataElementField dataElementField;
		private RelationOperator relationOperator;
		private String sequentialDataElement;
		private Integer relationMinCount;
		private String relationMinUnits;
		private Integer relationMaxCount;
		private String relationMaxUnits;

		public DataElementField getDataElementField() {
			return dataElementField;
		}

		public void setDataElementField(DataElementField dataElement) {
			this.dataElementField = dataElement;
		}

		public RelationOperator getRelationOperator() {
			return relationOperator;
		}

		public void setRelationOperator(RelationOperator relationOperator) {
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

		public void setRelationMinCount(Integer relationMinCount) {
			this.relationMinCount = relationMinCount;
		}

		public String getRelationMinUnits() {
			return relationMinUnits;
		}

		public void setRelationMinUnits(String relationMinUnits) {
			this.relationMinUnits = relationMinUnits;
		}

		public Integer getRelationMaxCount() {
			return relationMaxCount;
		}

		public void setRelationMaxCount(Integer relationMaxCount) {
			this.relationMaxCount = relationMaxCount;
		}

		public String getRelationMaxUnits() {
			return relationMaxUnits;
		}

		public void setRelationMaxUnits(String relationMaxUnits) {
			this.relationMaxUnits = relationMaxUnits;
		}
	}

	public static class DataElementField {
		private String dataElementKey;
		private String withValue;
		private Boolean hasDuration;
		private Integer minDuration;
		private String minDurationUnits;
		private Integer maxDuration;
		private String maxDurationUnits;
		private Boolean hasPropertyConstraint;
		private String property;
		private String propertyValue;

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

		public String getMinDurationUnits() {
			return minDurationUnits;
		}

		public void setMinDurationUnits(String minDurationUnits) {
			this.minDurationUnits = minDurationUnits;
		}

		public Integer getMaxDuration() {
			return maxDuration;
		}

		public void setMaxDuration(Integer maxDuration) {
			this.maxDuration = maxDuration;
		}

		public String getMaxDurationUnits() {
			return maxDurationUnits;
		}

		public void setMaxDurationUnits(String maxDurationUnits) {
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
	}

	@Override
	public void accept(DataElementVisitor visitor) {
		visitor.visit(this);
	}
}
