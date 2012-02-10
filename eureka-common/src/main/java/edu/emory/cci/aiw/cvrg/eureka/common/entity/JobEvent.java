package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An event associated with a job.
 * 
 * @author hrathod
 * 
 */
@XmlRootElement
@Entity
@Table(name = "job_events")
public class JobEvent {

	/**
	 * The unique identifier for the job event.
	 */
	@Id
	@GeneratedValue
	private Long id;
	/**
	 * The job for which the event was generated.
	 */
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Job.class)
	private Job job;
	/**
	 * The state of the event.
	 */
	private String state;
	/**
	 * The exception stack trace
	 */
	private String[] exceptionStackTrace;
	/**
	 * The time stamp for the event.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;
	/**
	 * The message generated for the event.
	 */
	private String message;

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param inId the id to set
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * @return the job
	 */
	public Job getJob() {
		return this.job;
	}

	/**
	 * @param inJob the job to set
	 */
	public void setJob(Job inJob) {
		this.job = inJob;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return this.state;
	}

	/**
	 * @param inState the state to set
	 */
	public void setState(String inState) {
		this.state = inState;
	}

	/**
	 * @return the exceptionStackTrace
	 */
	public String[] getExceptionStackTrace() {
		return this.exceptionStackTrace;
	}

	/**
	 * @param inExceptionStackTrace the exceptionStackTrace to set
	 */
	public void setExceptionStackTrace(String[] inExceptionStackTrace) {
		this.exceptionStackTrace = inExceptionStackTrace;
	}

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return this.timeStamp;
	}

	/**
	 * @param inTimeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date inTimeStamp) {
		this.timeStamp = inTimeStamp;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param inMessage the message to set
	 */
	public void setMessage(String inMessage) {
		this.message = inMessage;
	}

}
