package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

import java.util.Date;

/**
 * Observations related to a patient's encounter.
 * 
 * @author hrathod
 * 
 */
public interface Observation {

	/**
	 * Get the unique identifier for the observation.
	 * 
	 * @return The unique identifier for the observation.
	 */
	public abstract String getId();

	/**
	 * Set the unique identifier for the observation.
	 * 
	 * @param inId The unique identifier for the observation.
	 */
	public abstract void setId(String inId);

	/**
	 * Get the unique identifier for the encounter with which the observation is
	 * associated.
	 * 
	 * @return The unique identifier for the encounter.
	 */
	public abstract Long getEncounterId();

	/**
	 * Set the unique identifier for the encounter with which the observation is
	 * associated.
	 * 
	 * @param inEncounterId The unique identifier for the encounter.
	 */
	public abstract void setEncounterId(Long inEncounterId);

	/**
	 * Get the date of the observation.
	 * 
	 * @return The date of the observation.
	 */
	public abstract Date getTimestamp();

	/**
	 * Set the date of the observation.
	 * 
	 * @param inTimestamp The date of the observation.
	 */
	public abstract void setTimestamp(Date inTimestamp);

	/**
	 * Get the unique identifier for the entity associated with the observation.
	 * 
	 * @return The unique identifier for the entity.
	 */
	public abstract String getEntityId();

	/**
	 * Set the unique identifier for the entity associated with the observation.
	 * 
	 * @param inEntityId The unique identifier for the entity.
	 */
	public abstract void setEntityId(String inEntityId);

}