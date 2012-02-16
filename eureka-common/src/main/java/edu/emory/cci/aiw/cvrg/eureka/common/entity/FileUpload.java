package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.xml.bind.CycleRecoverable;

/**
 * Hold information about a user's file upload.
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
	@GeneratedValue
	private Long id;
	/**
	 * The on-disk location of the uploaded file.
	 */
	private String location;
	/**
	 * The timestamp of the initial upload.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	/**
	 * Has the file upload been validated?
	 */
	private boolean validated;
	/**
	 * Has the file been processed?
	 */
	private boolean processed;
	/**
	 * Are we done with the file?
	 */
	private boolean completed;
	/**
	 * The user to which this upload belongs
	 */
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * Contains a list of errors found in the file.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = FileError.class,
			mappedBy = "fileUpload")
	List<FileError> errors = new ArrayList<FileError>();
	/**
	 * Contains a list of warnings found in the file.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = FileWarning.class,
			mappedBy = "fileUpload")
	List<FileWarning> warnings = new ArrayList<FileWarning>();

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
	@JsonManagedReference("user-fileuploads")
	public User getUser() {
		return this.user;
	}

	/**
	 * Set the user to which the file upload belongs.
	 * 
	 * @param inUser The user to which the file upload belongs.
	 */
	public void setUser(User inUser) {
		this.user = inUser;
		if (!this.user.getFileUploads().contains(this)) {
			this.user.addFileUpload(this);
		}
	}

	/**
	 * Get the list of errors associated with the file upload.
	 * 
	 * @return The list of errors associated with the file upload.
	 */
	public List<FileError> getErrors() {
		return this.errors;
	}

	/**
	 * Set the list of errors associated with the file upload.
	 * 
	 * @param inErrors The list of errors associated with the file upload.
	 */
	// public void setErrors(List<FileError> inErrors) {
	// this.errors = inErrors;
	// for (FileError error : inErrors) {
	// FileUpload fileUpload = error.getFileUpload();
	// if (fileUpload == null || fileUpload.getId() != this.getId()) {
	// error.setFileUpload(this);
	// }
	// }
	// }

	/**
	 * Add an error to the list of errors for the file upload.
	 * 
	 * @param error The error to add.
	 */
	public void addError(FileError error) {
		FileUpload fileUpload = error.getFileUpload();
		if (fileUpload == null || fileUpload.getId() != this.getId()) {
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
	public List<FileWarning> getWarnings() {
		return this.warnings;
	}

	/**
	 * Set the list of warnings associated with the file upload.
	 * 
	 * @param inWarnings The list of warnings associated with the file upload.
	 */
	// public void setWarnings(List<FileWarning> inWarnings) {
	// this.warnings = inWarnings;
	// for (FileWarning warning : inWarnings) {
	// FileUpload fileUpload = warning.getFileUpload();
	// if (fileUpload == null || fileUpload.getId() != this.getId()) {
	// warning.setFileUpload(this);
	// }
	// }
	// }

	/**
	 * Add a new warning to the file upload.
	 * 
	 * @param warning The warning to add.
	 */
	public void addWarning(FileWarning warning) {
		FileUpload fileUpload = warning.getFileUpload();
		if (fileUpload == null || fileUpload.getId() != this.getId()) {
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
		return this.errors.size() > 0;
	}

	/**
	 * Find out whether the file upload contains any warnings.
	 * 
	 * @return True if there are any warnings, false otherwise.
	 */
	public boolean containsWarnings() {
		return this.warnings.size() > 0;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FileUpload [id=").append(this.id).append(", location=")
				.append(this.location).append(", userId=")
				.append(this.user.getId()).append(", timestamp=")
				.append(this.timestamp).append(", validated=")
				.append(this.validated).append(", processed=")
				.append(this.processed).append(", completed=")
				.append(this.completed).append(", errors=").append(this.errors)
				.append(", warnings=").append(this.warnings).append("]");
		return builder.toString();
	}

}
