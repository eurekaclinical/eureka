package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.Date;

/**
 * A user-created data element from the UI. Contains fields common to all
 * user-created data elements.
 */
public abstract class DataElement {
    private Long id;
    private String key;
    private Long userId;
    private String abbrevDisplayName;
    private String displayName;
    private boolean inSystem;
    private Date created;
    private Date lastModified;
    private boolean summarized;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public boolean isInSystem() {
        return inSystem;
    }

    public void setInSystem(boolean inSystem) {
        this.inSystem = inSystem;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isSummarized() {
        return summarized;
    }

    public void setSummarized(boolean summarized) {
        this.summarized = summarized;
    }

}
