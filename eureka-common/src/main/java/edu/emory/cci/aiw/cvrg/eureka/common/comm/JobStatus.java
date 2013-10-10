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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEventType;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * A communication bean to transfer information about a job status.
 *
 * @author sagrava
 *
 */
public class JobStatus {

	private JobEventType state;
	/**
	 * The date of job start.
	 */
	private Date startedDate;
	private Date finishedDate;
	/**
	 * A list of messages about the file or job processing status.
	 */
	private List<String> messages;

	public boolean isJobSubmitted() {
		return this.state != JobEventType.COMPLETED && this.state != JobEventType.FAILED;
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

	public JobEventType getState() {
		return state;
	}

	public void setState(JobEventType state) {
		this.state = state;
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
}
