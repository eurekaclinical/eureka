package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.List;

public final class Sequence extends DataElement {

	private DataElement primaryDataElement;
	private List<RelatedDataElement> relatedDataElements;

	public DataElement getPrimaryDataElement() {
		return primaryDataElement;
	}

	public void setPrimaryDataElement(DataElement primaryDataElement) {
		this.primaryDataElement = primaryDataElement;
	}

	public List<RelatedDataElement> getRelatedDataElements() {
		return relatedDataElements;
	}

	public void setRelatedDataElements(
	        List<RelatedDataElement> relatedDataElements) {
		this.relatedDataElements = relatedDataElements;
	}
	
	public enum RelationOperator {
		BEFORE, AFTER, INVALID;
	}

	public static class RelatedDataElement {
		private DataElement dataElement;
		private RelationOperator relationOperator;
		private Long rhsDataElement;
		private Integer relationMinCount;
		private String relationMinUnits;
		private Integer relationMaxCount;
		private String relationMaxUnits;

		public DataElement getDataElement() {
			return dataElement;
		}

		public void setDataElement(DataElement dataElement) {
			this.dataElement = dataElement;
		}

		public RelationOperator getRelationOperator() {
			return relationOperator;
		}

		public void setRelationOperator(RelationOperator relationOperator) {
			this.relationOperator = relationOperator;
		}

		public Long getRhsDataElement() {
			return rhsDataElement;
		}

		public void setRhsDataElement(Long rhsDataElement) {
			this.rhsDataElement = rhsDataElement;
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

	public static class DataElement {
		private Long dataElement;
		private String withValue;
		private Boolean hasDuration;
		private Integer minDuration;
		private String minDurationUnits;
		private Integer maxDuration;
		private String maxDurationUnits;
		private Boolean hasPropertyConstraint;
		private String property;
		private String propertyValue;

		public Long getDataElement() {
			return dataElement;
		}

		public void setDataElement(Long dataElement) {
			this.dataElement = dataElement;
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
}
