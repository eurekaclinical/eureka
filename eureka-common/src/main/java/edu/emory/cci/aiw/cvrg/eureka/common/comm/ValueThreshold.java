package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.List;

public final class ValueThreshold {

	private String dataElementKey;
	private Long lowerComp;
	private Long upperComp;
	private String lowerValue;
	private String upperValue;
	private String lowerUnits;
	private String upperUnits;
	private Boolean isBeforeOrAfter;
	private Long relationOperator;
	private List<String> relatedDataElementKeys;
	private Integer atLeastCount;
	private Long atLeastTimeUnit;
	private Integer atMostCount;
	private Long atMostTimeUnit;

	public String getDataElementKey() {
		return dataElementKey;
	}

	public void setDataElementKey(String dataElementKey) {
		this.dataElementKey = dataElementKey;
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

	public List<String> getRelatedDataElementKeys() {
		return relatedDataElementKeys;
	}

	public void setRelatedDataElementKeys(List<String> relatedDataElementKeys) {
		this.relatedDataElementKeys = relatedDataElementKeys;
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
