package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Holds information about a job that is sent from the services layer to the
 * back-end layer.
 * 
 * @author hrathod
 * 
 */
@XmlRootElement
@Entity
@Table(name = "jobs")
public class Job {
	/**
	 * The unique identifier for the job request.
	 */
	@Id
	@GeneratedValue
	private Long id;
	/**
	 * The initial timestamp when the job was started.
	 */
	private Date timestamp;
	/**
	 * The unique identifier of the configuration to use for this job.
	 */
	private Long configurationId;
	/**
	 * The unique identifier of the user submitting the job request.
	 */
	private Long userId;
	/**
	 * The events generated for the job.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = JobEvent.class)
	private List<JobEvent> jobEvents;

	/**
	 * Get the unique identifier for the job request.
	 * 
	 * @return The unique identifier for the job request.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the unique identifier for the job request.
	 * 
	 * @param inId The unique identifier for the job request.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param inTimestamp the timestamp to set
	 */
	public void setTimestamp(Date inTimestamp) {
		this.timestamp = inTimestamp;
	}

	/**
	 * Get the unique identifier of the configuration to be used for the job
	 * request.
	 * 
	 * @return The unique identifier of the configuration.
	 */
	public Long getConfigurationId() {
		return this.configurationId;
	}

	/**
	 * Set the unique identifier of the configuration to be used for the job
	 * request.
	 * 
	 * @param inConfigurationId The unique identifier of the configuration.
	 */
	public void setConfigurationId(Long inConfigurationId) {
		this.configurationId = inConfigurationId;
	}

	/**
	 * Get the unique identifier for the user who submitted the request.
	 * 
	 * @return The unique identifier for the user.
	 */
	public Long getUserId() {
		return this.userId;
	}

	/**
	 * Set the unique identifier for the user who submitted the request.
	 * 
	 * @param inUserId The unique identifier for the user.
	 */
	public void setUserId(Long inUserId) {
		this.userId = inUserId;
	}

	/**
	 * @return the jobEvents
	 */
	public List<JobEvent> getJobEvents() {
		return this.jobEvents;
	}

	/**
	 * @param inJobEvents the jobEvents to set
	 */
	public void setJobEvents(List<JobEvent> inJobEvents) {
		this.jobEvents = inJobEvents;
	}
}
