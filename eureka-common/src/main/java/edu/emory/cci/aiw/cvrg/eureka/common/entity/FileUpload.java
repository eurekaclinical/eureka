package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Hold information about a uer's file upload.
 * 
 * @author hrathod
 * 
 */
@XmlRootElement
@Entity
@Table(name = "file_uploads")
public class FileUpload {

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
	 * The user to which this upload belongs
	 */
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = User.class)
	private User user;
	/**
	 * Contains a list of errors found in the file.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = FileError.class)
	List<FileError> errors;
	/**
	 * Contains a list of warnings found in the file.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = FileWarning.class)
	List<FileWarning> warnings;

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
	 * Get the user to which the file upload belongs.
	 * 
	 * @return The user to which the file upload belongs.
	 */
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
	public void setErrors(List<FileError> inErrors) {
		this.errors = inErrors;
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
	public void setWarnings(List<FileWarning> inWarnings) {
		this.warnings = inWarnings;
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
}
