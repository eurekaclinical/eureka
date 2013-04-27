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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobState;
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
	
	private JobState state;
	
	/**
	 * The date of the document upload.
	 */
	private Date uploadTime;

	/**
	 * A list of messages about the file or job processing status.
	 */
	private List<String> messages;

	public boolean isJobSubmitted() {
		return this.state != JobState.DONE;
	}

	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public JobState getState() {
		return state;
	}

	public void setState(JobState state) {
		this.state = state;
	}
	
	public String getStatusDate() {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
		return df.format(this.uploadTime);
	}
	
	public String getStatus() {
		if (this.state != JobState.DONE) {
			return step() + " out of " + totalSteps();
		} else {
			return "Complete";
		}
	}
	
	public String getFirstMessage() {
		if (this.messages == null || this.messages.isEmpty()) {
			return "No errors reported";
		} else {
			return this.messages.get(0);
		}
	}
	
	private static int totalSteps() {
		return 4;
	}

	private int step() {
		switch (this.state) {
			case CREATED:
				return 1;
			case VALIDATED:
				return 2;
			case PROCESSING:
				return 3;
			default:
				return 4;
		}
	}

}
