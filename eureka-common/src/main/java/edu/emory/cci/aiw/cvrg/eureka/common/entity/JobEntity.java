/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Holds information about a job that is sent from the services layer to the
 * back-end layer.
 *
 * @author hrathod
 *
 */
@Entity
@Table(name = "jobs")
public class JobEntity {
	
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
	private Date created = new Date();
	/**
	 * The unique identifier of the configuration to use for this job.
	 */
	@Column(nullable = false)
	private String sourceConfigId;
	
	/**
	 * The unique identifier of the configuration to use for this job.
	 */
	@Column(nullable = false)
	private String destinationId;
	/**
	 * The unique identifier of the user submitting the job request.
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private EtlUser etlUser;
	/**
	 * The events generated for the job.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = JobEvent.class,
			mappedBy = "job")
	private List<JobEvent> jobEvents = new ArrayList<JobEvent>();

	private static class JobEventComparator implements Comparator<JobEvent>,
			Serializable {

		private static final long serialVersionUID = -1597150892714722679L;
		
		private static final Map<JobEventType, Integer> order = new EnumMap<JobEventType, Integer>(JobEventType.class);
		static {
			order.put(JobEventType.VALIDATING, 0);
			order.put(JobEventType.VALIDATED, 1);
			order.put(JobEventType.PROCESSING, 2);
			order.put(JobEventType.WARNING, 3);
			order.put(JobEventType.ERROR, 4);
			order.put(JobEventType.COMPLETED, 5);
			order.put(JobEventType.FAILED, 6);
		}
		
		@Override
		public int compare(JobEvent a, JobEvent b) {
			return order.get(a.getState()).compareTo(order.get(b.getState()));
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
	public Date getCreated() {
		return this.created;
	}

	/**
	 * @param inTimestamp the timestamp to set
	 */
	public void setCreated(Date inTimestamp) {
		this.created = inTimestamp;
	}

	/**
	 * Get the unique identifier of the configuration to be used for the job
	 * request.
	 *
	 * @return The unique identifier of the configuration.
	 */
	public String getSourceConfigId() {
		return this.sourceConfigId;
	}

	/**
	 * Set the unique identifier of the configuration to be used for the job
	 * request.
	 *
	 * @param inSourceConfigId The unique identifier of the configuration.
	 */
	public void setSourceConfigId(String inSourceConfigId) {
		this.sourceConfigId = inSourceConfigId;
	}

	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String inDestinationId) {
		this.destinationId = inDestinationId;
	}
	
	

	/**
	 * Get the unique identifier for the user who submitted the request.
	 *
	 * @return The unique identifier for the user.
	 */
	public EtlUser getEtlUser() {
		return this.etlUser;
	}

	/**
	 * Set the unique identifier for the user who submitted the request.
	 *
	 * @param inEtlUser The unique identifier for the user.
	 */
	public void setEtlUser(EtlUser inEtlUser) {
		this.etlUser = inEtlUser;
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
	public JobEventType getCurrentState() {

		JobEvent jev = getSortedEvents().last();
		return (jev == null) ? null : jev.getState();
	}

	public void newEvent(JobEventType state, String message, String[] stackTrace) {
		if (state == null) {
			throw new IllegalArgumentException("state cannot be null");
		}
		final Date date = new Date();
		JobEvent jev = new JobEvent();
		jev.setJob(this);
		jev.setTimeStamp(date);
		jev.setState(state);
		jev.setMessage(message);
		if (stackTrace != null) {
			jev.setExceptionStackTrace(StringUtils.join(stackTrace,'\n'));
		}
		this.jobEvents.add(jev);
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
