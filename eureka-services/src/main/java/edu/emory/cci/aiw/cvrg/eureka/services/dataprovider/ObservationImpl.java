package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

import java.util.Date;

/**
 * Abstract class used as the super-type for various types of observations
 * related to an encounter.
 * 
 * @author hrathod
 * 
 */
abstract class ObservationImpl implements Observation {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#setId
	 * (java.lang.String)
	 */
	@Override
	public void setId(String inId) {
		this.id = inId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#
	 * getEncounterId()
	 */
	@Override
	public Long getEncounterId() {
		return this.encounterId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#
	 * setEncounterId(java.lang.Long)
	 */
	@Override
	public void setEncounterId(Long inEncounterId) {
		this.encounterId = inEncounterId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#getTimestamp
	 * ()
	 */
	@Override
	public Date getTimestamp() {
		return this.timestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#setTimestamp
	 * (java.util.Date)
	 */
	@Override
	public void setTimestamp(Date inTimestamp) {
		this.timestamp = inTimestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#getEntityId
	 * ()
	 */
	@Override
	public String getEntityId() {
		return this.entityId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#setEntityId
	 * (java.lang.String)
	 */
	@Override
	public void setEntityId(String inEntityId) {
		this.entityId = inEntityId;
	}
}
