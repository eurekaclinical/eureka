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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.xml.bind.CycleRecoverable;
import javax.persistence.Column;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Store information about a user's file upload.
 *
 * @author hrathod
 *
 */
@XmlRootElement
@Entity
@Table(name = "file_uploads")
public class FileUpload implements CycleRecoverable {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileUpload.class);
	/**
	 * The unique identifier for the file upload.
	 */
	@Id
	@SequenceGenerator(name = "FILE_SEQ_GENERATOR", sequenceName = "FILE_SEQ",
	allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator = "FILE_SEQ_GENERATOR")
	private Long id;
	/**
	 * The on-disk location of the uploaded file.
	 */
	@Column(nullable = false)
	private String location;
	/**
	 * The timestamp of the initial upload.
	 */
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	/**
	 * Has the file upload been validated?.
	 */
	@Column(nullable = false)
	private boolean validated;
	/**
	 * Has the file been processed?.
	 */
	@Column(nullable = false)
	private boolean processed;
	/**
	 * Are we done with the file?.
	 */
	@Column(nullable = false)
	private boolean completed;
	/**
	 * The user to which this upload belongs.
	 */
	@Column(nullable = false)
	private Long userId;
	/**
	 * Contains a list of errors found in the file.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = FileError.class,
	mappedBy = "fileUpload")
	private List<FileError> errors = new ArrayList<FileError>();
	/**
	 * Contains a list of warnings found in the file.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = FileWarning.class,
	mappedBy = "fileUpload")
	private List<FileWarning> warnings = new ArrayList<FileWarning>();

	/**
	 * Get the unique identifier for the file upload.
	 *
	 * @return The unique identifier for the file upload.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the unique identifier for the file upload.
	 *
	 * @param inId The unique identifier for the file upload.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Get the on-disk location of the file upload.
	 *
	 * @return The on-disk location of the file upload.
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * Set the on-desk location of the file upload.
	 *
	 * @param inLocation The on-disk location of the file upload.
	 */
	public void setLocation(String inLocation) {
		this.location = inLocation;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param inTimestamp the timestamp to set
	 */
	public void setTimestamp(Date inTimestamp) {
		this.timestamp = inTimestamp;
	}

	/**
	 * @return the validated
	 */
	public boolean isValidated() {
		return this.validated;
	}

	/**
	 * @param inValidated the validated to set
	 */
	public void setValidated(boolean inValidated) {
		this.validated = inValidated;
	}

	/**
	 * @return the processed
	 */
	public boolean isProcessed() {
		return this.processed;
	}

	/**
	 * @param inProcessed the processed to set
	 */
	public void setProcessed(boolean inProcessed) {
		this.processed = inProcessed;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return this.completed;
	}

	/**
	 * @param inCompleted the completed to set
	 */
	public void setCompleted(boolean inCompleted) {
		this.completed = inCompleted;
	}

	/**
	 * Get the user to which the file upload belongs.
	 *
	 * @return The user to which the file upload belongs.
	 */
	public Long getUserId() {
		return this.userId;
	}

	/**
	 * Set the user to which the file upload belongs.
	 *
	 * @param inUserId The user to which the file upload belongs.
	 */
	public void setUserId(Long inUserId) {
		this.userId = inUserId;
	}

	/**
	 * Get the list of errors associated with the file upload.
	 *
	 * @return The list of errors associated with the file upload.
	 */
	@JsonManagedReference("fileupload-messages")
	public List<FileError> getErrors() {
		return this.errors;
	}

	/**
	 * Set the list of errors associated with the file upload.
	 *
	 * @param inErrors The list of errors associated with the file upload.
	 */
	public void setErrors(List<FileError> inErrors) {
		this.errors = inErrors;
		for (FileError error : inErrors) {
			FileUpload fileUpload = error.getFileUpload();
			if (fileUpload == null || !fileUpload.getId().equals(this.getId())) {
				error.setFileUpload(this);
			}
		}
	}

	/**
	 * Add an error to the list of errors for the file upload.
	 *
	 * @param error The error to add.
	 */
	public void addError(FileError error) {
		FileUpload fileUpload = error.getFileUpload();
		if (fileUpload == null || !fileUpload.getId().equals(this.getId())) {
			error.setFileUpload(this);
		}
		LOGGER.debug("Adding error: {}", error.getText());
		this.errors.add(error);
	}

	/**
	 * Get the list of warnings associated with the file upload.
	 *
	 * @return The list of warnings associated with the file upload.
	 */
	@JsonManagedReference("fileupload-messages")
	public List<FileWarning> getWarnings() {
		return this.warnings;
	}

	/**
	 * Set the list of warnings associated with the file upload.
	 *
	 * @param inWarnings The list of warnings associated with the file upload.
	 */
	public void setWarnings(List<FileWarning> inWarnings) {
		this.warnings = inWarnings;
		for (FileWarning warning : inWarnings) {
			FileUpload fileUpload = warning.getFileUpload();
			if (fileUpload == null || !fileUpload.getId().equals(this.getId())) {
				warning.setFileUpload(this);
			}
		}
	}

	/**
	 * Add a new warning to the file upload.
	 *
	 * @param warning The warning to add.
	 */
	public void addWarning(FileWarning warning) {
		FileUpload fileUpload = warning.getFileUpload();
		if (fileUpload == null || !fileUpload.getId().equals(this.getId())) {
			warning.setFileUpload(this);
		}
		this.warnings.add(warning);
	}

	/**
	 * Find out whether the file upload contains any errors.
	 *
	 * @return True if there are any errors, false otherwise.
	 */
	public boolean containsErrors() {
		return !this.errors.isEmpty();
	}

	/**
	 * Find out whether the file upload contains any warnings.
	 *
	 * @return True if there are any warnings, false otherwise.
	 */
	public boolean containsWarnings() {
		return !this.warnings.isEmpty();
	}
	
	@Override
	public Object onCycleDetected(Context inContext) {
		return null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
