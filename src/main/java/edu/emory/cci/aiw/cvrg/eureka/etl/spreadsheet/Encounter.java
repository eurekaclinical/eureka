/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
 * Hold information about a patient's encounter.
 *
 * @author hrathod
 *
 */
public class Encounter {
	/**
	 * The encounter's unique identifier.
	 */
	private Long id;
	/**
	 * The patient with which the encounter is associated.
	 */
	private Long patientId;
	/**
	 * The provider associated with this encounter.
	 */
	private Long providerId;
	/**
	 * The start time of the encounter.
	 */
	private Date start;
	/**
	 * The end time of the encounter.
	 */
	private Date end;
	/**
	 * The type of the encounter.
	 */
	private String type;
	/**
	 * The discharge disposition for the encounter.
	 */
	private String dischargeDisposition;

	/**
	 * Get the encounter's unique identifier.
	 *
	 * @return The encounter's unique identifier.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the encounter's unique identifier.
	 *
	 * @param inId The encounter's unique identifier.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Get the unique identifier for the patient associated with this encounter.
	 *
	 * @return The unique identifier of the patient associated with this
	 *         encounter.
	 */
	public Long getPatientId() {
		return this.patientId;
	}

	/**
	 * Set the unique identifier for the patient associated with this encounter.
	 *
	 * @param inPatientId The unique identifier for the patient associated with
	 *            this encounter.
	 */
	public void setPatientId(Long inPatientId) {
		this.patientId = inPatientId;
	}

	/**
	 * Get the unique identifier for the provider associated with this
	 * encounter.
	 *
	 * @return The provider's unique identifier
	 */
	public Long getProviderId() {
		return this.providerId;
	}

	/**
	 * Set the unique identifier for the provider associated with this
	 * encounter.
	 *
	 * @param inProviderId The provider's unique identifier.
	 */
	public void setProviderId(Long inProviderId) {
		this.providerId = inProviderId;
	}

	/**
	 * Get the encounter's start date.
	 *
	 * @return The encounter's start date.
	 */
	public Date getStart() {
		return new Date(this.start.getTime());
	}

	/**
	 * Set the encounter's start date.
	 *
	 * @param inStart The encounter's start date.
	 */
	public void setStart(Date inStart) {
		this.start = new Date(inStart.getTime());
	}

	/**
	 * Get the encounter's end date.
	 *
	 * @return The encounter's end date.
	 */
	public Date getEnd() {
		return new Date(this.end.getTime());
	}

	/**
	 * Set the encounter's end date.
	 *
	 * @param inEnd The encounter's end date.
	 */
	public void setEnd(Date inEnd) {
		this.end = new Date(inEnd.getTime());
	}

	/**
	 * Get the encounter's type.
	 *
	 * @return The encounter's type.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Set the encounter's type.
	 *
	 * @param inType The encounter's type.
	 */
	public void setType(String inType) {
		this.type = inType;
	}

	/**
	 * Get the encounter's discharge disposition.
	 *
	 * @return The encounter's discharge disposition.
	 */
	public String getDischargeDisposition() {
		return this.dischargeDisposition;
	}

	/**
	 * Set the encounter's discharge disposition.
	 *
	 * @param inDischargeDisposition The encounter's discharge disposition.
	 */
	public void setDischargeDisposition(String inDischargeDisposition) {
		this.dischargeDisposition = inDischargeDisposition;
	}
}
