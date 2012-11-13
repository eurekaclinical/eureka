package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.List;

public final class CategoricalElement extends DataElement {
	private List<DataElement> children;

	public List<DataElement> getChildren() {
		return children;
	}

	public void setChildren(List<DataElement> children) {
		this.children = children;
	}
}
