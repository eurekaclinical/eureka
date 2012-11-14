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
        private Long sequentialDataElement;
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

        public Long getSequentialDataElement() {
            return sequentialDataElement;
        }

        public void setSequentialDataElement(Long rhsDataElement) {
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
