package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andrew Post
 */
public class Statistics {
	private Integer numberOfKeys;
	private Map<String, Integer> counts;
	private Map<String, String> childrenToParents;

	public Integer getNumberOfKeys() {
		return numberOfKeys;
	}

	public void setNumberOfKeys(Integer numberOfKeys) {
		this.numberOfKeys = numberOfKeys;
	}
	
	public Map<String, Integer> getCounts() {
		return new HashMap<>(counts);
	}

	public void setCounts(Map<String, Integer> counts) {
		this.counts = new HashMap<>(counts);
	}

	public Map<String, String> getChildrenToParents() {
		return childrenToParents;
	}

	public void setChildrenToParents(Map<String, String> childrenToParents) {
		this.childrenToParents = childrenToParents;
	}
	
}
