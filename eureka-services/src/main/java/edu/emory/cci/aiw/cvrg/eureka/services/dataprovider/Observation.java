package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

import java.util.Date;

/**
 * Abstract class used as the super-type for various types of observations
 * related to an encounter.
 * 
 * @author hrathod
 * 
 */
public abstract class Observation {
	/**
	 * The unique identifier for the observation.
	 */
	private String id;
	/**
	 * The unique identifier for the encounter to which this observation is
	 * associated.
	 */
	private Long encounterId;
	/**
	 * The date of the observation.
	 */
	private Date timestamp;
	/**
	 * The unique identifier for the entity associated with this observation.
	 */
	private String entityId;

	/**
	 * Get the unique identifier for the observation.
	 * 
	 * @return The unique identifier for the observation.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Set the unique identifier for the observation.
	 * 
	 * @param inId The unique identifier for the observation.
	 */
	public void setId(String inId) {
		this.id = inId;
	}

	/**
	 * Get the unique identifier for the encounter with which the observation is
	 * associated.
	 * 
	 * @return The unique identifier for the encounter.
	 */
	public Long getEncounterId() {
		return this.encounterId;
	}

	/**
	 * Set the unique identifier for the encounter with which the observation is
	 * associated.
	 * 
	 * @param inEncounterId The unique identifier for the encounter.
	 */
	public void setEncounterId(Long inEncounterId) {
		this.encounterId = inEncounterId;
	}

	/**
	 * Get the date of the observation.
	 * 
	 * @return The date of the observation.
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Set the date of the observation.
	 * 
	 * @param inTimestamp The date of the observation.
	 */
	public void setTimestamp(Date inTimestamp) {
		this.timestamp = inTimestamp;
	}

	/**
	 * Get the unique identifier for the entity associated with the observation.
	 * 
	 * @return The unique identifier for the entity.
	 */
	public String getEntityId() {
		return this.entityId;
	}

	/**
	 * Set the unique identifier for the entity associated with the observation.
	 * 
	 * @param inEntityId The unique identifier for the entity.
	 */
	public void setEntityId(String inEntityId) {
		this.entityId = inEntityId;
	}
}
