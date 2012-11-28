package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.List;

public class ResultThresholds extends DataElement {

	private static enum ThresholdsOperator {
		ALL, ANY
	}

	private String name;
	private ThresholdsOperator thresholdsOperator;
	private List<ValueThreshold> valueThresholds;

	public ResultThresholds() {
		super(Type.VALUE_THRESHOLD);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ThresholdsOperator getThresholdsOperator() {
		return thresholdsOperator;
	}

	public void setThresholdsOperator(ThresholdsOperator thresholdsOperator) {
		this.thresholdsOperator = thresholdsOperator;
	}

	public List<ValueThreshold> getValueThresholds() {
		return valueThresholds;
	}

	public void setValueThresholds(List<ValueThreshold> valueThresholds) {
		this.valueThresholds = valueThresholds;
	}

	@Override
	public void accept(DataElementVisitor visitor) {
		visitor.visit(this);
	}

}
