package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.Date;
import java.util.List;

/**
 * @author hrathod
 */
public class PropositionWrapper {

	public enum Type {AND, OR}

	private String id;
	private String key;
	private Long userId;
	private String abbrevDisplayName;
	private String displayName;
	private Type type;
	private boolean inSystem;
	private List<Long> userTargets;
	private List<String> systemTargets;
	private Date created;
	private Date lastModified;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String inKey) {
		key = inKey;
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

	public List<Long> getUserTargets() {
		return userTargets;
	}

	public void setUserTargets(List<Long> userTargets) {
		this.userTargets = userTargets;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isInSystem() {
		return inSystem;
	}

	public void setInSystem(boolean inSystem) {
		this.inSystem = inSystem;
	}

	public List<String> getSystemTargets() {
		return systemTargets;
	}

	public void setSystemTargets(List<String> systemTargets) {
		this.systemTargets = systemTargets;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date inCreated) {
		created = inCreated;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date inLastModified) {
		lastModified = inLastModified;
	}
}
