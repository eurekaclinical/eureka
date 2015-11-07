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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobStatus;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A communication bean to transfer information about a job status.
 *
 * @author sagrava
 *
 */
public class JobListRow {
	
	private edu.emory.cci.aiw.cvrg.eureka.common.entity.JobStatus state;
	/**
	 * The date of job start.
	 */
	private Date startedDate;
	private Date finishedDate;
	/**
	 * A list of messages about the file or job processing status.
	 */
	private List<String> messages;
	
	private List<Link> links;
	
	private String sourceConfigId;
	
	private String destinationId;
	
	public JobListRow() {
		this.links = new ArrayList<>();
	}
	
	public boolean isJobSubmitted() {
		return this.state != edu.emory.cci.aiw.cvrg.eureka.common.entity.JobStatus.COMPLETED && this.state != edu.emory.cci.aiw.cvrg.eureka.common.entity.JobStatus.FAILED;
	}

	public Date getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(Date startedDate) {
		this.startedDate = startedDate;
	}

	public Date getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public JobStatus getState() {
		return state;
	}

	public void setStatus(JobStatus state) {
		this.state = state;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		if (links == null) {
			this.links = new ArrayList<>();
		} else {
			this.links = links;
		}
	}
	
	public String getStartedDateFormatted() {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
		return df.format(this.startedDate);
	}

	public String getFinishedDateFormatted() {
		if (this.finishedDate != null) {
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
			return df.format(this.finishedDate);
		} else {
			return "";
		}
	}

	public String getStatus() {
		switch (this.state) {
			case COMPLETED:
				return "Completed";
			case FAILED:
				return "Failed";
			case VALIDATING:
			case VALIDATED:
			case STARTING:
			case STARTED:
			case WARNING:
			case ERROR:
				return "In progress";
			default:
				throw new AssertionError("Invalid state " + this.state);
		}
	}

	public String getMostRecentMessage() {
		if (this.messages == null || this.messages.isEmpty()) {
			return "No errors reported";
		} else {
			return this.messages.get(this.messages.size() - 1);
		}
	}

	public String getSourceConfigId() {
		return sourceConfigId;
	}

	public void setSourceConfigId(String sourceConfigId) {
		this.sourceConfigId = sourceConfigId;
	}

	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}
	
}
