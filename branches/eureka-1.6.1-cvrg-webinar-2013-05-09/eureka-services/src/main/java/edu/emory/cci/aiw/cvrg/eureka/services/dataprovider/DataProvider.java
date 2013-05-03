/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

import java.io.IOException;
import java.util.List;

/**
 * An interface to provide functionality needed to provide data for upload to a
 * PROTEMPA schema. The data getter methods assume that any resources that need
 * to be opened to read the data have been opened. If {@link #close() } has
 * been called, the behavior of the getters is undefined.
 * 
 * @author hrathod
 * 
 */
public interface DataProvider {
	/**
	 * Get a list of patients from the data.
	 * 
	 * @return A list of {@link Patient} objects.
	 * @throws DataProviderException if an error occurred reading patients from
	 * the data source.
	 */
	public List<Patient> getPatients() throws DataProviderException;

	/**
	 * Get a list of providers from the data.
	 * 
	 * @return A list of {@link Provider} objects.
	 * @throws DataProviderException if an error occurred reading providers
	 * from the data source.
	 */
	public List<Provider> getProviders() throws DataProviderException;

	/**
	 * Get a list of encounters from the data.
	 * 
	 * @return A list of {@link Encounter} objects.
	 * @throws DataProviderException if an error occurred reading encounters
	 * from the data source.
	 */
	public List<Encounter> getEncounters() throws DataProviderException;

	public List<Enrollment> getEnrollments() throws DataProviderException;

	/**
	 * Get a list of CPT codes from the data.
	 * 
	 * @return A list of {@link CPT} objects.
	 * @throws DataProviderException if an error occurred reading CPT codes 
	 * from the data source.
	 */
	public List<CPT> getCptCodes() throws DataProviderException;

	/**
	 * Get a list of ICD9 Diagnostic codes from the data.
	 * 
	 * @return A list of {@link Icd9Diagnosis} objects.
	 * @throws DataProviderException if an error occurred reading ICD9 
	 * diagnosis codes from the data source.
	 */
	public List<Icd9Diagnosis> getIcd9Diagnoses() throws DataProviderException;

	/**
	 * Get a list of ICD9 Procedure codes from the data.
	 * 
	 * @return A list of {@link Icd9Procedure} objects.
	 * @throws DataProviderException if an error occurred reading ICD9 
	 * procedure codes from the data source.
	 */
	public List<Icd9Procedure> getIcd9Procedures() throws DataProviderException;

	/**
	 * Get a list of medication from the data.
	 * 
	 * @return A list of {@link Medication} objects.
	 * @throws DataProviderException if an error occurred reading medication 
	 * codes from the data source.
	 */
	public List<Medication> getMedications() throws DataProviderException;

	public List<MedicationDispense> getMedicationDispenses() throws
			DataProviderException;

	/**
	 * Get a list of lab results from the data.
	 * 
	 * @return A list of {@link Lab} objects.
	 * @throws DataProviderException if an error occurred reading laboratory
	 * test results from the data source.
	 */
	public List<Lab> getLabs() throws DataProviderException;

	/**
	 * Get a list of vitals from the data.
	 * 
	 * @return A list of {@link Vital} objects.
	 * @throws DataProviderException if an error occurred reading vital signs 
	 * from the data source.
	 */
	public List<Vital> getVitals() throws DataProviderException;
	
	/**
	 * Closes any underlying resources needed to read the data. May be called
	 * multiple times. After the first time, nothing will happen.
	 * 
	 * @throws IOException if an error occurred closing the underlying data
	 * source.
	 */
	public void close() throws IOException;
}
