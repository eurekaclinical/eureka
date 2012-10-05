package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hrathod
 */
public class PropositionWrapper implements Serializable {

	public enum Type {AND, OR}

	private Long id;
	private String key;
	private Long userId;
	private String abbrevDisplayName;
	private String displayName;
	private Type type;
	private boolean inSystem;
	private List<PropositionWrapper> children;
//	private List<PropositionWrapper> userTargets;
//	private List<String> systemTargets;
	private Date created;
	private Date lastModified;
	private boolean summarized;
	private boolean parent;

    public PropositionWrapper() {
        children = new ArrayList<PropositionWrapper>();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

//	public List<Long> getUserTargets() {
//		return userTargets;
//	}
//
//	public void setUserTargets(List<Long> userTargets) {
//		this.userTargets = userTargets;
//	}

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

//	public List<String> getSystemTargets() {
//		return systemTargets;
//	}
//
//	public void setSystemTargets(List<String> systemTargets) {
//		this.systemTargets = systemTargets;
//	}

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

	public boolean isSummarized() {
		return summarized;
	}

	public void setSummarized(boolean inSummarized) {
		summarized = inSummarized;
	}

	public List<PropositionWrapper> getChildren() {
		return children;
	}

	public void setChildren(List<PropositionWrapper> inChildren) {
		children = inChildren;
	}

	public boolean isParent() {
		return parent;
	}

	public void setParent(boolean inParent) {
		parent = inParent;
	}

	@Override
	public String toString() {
		return "PropositionWrapper{" +
			"id=" + id +
			", key='" + key + '\'' +
			", userId=" + userId +
			", abbrevDisplayName='" + abbrevDisplayName + '\'' +
			", displayName='" + displayName + '\'' +
			", type=" + type +
			", inSystem=" + inSystem +
			", children=" + children.size() +
			", created=" + created +
			", lastModified=" + lastModified +
			", summarized=" + summarized +
			", parent=" + parent +
			'}';
	}
}
