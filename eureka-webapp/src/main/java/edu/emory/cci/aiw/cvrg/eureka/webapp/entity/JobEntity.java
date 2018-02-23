/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.webapp.entity;

import org.eurekaclinical.eureka.client.comm.Job;
import org.eurekaclinical.eureka.client.comm.Link;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.eurekaclinical.eureka.client.comm.JobEvent;
import org.eurekaclinical.eureka.client.comm.JobStatus;

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
	@ManyToOne
	@JoinColumn(name = "destination_id", referencedColumnName = "id", nullable = false)
	private DestinationEntity destination;
	/**
	 * The unique identifier of the user submitting the job request.
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private AuthorizedUserEntity user;
	/**
	 * The events generated for the job.
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "job")
	private List<JobEventEntity> jobEvents;
	
	private String name;

	/**
	 * The timestamp when the job ended.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date finished;

	public Date getFinished() {
		return finished;
	}

	public void setFinished(Date finished) {
		this.finished = finished;
	}

	private static JobEventComparator JOB_EVENT_COMPARATOR = new JobEventComparator();
	private static JobEventEntityComparator JOB_EVENT_ENTITY_COMPARATOR = new JobEventEntityComparator();

	public JobEntity() {
		this.jobEvents = new ArrayList<>();
	}

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

	public DestinationEntity getDestination() {
		return destination;
	}

	public void setDestination(DestinationEntity inDestination) {
		this.destination = inDestination;
	}

	/**
	 * Get the unique identifier for the user who submitted the request.
	 *
	 * @return The unique identifier for the user.
	 */
	public AuthorizedUserEntity getUser() {
		return this.user;
	}

	/**
	 * Set the unique identifier for the user who submitted the request.
	 *
	 * @param inEtlUser The unique identifier for the user.
	 */
	public void setUser(AuthorizedUserEntity inEtlUser) {
		this.user = inEtlUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the jobEvents
	 */
	@JsonManagedReference("job-event")
	public List<JobEventEntity> getJobEvents() {
		return new ArrayList<>(this.jobEvents);
	}

	/**
	 * @param inJobEvents the jobEvents to set
	 */
	public void setJobEvents(List<JobEventEntity> inJobEvents) {
		if (inJobEvents == null) {
			this.jobEvents = new ArrayList<>();
		} else {
			this.jobEvents = new ArrayList<>(inJobEvents);
			for (JobEventEntity jobEvent : this.jobEvents) {
				jobEvent.setJob(this);
			}
		}
	}

	public void addJobEvent(JobEventEntity jobEvent) {
		if (!this.jobEvents.contains(jobEvent)) {
			this.jobEvents.add(jobEvent);
			jobEvent.setJob(this);
		}
	}

	public void removeJobEvent(JobEventEntity jobEvent) {
		if (this.jobEvents.remove(jobEvent)) {
			jobEvent.setJob(null);
		}
	}

	public JobStatus getCurrentStatus() {
		JobStatus result;
		List<JobEventEntity> jobEventsInReverseOrder = getJobEventsInReverseOrder();
		if (jobEventsInReverseOrder.isEmpty()) {
			result = JobStatus.STARTING;
		} else {
			JobEventEntity jev = jobEventsInReverseOrder.get(0);
			if (jev != null) {
				result = jev.getStatus();
			} else {
				result = JobStatus.STARTING;
			}
		}
		return result;
	}

	/**
	 * Gets job events sorted in reverse order of occurrence. Uses the
	 * {@link JobEventComparator} to perform sorting.
	 *
	 * @return a {@link List} of {@link JobEventEntity}s in reverse order of
	 * occurrence.
	 */
	private List<JobEvent> jobEventsSorted() {
		List<JobEvent> jobEvents = new ArrayList<>();
		for (JobEventEntity jee : this.jobEvents) {
			jobEvents.add(jee.toJobEvent());
		}
		Collections.sort(jobEvents, JOB_EVENT_COMPARATOR);
		return jobEvents;
	}
	
	public List<JobEventEntity> getJobEventsInOrder() {
		List<JobEventEntity> jobEvents = new ArrayList<>(this.jobEvents);
		Collections.sort(jobEvents, JOB_EVENT_ENTITY_COMPARATOR);
		return jobEvents;
	}

	/**
	 * Gets job events sorted in reverse order of occurrence. Uses the
	 * {@link JobEventComparator} to perform sorting.
	 *
	 * @return a {@link List} of {@link JobEventEntity}s in reverse order of
	 * occurrence.
	 */
	public List<JobEventEntity> getJobEventsInReverseOrder() {
		List<JobEventEntity> jobEvents = new ArrayList<>(this.jobEvents);
		Collections.sort(jobEvents, Collections.reverseOrder(JOB_EVENT_ENTITY_COMPARATOR));
		return jobEvents;
	}

	public Job toJob() {
		Job job = new Job();
		job.setDestinationId(this.destination.getName());
		job.setSourceConfigId(this.sourceConfigId);
		job.setStartTimestamp(this.created);
		job.setId(this.id);
		if (this.user != null) {
			job.setUsername(this.user.getUsername());
		}
		job.setStatus(getCurrentStatus());
		job.setJobEvents(jobEventsSorted());
		job.setFinishTimestamp(this.finished);
		List<LinkEntity> linkEntities = this.destination.getLinks();
		List<Link> links = new ArrayList<>(linkEntities != null ? linkEntities.size() : 0);
		if (linkEntities != null) {
			for (LinkEntity le : linkEntities) {
				links.add(le.toLink());
			}
		}
		job.setLinks(links);
		job.setGetStatisticsSupported(this.destination.isGetStatisticsSupported());
		return job;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
