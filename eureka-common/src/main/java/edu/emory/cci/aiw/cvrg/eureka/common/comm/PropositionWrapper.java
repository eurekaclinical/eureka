package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.List;

/**
 *
 * @author hrathod
 */
public class PropositionWrapper {

	public enum Type {

		AND, OR
	};
	private String id;
	private Long userId;
	private String abbrevDisplayName;
	private String displayName;
	private Type type;
	private List<String> targets;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAbbrevDisplayName() {
		return abbrevDisplayName;
	}

	public void setAbbrevDisplayName(String abbrevDisplayName) {
		this.abbrevDisplayName = abbrevDisplayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<String> getTargets() {
		return targets;
	}

	public void setTargets(List<String> targets) {
		this.targets = targets;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
