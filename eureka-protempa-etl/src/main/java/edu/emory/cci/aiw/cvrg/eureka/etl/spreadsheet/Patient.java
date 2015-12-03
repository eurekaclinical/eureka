/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2015 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.spreadsheet;

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
		return new Date(this.dateOfBirth.getTime());
	}

	/**
	 * Set the patient's date of birth.
	 *
	 * @param inDateOfBirth The patient's date of birth.
	 */
	public void setDateOfBirth(Date inDateOfBirth) {
		this.dateOfBirth = new Date(inDateOfBirth.getTime());
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
