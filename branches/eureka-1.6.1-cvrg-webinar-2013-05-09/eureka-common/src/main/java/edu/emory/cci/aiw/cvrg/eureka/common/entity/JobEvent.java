/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonBackReference;

import com.sun.xml.bind.CycleRecoverable;
import javax.persistence.Column;
import javax.persistence.Lob;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An event associated with a job.
 *
 * @author hrathod
 *
 */
@XmlRootElement
@Entity
@Table(name = "job_events")
public class JobEvent implements CycleRecoverable {

	/**
	 * The unique identifier for the job event.
	 */
	@Id
	@SequenceGenerator(name = "JOBEVENT_SEQ_GENERATOR",
			sequenceName = "JOBEVENT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "JOBEVENT_SEQ_GENERATOR")
	private Long id;
	/**
	 * The job for which the event was generated.
	 */
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Job.class)
	@JoinColumn(name = "job_id")
	private Job job;
	/**
	 * The state of the event.
	 */
	@Column(nullable = false)
	private String state;
	
	/**
	 * The exception stack trace. The name is prefixed with a z to force
	 * hibernate to populate this field last in insert and update statements
	 * to avoid the dreaded 
	 * <code>ORA-24816: Expanded non LONG bind data supplied after actual LONG or LOB column</code>
	 * error message from Oracle. Hibernate apparently orders fields 
	 * alphabetically.
	 */
	@Lob
	@Column(name="exceptionstacktrace")
	private String zExceptionStackTrace;
	/**
	 * The time stamp for the event.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
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
	@JsonBackReference("job-event")
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
	public String getExceptionStackTrace() {
		return this.zExceptionStackTrace;
	}

	/**
	 * @param inExceptionStackTrace the exceptionStackTrace to set
	 */
	public void setExceptionStackTrace(String inExceptionStackTrace) {
		this.zExceptionStackTrace = inExceptionStackTrace;
	}

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return new Date(this.timeStamp.getTime());
	}

	/**
	 * @param inTimeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date inTimeStamp) {
		this.timeStamp = new Date(inTimeStamp.getTime());
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

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sun.xml.bind.CycleRecoverable#onCycleDetected(com.sun.xml.bind.
	 * CycleRecoverable.Context)
	 */
	@Override
	public Object onCycleDetected(Context inContext) {
		return null;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	

}
