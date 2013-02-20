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

import java.util.Date;
import java.util.List;

/**
 * A communication bean to transfer information about a job status.
 *
 * @author sagrava
 *
 */
public class JobStatus {

	/**
	 * The current step in the process.
	 */
	private int currentStep;

	/**
	 * The total number of steps in the process.
	 */
	private int totalSteps;

	/**
	 * The date of the document upload.
	 */
	private Date uploadTime;

	/**
	 * A list of messages about the file or job processing status.
	 */
	private List<String> messages;

	public int getCurrentStep() {
		return currentStep;
	}
	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}
	public int getTotalSteps() {
		return totalSteps;
	}
	public void setTotalSteps(int totalSteps) {
		this.totalSteps = totalSteps;
	}
	public Date getUploadTime() {
		return new Date(uploadTime.getTime());
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = new Date(uploadTime.getTime());
	}
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

}
