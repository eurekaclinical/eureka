/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.etl.spreadsheet;

/**
 * A person related to an encounter, such as a provider or a patient.
 * 
 * @author hrathod
 * 
 */
public interface Person {

	/**
	 * Get the person's unique identifier.
	 * 
	 * @return The person's unique identifier.
	 */
	public abstract Long getId();

	/**
	 * Set the person's unique identifier.
	 * 
	 * @param inId The person's unique identifier.
	 */
	public abstract void setId(Long inId);

	/**
	 * Get the person's first name.
	 * 
	 * @return The person's first name.
	 */
	public abstract String getFirstName();

	/**
	 * Set the person's first name.
	 * 
	 * @param inFirstName The person's first name.
	 */
	public abstract void setFirstName(String inFirstName);

	/**
	 * Get the person's last name.
	 * 
	 * @return The person's last name.
	 */
	public abstract String getLastName();

	/**
	 * Set the person's last name.
	 * 
	 * @param inLastName The person's last name.
	 */
	public abstract void setLastName(String inLastName);

}
