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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;

import com.sun.xml.bind.CycleRecoverable;
import javax.persistence.Column;
import org.apache.commons.lang.builder.ToStringBuilder;

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
public class Job implements CycleRecoverable {

	/**
	 * The unique identifier for the job request.
	 */
	@Id
	@SequenceGenerator(name = "JOB_SEQ_GENERATOR", sequenceName = "JOB_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "JOB_SEQ_GENERATOR")
	private Long id;
	/**
	 * The initial timestamp when the job was started.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp = new Date();
	/**
	 * The unique identifier of the configuration to use for this job.
	 */
	@Column(nullable = false)
	private Long configurationId;
	/**
	 * The unique identifier of the user submitting the job request.
	 */
	@Column(nullable = false)
	private Long userId;
	/**
	 * The events generated for the job.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = JobEvent.class,
			fetch = FetchType.EAGER, mappedBy = "job")
	private List<JobEvent> jobEvents = new ArrayList<JobEvent>();

	private static class JobEventComparator implements Comparator<JobEvent>,
			Serializable {

		private static final long serialVersionUID = -1597150892714722679L;

		@Override
		public int compare(JobEvent a, JobEvent b) {
			if (a.getTimeStamp() == null && b.getTimeStamp() == null) {
				return 0;
			}
			if (a.getTimeStamp() == null) {
				return 1;
			}
			if (b.getTimeStamp() == null) {
				return -1;
			}
			return a.getTimeStamp().compareTo(b.getTimeStamp());
		}
	}

	private static JobEventComparator JOB_EVENT_COMPARATOR = new JobEventComparator();

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
		return new Date(this.timestamp.getTime());
	}

	/**
	 * @param inTimestamp the timestamp to set
	 */
	public void setTimestamp(Date inTimestamp) {
		this.timestamp = new Date(inTimestamp.getTime());
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
	@JsonManagedReference("job-event")
	public List<JobEvent> getJobEvents() {
		return this.jobEvents;
	}

	/**
	 * @param inJobEvents the jobEvents to set
	 */
	public void setJobEvents(List<JobEvent> inJobEvents) {
		this.jobEvents = inJobEvents;
	}

	@JsonIgnore
	public String getCurrentState() {

		JobEvent jev = getSortedEvents().last();
		return (jev == null) ? "" : jev.getState();
	}

	public void setNewState(String state, String message, String[] stackTrace) {

		final Date date = new Date();
		JobEvent jev = new JobEvent();
		jev.setJob(this);
		jev.setTimeStamp(date);
		jev.setState(state);
		jev.setMessage(message);
		jev.setExceptionStackTrace(stackTrace);
		this.setTimestamp(date);
		this.jobEvents.add(jev);
	}

	@JsonIgnore
	public Date getCreationTime() {

		JobEvent jev = getSortedEvents().first();
		return (jev == null) ? null : jev.getTimeStamp();
	}

	@JsonIgnore
	private TreeSet<JobEvent> getSortedEvents() {

		if (this.jobEvents == null || this.jobEvents.size() == 0) {

			return new TreeSet<JobEvent>();
		}
		TreeSet<JobEvent> ts = new TreeSet<JobEvent>(JOB_EVENT_COMPARATOR);
		ts.addAll(this.jobEvents);
		return ts;
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
