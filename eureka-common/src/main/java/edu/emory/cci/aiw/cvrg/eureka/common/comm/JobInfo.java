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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileError;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileWarning;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent;

/**
 * A communication bean to transfer information about a user's job.
 * 
 * @author hrathod
 * 
 */
@XmlRootElement
public class JobInfo {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JobInfo.class);
	/**
	 * The total number of steps in the process.
	 */
	private int totalSteps = 7;
	/**
	 * The file upload for the job being processed.
	 */
	private FileUpload fileUpload;
	/**
	 * The actual running job, if the file upload/validation/processing is
	 * complete.
	 */
	private Job job;

	/**
	 * Get the processing step for the file upload or job.
	 * 
	 * @return The process step that the job is currently on.
	 */
	@JsonIgnore
	public int getCurrentStep() {
		int step;

		if (this.job == null && this.fileUpload == null) {
			// we have neither an upload nor a job
			LOGGER.debug("job and file upload are null");
			step = 0;
		} else if (this.job != null && this.fileUpload == null) {
			// we have a job, but no upload
			LOGGER.debug("job is not null, file upload is null");
			step = this.getJobStep();
		} else if (this.fileUpload != null && this.job == null) {
			// we have an upload, but no job
			LOGGER.debug("job is null, file upload is not null");
			step = this.getUploadStep();
		} else if (this.isUploadActive() || this.isJobActive()) {
			// we have either an active upload or an active job
			LOGGER.debug("job or file upload is active");
			if (this.isUploadActive()) {
				LOGGER.debug("upload is active");
				step = this.getUploadStep();
			} else {
				LOGGER.debug("job is active");
				step = this.getJobStep();
			}
		} else {
			// we have both an inactive job, and an inactive upload, so we just
			// return the later of the two
			LOGGER.debug("job and file upload are inactive");
			if (this.fileUpload.getTimestamp().after(this.job.getTimestamp())) {
				LOGGER.debug("file upload is later than job");
				step = this.getUploadStep();
			} else {
				LOGGER.debug("job is later than file upload");
				step = this.getJobStep();
			}
		}
		return step;
	}

	/**
	 * Return the current status of the job.
	 * 
	 * @return The current step.
	 */
	@JsonIgnore
	private int getJobStep() {
		int step = 0;
		String currentState = this.job.getCurrentState();
		if (currentState.equals("DONE") || currentState.equals("FAILED")
				|| currentState.equals("EXCEPTION")
				|| currentState.equals("INTERRUPTED")) {
			step = 7;
		} else if (currentState.equals("PROCESSING")) {
			step = 6;
		} else if (currentState.equals("CREATED")) {
			step = 5;
		}
		return step;
	}

	/**
	 * Return the current status of the upload.
	 * 
	 * @return The current step.
	 */
	@JsonIgnore
	private int getUploadStep() {
		int step = 0;
		if (this.fileUpload.containsErrors()) {
			step = this.totalSteps;
		} else if (this.fileUpload.isCompleted()) {
			step = 4;
		} else if (this.fileUpload.isProcessed()) {
			step = 3;
		} else if (this.fileUpload.isValidated()) {
			step = 2;
		} else {
			step = 1;
		}
		return step;
	}

	/**
	 * Find out whether the job contained in this job information object is
	 * currently active or not.
	 * 
	 * @return True, if the job is active and false otherwise.
	 */
	@JsonIgnore
	private boolean isJobActive() {
		boolean result;
		if (this.job == null) {
			result = false;
		} else {
			String state = this.job.getCurrentState();
			result = (state.equals("CREATED") || state.equals("PROCESSING"));
		}
		return result;
	}

	/**
	 * Find out whether the file upload contained in this job information object
	 * is currently active or not.
	 * 
	 * @return True if the file upload is active, false otherwise.
	 */
	private boolean isUploadActive() {
		boolean result;
		if (this.fileUpload == null) {
			result = false;
		} else {
			result = !this.fileUpload.isCompleted();
		}
		return result;
	}

	/**
	 * Get the timestamp of the latest update for a file job.
	 * 
	 * @return The latest timestamp for the job.
	 */
	@JsonIgnore
	public Date getTimestamp() {
		Date result;
		if (this.job == null && this.fileUpload == null) {
			// we have neither an upload nor a job
			LOGGER.debug("job and file upload are null");
			result = new Date();
		} else if (this.job != null && this.fileUpload == null) {
			// we have a job, but no upload
			LOGGER.debug("job is not null, file upload is null");
			result = this.job.getTimestamp();
		} else if (this.fileUpload != null && this.job == null) {
			// we have an upload, but no job
			LOGGER.debug("job is null, file upload is not null");
			result = this.fileUpload.getTimestamp();
		} else if (this.isUploadActive() || this.isJobActive()) {
			// we have either an active upload or an active job
			LOGGER.debug("job or file upload is active");
			if (this.isUploadActive()) {
				LOGGER.debug("upload is active");
				result = this.fileUpload.getTimestamp();
			} else {
				LOGGER.debug("job is active");
				result = this.job.getTimestamp();
			}
		} else {
			// we have both an inactive job, and an inactive upload, so we just
			// return the later of the two
			LOGGER.debug("job and file upload are inactive");
			if (this.fileUpload.getTimestamp().after(this.job.getTimestamp())) {
				LOGGER.debug("file upload is later than job");
				result = this.fileUpload.getTimestamp();
			} else {
				LOGGER.debug("job is later than file upload");
				result = this.job.getTimestamp();
			}
		}
		return result;
	}

	/**
	 * @return the totalSteps
	 */
	public int getTotalSteps() {
		return this.totalSteps;
	}

	/**
	 * @param inTotalSteps the totalSteps to set
	 */
	public void setTotalSteps(int inTotalSteps) {
		this.totalSteps = inTotalSteps;
	}

	/**
	 * Get a list of messages for the job, including those generated by the file
	 * validation, as well as the running of the job itself.
	 * 
	 * @return The list of messages for the job.
	 */
	@JsonIgnore
	public List<String> getMessages() {
		List<String> messages = new ArrayList<String>();
		if (this.fileUpload != null) {
			messages.addAll(this.getFileUploadMessages());
		}
		if (this.job != null) {
			messages.addAll(this.getJobMessages());
		}
		return messages;
	}

	/**
	 * Return a list of messages generated by the file upload validation.
	 * 
	 * @return The list of messages.
	 */
	private List<String> getFileUploadMessages() {
		List<String> messsages = new ArrayList<String>();
		if (this.fileUpload != null) {
			for (FileWarning fileWarning : this.fileUpload.getWarnings()) {
				messsages.add(fileWarning.toString());
			}
			for (FileError error : this.fileUpload.getErrors()) {
				messsages.add(error.toString());
			}
		}
		return messsages;
	}

	/**
	 * Return a list of messages generated by the job.
	 * 
	 * @return The list of messages.
	 */
	private List<String> getJobMessages() {
		final List<String> messages = new ArrayList<String>();
		if (this.job != null) {
			for (JobEvent event : this.job.getJobEvents()) {
				final String message = event.getMessage();
				if (message != null) {
					messages.add(message);
				}
			}
		}
		return messages;
	}

	/**
	 * @return the fileUpload
	 */
	public FileUpload getFileUpload() {
		return this.fileUpload;
	}

	/**
	 * @param inFileUpload the fileUpload to set
	 */
	public void setFileUpload(FileUpload inFileUpload) {
		this.fileUpload = inFileUpload;
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
}
