package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

import java.util.Date;

/**
 * Holds information about a patient.
 * 
 * @author hrathod
 * 
 */
public class Patient extends PersonImpl {
	/**
	 * Patient's date of birth.
	 */
	private Date dateOfBirth;
	/**
	 * Language spoken by the patient.
	 */
	private String language;
	/**
	 * Patient's marital status.
	 */
	private String maritalStatus;
	/**
	 * Patient's race.
	 */
	private String race;
	/**
	 * Patient's gender.
	 */
	private String gender;

	/**
	 * Get the patient's date of birth.
	 * 
	 * @return The patient's date of birth.
	 */
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	/**
	 * Set the patient's date of birth.
	 * 
	 * @param inDateOfBirth The patient's date of birth.
	 */
	public void setDateOfBirth(Date inDateOfBirth) {
		this.dateOfBirth = inDateOfBirth;
	}

	/**
	 * Get the language spoken by the patient.
	 * 
	 * @return The language spoken by the patient.
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * Set the language spoken by the patient.
	 * 
	 * @param inLlanguage The language spoken by the patient.
	 */
	public void setLanguage(String inLlanguage) {
		this.language = inLlanguage;
	}

	/**
	 * Get the patient's marital status.
	 * 
	 * @return The patient's marital status.
	 */
	public String getMaritalStatus() {
		return this.maritalStatus;
	}

	/**
	 * Set the patient's marital status.
	 * 
	 * @param inMaritalStatus The patient's marital status.
	 */
	public void setMaritalStatus(String inMaritalStatus) {
		this.maritalStatus = inMaritalStatus;
	}

	/**
	 * Get the patient's race.
	 * 
	 * @return The patient's race.
	 */
	public String getRace() {
		return this.race;
	}

	/**
	 * Set the patient's race.
	 * 
	 * @param inRace The patient's race.
	 */
	public void setRace(String inRace) {
		this.race = inRace;
	}

	/**
	 * Get the patient's gender.
	 * 
	 * @return The patient's gender.
	 */
	public String getGender() {
		return this.gender;
	}

	/**
	 * Set the patient's gender.
	 * 
	 * @param inGender The patient's gender.
	 */
	public void setGender(String inGender) {
		this.gender = inGender;
	}
}
