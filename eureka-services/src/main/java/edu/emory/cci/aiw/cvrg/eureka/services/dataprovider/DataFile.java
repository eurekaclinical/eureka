package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A bean to hold information about a user's uploaded spreadsheet file.
 * 
 * @author hrathod
 * 
 */
@Entity
public class DataFile {

	/**
	 * The data file's unique identifier.
	 */
	@Id
	@GeneratedValue
	private Long id;
	/**
	 * The on-disk location of the data file.
	 */
	private String location;
	/**
	 * Did the data file have any warnings during validation?
	 */
	private boolean warnings;
	/**
	 * Did the data file have any errors during validation?
	 */
	private boolean errors;
	/**
	 * Whether the data file has already been validated or not.
	 */
	private boolean validated;

	/**
	 * Get the unique identifier for the data file.
	 * 
	 * @return The data file's unique identifier.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the data file's unique identifier.
	 * 
	 * @param inId The data file's unique identifier.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Get the on-disk location of the data file.
	 * 
	 * @return The on-disk location of the data file.
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * Set the on-disk location of the data file.
	 * 
	 * @param inLocation
	 */
	public void setLocation(String inLocation) {
		this.location = inLocation;
	}

	/**
	 * Get the warning flag for the data file.
	 * 
	 * @return True if there are warnings, false otherwise.
	 */
	public boolean isWarnings() {
		return this.warnings;
	}

	/**
	 * Set the warning flag for the data file.
	 * 
	 * @param inWarnings True, if there are warnings for the data file, false
	 *            otherwise.
	 */
	public void setWarnings(boolean inWarnings) {
		this.warnings = inWarnings;
	}

	/**
	 * Get the error flag for the data file.
	 * 
	 * @return True if the data file has errors, false otherwise.
	 */
	public boolean isErrors() {
		return this.errors;
	}

	/**
	 * Set the error flag for the data file.
	 * 
	 * @param inErrors True if there are errors in the data file, false
	 *            otherwise.
	 */
	public void setErrors(boolean inErrors) {
		this.errors = inErrors;
	}

	/**
	 * Get the validation flag for the data file.
	 * 
	 * @return True if the data file has already been validated, false
	 *         otherwise.
	 */
	public boolean isValidated() {
		return this.validated;
	}

	/**
	 * Set the validation flag for the data file.
	 * 
	 * @param inValidated True if the data file has been validated, false
	 *            otherwise.
	 */
	public void setValidated(boolean inValidated) {
		this.validated = inValidated;
	}
}
